/**
 * Copyright (C), 2015-2019
 */
package net.zhaoxiaobin.httpdecrypt.http;

import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * 构建新的响应对象，缓存响应体
 * 可以通过此对象获取响应体，然后进行修改，通过原响应流返回给调用方
 *
 * @author zhaoxb
 * @create 2019-09-26 17:52
 */
@Data
public class ModifyResponseBodyWrapper extends HttpServletResponseWrapper {
    /**
     * 原响应对象
     */
    private HttpServletResponse originalResponse;
    /**
     * 缓存响应体的输出流（低级流）
     */
    private ByteArrayOutputStream baos;
    /**
     * 输出响应体的高级流
     */
    private ServletOutputStream out;
    /**
     * 输出响应体的字符流
     */
    private PrintWriter writer;

    /**
     * 构建新的响应对象
     *
     * @param resp 原响应对象
     */
    @SneakyThrows
    public ModifyResponseBodyWrapper(HttpServletResponse resp) {
        super(resp);
        this.originalResponse = resp;
        this.baos = new ByteArrayOutputStream();
        this.out = new SubServletOutputStream(baos);
        this.writer = new PrintWriter(new OutputStreamWriter(baos));
    }

    /**
     * 获取输出流
     *
     * @return
     */
    @Override
    public ServletOutputStream getOutputStream() {
        return out;
    }

    /**
     * 获取输出流（字符）
     *
     * @return
     */
    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    /**
     * 获取响应体
     *
     * @return
     * @throws IOException
     */
    public String getResponseBody() throws IOException {
        return this.getResponseBody(null);
    }

    /**
     * 通过指定字符集获取响应体
     *
     * @param charset 字符集，指定响应体的编码格式
     * @return
     * @throws IOException
     */
    public String getResponseBody(String charset) throws IOException {
        /**
         * 应用层会用ServletOutputStream或PrintWriter字符流来输出响应
         * 需要把这2个流中的数据强制刷到ByteArrayOutputStream这个流中，否则取不到响应数据或数据不完整
         */
        out.flush();
        writer.flush();
        return new String(baos.toByteArray(), StringUtils.isBlank(charset) ? this.getCharacterEncoding() : charset);
    }

    /**
     * 输出流，应用层会用此流来写出响应体
     */
    class SubServletOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream baos;

        public SubServletOutputStream(ByteArrayOutputStream baos) {
            this.baos = baos;
        }

        @Override
        public void write(int b) {
            baos.write(b);
        }

        @Override
        public void write(byte[] b) {
            baos.write(b, 0, b.length);
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }
}