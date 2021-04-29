/**
 * Copyright (C), 2015-2020
 */
package com.zxb.libai.utils;

import com.jcraft.jsch.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

/**
 * @author zhaoxb
 * @create 2020-02-25 12:45
 */
@Slf4j
public class SFTPUtils {
    private String host;
    private String username;
    private String password;
    private int port = 22;
    private ChannelSftp sftp;
    private Session sshSession;

    public SFTPUtils() {
    }

    public SFTPUtils(String host, String username, String password, int port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
    }

    public SFTPUtils(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    /**
     * connect server via sftp
     */
    @SneakyThrows
    public void connect() {
        JSch jsch = new JSch();
        jsch.getSession(username, host, port);
        sshSession = jsch.getSession(username, host, port);
        log.info("Session created.");
        sshSession.setPassword(password);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        sshSession.setConfig(sshConfig);
        sshSession.connect();
        log.info("Session connected.");
        log.info("Opening Channel.");
        Channel channel = sshSession.openChannel("sftp");
        channel.connect();
        sftp = (ChannelSftp) channel;
        log.info("Connected to " + host + ".");
    }


    /**
     * 关闭资源
     */
    public void disconnect() {
        if (this.sftp != null && this.sftp.isConnected()) {
            this.sftp.disconnect();
            log.info("sftp is closed already");
        }

        if (this.sshSession != null && this.sshSession.isConnected()) {
            this.sshSession.disconnect();
            log.info("sshSession is closed already");
        }

    }

    /**
     * 批量下载文件
     *
     * @param remotPath  远程下载目录(以路径符号结束)
     * @param localPath  本地保存目录(以路径符号结束)
     * @param fileFormat 下载文件格式(以特定字符开头,为空不做检验)
     * @param del        下载后是否删除sftp文件
     * @return
     */
    @SneakyThrows
    public void batchDownLoadFile(String remotPath, String localPath, String fileFormat, boolean del) {
        try {
            connect();
            Vector v = listFiles(remotPath);
            if (v.size() <= 0) {
                return;
            }
            Iterator it = v.iterator();
            while (it.hasNext()) {
                ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) it.next();
                String filename = entry.getFilename();
                SftpATTRS attrs = entry.getAttrs();
                if (attrs.isDir()) {
                    continue;
                }
                if (StringUtils.isNotBlank(fileFormat)) {
                    if (filename.startsWith(fileFormat) && this.downloadFile(remotPath, filename, localPath, filename) && del) {
                        deleteSFTP(remotPath, filename);
                    }
                } else {
                    if (this.downloadFile(remotPath, filename, localPath, filename) && del) {
                        deleteSFTP(remotPath, filename);
                    }
                }
            }
        } finally {
            this.disconnect();
        }
    }

    /**
     * 下载单个文件
     *
     * @param remotePath     远程下载目录(以路径符号结束)
     * @param remoteFileName 下载文件名
     * @param localPath      本地保存目录(以路径符号结束)
     * @param localFileName  保存文件名
     * @return
     */
    @SneakyThrows
    public boolean downloadFile(String remotePath, String remoteFileName, String localPath, String localFileName) {
        sftp.cd(remotePath);
        File file = new File(localPath + localFileName);
        mkdirs(localPath + localFileName);
        sftp.get(remoteFileName, new FileOutputStream(file));
        return true;
    }

    /**
     * 上传单个文件
     *
     * @param remotePath     远程保存目录
     * @param remoteFileName 保存文件名
     * @param localPath      本地上传目录(以路径符号结束)
     * @param localFileName  上传的文件名
     * @return
     */
    @SneakyThrows
    public boolean uploadFile(String remotePath, String remoteFileName, String localPath, String localFileName) {
        createDir(remotePath);
        File file = new File(localPath + localFileName);
        FileInputStream in = new FileInputStream(file);
        sftp.put(in, remoteFileName);
        return true;
    }

    /**
     * 批量上传文件
     *
     * @param remotePath 远程保存目录
     * @param localPath  本地上传目录(以路径符号结束)
     * @param del        上传后是否删除本地文件
     * @return
     */
    public void bacthUploadFile(String remotePath, String localPath, boolean del) {
        try {
            connect();
            File file = new File(localPath);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (!files[i].isFile() || files[i].getName().endsWith("bak")) {
                    continue;
                }
                if (this.uploadFile(remotePath, files[i].getName(), localPath, files[i].getName()) && del) {
                    deleteFile(localPath + files[i].getName());
                }
            }
        } finally {
            this.disconnect();
        }
    }

    /**
     * 删除本地文件
     *
     * @param filePath
     * @return
     */
    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile() && file.delete();
    }

    /**
     * 创建目录
     *
     * @param createpath
     * @return
     */
    @SneakyThrows
    public boolean createDir(String createpath) {
        if (isDirExist(createpath)) {
            this.sftp.cd(createpath);
            return true;
        }
        String pathArry[] = createpath.split("/");
        StringBuffer filePath = new StringBuffer("/");
        for (String path : pathArry) {
            if (StringUtils.isBlank(path)) {
                continue;
            }
            filePath.append(path + "/");
            if (isDirExist(filePath.toString())) {
                sftp.cd(filePath.toString());
            } else {
                // 建立目录
                sftp.mkdir(filePath.toString());
                // 进入并设置为当前目录
                sftp.cd(filePath.toString());
            }

        }
        this.sftp.cd(createpath);
        return true;
    }

    /**
     * 判断目录是否存在
     *
     * @param directory
     * @return
     */
    @SneakyThrows
    public boolean isDirExist(String directory) {
        SftpATTRS sftpATTRS = sftp.lstat(directory);
        return sftpATTRS.isDir();
    }

    /**
     * 删除stfp文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    @SneakyThrows
    public void deleteSFTP(String directory, String deleteFile) {
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }

    /**
     * 如果目录不存在就创建目录
     *
     * @param path
     */
    public void mkdirs(String path) {
        File f = new File(path);
        String fs = f.getParent();
        f = new File(fs);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @return
     * @throws SftpException
     */
    @SneakyThrows
    public Vector listFiles(String directory) {
        return sftp.ls(directory);
    }

}

