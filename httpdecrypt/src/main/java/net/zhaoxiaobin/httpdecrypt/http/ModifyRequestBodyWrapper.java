/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.httpdecrypt.http;

import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * 修改http请求体和contentType后构建新的请求对象
 * 只针对请求体可读的请求类型
 *
 * @author zhaoxb
 * @create 2019-09-26 17:49
 */
@Data
public class ModifyRequestBodyWrapper extends HttpServletRequestWrapper {
    /**
     * 原请求对象
     */
    private HttpServletRequest orginalRequest;
    /**
     * 修改后的请求体
     */
    private String modifyRequestBody;
    /**
     * 修改后的请求类型
     */
    private String contentType;

    /**
     * 修改请求体，请求类型沿用原来的
     *
     * @param orginalRequest    原请求对象
     * @param modifyRequestBody 修改后的请求体
     */
    public ModifyRequestBodyWrapper(HttpServletRequest orginalRequest, String modifyRequestBody) {
        this(orginalRequest, modifyRequestBody, null);
    }

    /**
     * 修改请求体和请求类型
     *
     * @param orginalRequest    原请求对象
     * @param modifyRequestBody 修改后的请求体
     * @param contentType       修改后的请求类型
     */
    public ModifyRequestBodyWrapper(HttpServletRequest orginalRequest, String modifyRequestBody, String contentType) {
        super(orginalRequest);
        this.modifyRequestBody = modifyRequestBody;
        this.orginalRequest = orginalRequest;
        this.contentType = contentType;
    }

    /**
     * 构建新的输入流，在新的输入流中放入修改后的请求体（使用原请求中的字符集）
     *
     * @return 新的输入流（包含修改后的请求体）
     */
    @Override
    @SneakyThrows
    public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            private InputStream in = new ByteArrayInputStream(modifyRequestBody.getBytes(orginalRequest.getCharacterEncoding()));

            @Override
            public int read() throws IOException {
                return in.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    /**
     * 获取新的请求体大小
     *
     * @return
     */
    @Override
    @SneakyThrows
    public int getContentLength() {
        return modifyRequestBody.getBytes(orginalRequest.getCharacterEncoding()).length;
    }

    /**
     * 获取新的请求体大小
     *
     * @return
     */
    @Override
    @SneakyThrows
    public long getContentLengthLong() {
        return modifyRequestBody.getBytes(orginalRequest.getCharacterEncoding()).length;
    }

    /**
     * 获取新的请求类型，默认沿用原请求的
     *
     * @return
     */
    @Override
    public String getContentType() {
        return StringUtils.isBlank(contentType) ? orginalRequest.getContentType() : contentType;
    }

    /**
     * 修改contentType
     *
     * @param name 请求头
     * @return
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        if (null != name && name.replace("-", "").toLowerCase().equals("contenttype") && !StringUtils.isBlank(contentType)) {
            return new Enumeration<String>() {
                private boolean hasGetted = false;

                @Override
                public boolean hasMoreElements() {
                    return !hasGetted;
                }

                @Override
                public String nextElement() {
                    if (hasGetted) {
                        throw new NoSuchElementException();
                    } else {
                        hasGetted = true;
                        return contentType;
                    }
                }
            };
        }
        return super.getHeaders(name);
    }
}