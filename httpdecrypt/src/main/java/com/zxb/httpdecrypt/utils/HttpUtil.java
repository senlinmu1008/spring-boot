/**
 * Copyright (C), 2015-2019
 */
package com.zxb.httpdecrypt.utils;

import com.zxb.httpdecrypt.http.ModifyRequestBodyWrapper;
import com.zxb.httpdecrypt.http.ModifyResponseBodyWrapper;
import jodd.servlet.ServletUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Function;

/**
 * 封装对修改http请求体和响应体的函数
 *
 * @author zhaoxb
 * @create 2019-10-05 19:34
 */
@Slf4j
public class HttpUtil {
    /**
     * 修改http请求体/响应体
     *
     * @param originalRequest       原请求对象
     * @param originalResponse      原响应对象
     * @param chain                 调用链
     * @param modifyRequestBodyFun  修改请求体函数
     * @param modifyResponseBodyFun 修改响应体函数
     * @throws IOException
     * @throws ServletException
     */
    public static void modifyHttpData(ServletRequest originalRequest, ServletResponse originalResponse, FilterChain chain,
                                      Function<String, String> modifyRequestBodyFun, Function<String, String> modifyResponseBodyFun) throws IOException, ServletException {
        modifyHttpData(originalRequest, originalResponse, chain, modifyRequestBodyFun, modifyResponseBodyFun, null);
    }

    /**
     * 修改http请求体/响应体
     *
     * @param request               原请求对象
     * @param response              原响应对象
     * @param chain                 调用链
     * @param modifyRequestBodyFun  修改请求体函数
     * @param modifyResponseBodyFun 修改响应体函数
     * @param requestContentType    修改后的请求类型
     * @throws IOException
     * @throws ServletException
     */
    public static void modifyHttpData(ServletRequest request, ServletResponse response, FilterChain chain,
                                      Function<String, String> modifyRequestBodyFun, Function<String, String> modifyResponseBodyFun,
                                      String requestContentType) throws IOException, ServletException {
        /**
         * 1.原请求/响应对象强转
         */
        HttpServletRequest originalRequest = (HttpServletRequest) request;
        HttpServletResponse originalResponse = (HttpServletResponse) response;

        /**
         * 2.读取原请求体（密文），执行修改请求体函数得到修改后的请求体（明文），然后构建新的请求对象（包含修改后的请求体）
         */
        String originalRequestBody = ServletUtil.readRequestBody(originalRequest); // 读取原请求体（密文）
        String modifyRequestBody = modifyRequestBodyFun.apply(originalRequestBody); // 修改请求体（明文）
        ModifyRequestBodyWrapper requestWrapper = modifyRequestBodyAndContentType(originalRequest, modifyRequestBody, requestContentType);

        /**
         * 3.构建新的响应对象，执行调用链（用新的请求对象和响应对象）
         * 得到应用层的响应后（明文），执行修改响应体函数，最后得到需要响应给调用方的响应体（密文）
         */
        ModifyResponseBodyWrapper responseWrapper = getHttpResponseWrapper(originalResponse);
        chain.doFilter(requestWrapper, responseWrapper);
        String originalResponseBody = responseWrapper.getResponseBody(); // 原响应体（明文）
        String modifyResponseBody = modifyResponseBodyFun.apply(originalResponseBody); // 修改后的响应体（密文）

        /**
         * 4.将修改后的响应体用原响应对象的输出流来输出
         * 要保证响应类型和原请求中的一致，并重新设置响应体大小
         */
        originalResponse.setContentType(requestWrapper.getOrginalRequest().getContentType()); // 与请求时保持一致
        byte[] responseData = modifyResponseBody.getBytes(responseWrapper.getCharacterEncoding()); // 编码与实际响应一致
        originalResponse.setContentLength(responseData.length);
        @Cleanup ServletOutputStream out = originalResponse.getOutputStream();
        out.write(responseData);
    }

    /**
     * 修改请求体
     *
     * @param request           原请求
     * @param modifyRequestBody 修改后的请求体
     * @return
     */
    public static ModifyRequestBodyWrapper modifyRequestBody(ServletRequest request, String modifyRequestBody) {
        return modifyRequestBodyAndContentType(request, modifyRequestBody, null);
    }

    /**
     * 修改请求体和请求类型
     *
     * @param request           原请求
     * @param modifyRequestBody 修改后的请求体
     * @param contentType       请求类型
     * @return
     */
    public static ModifyRequestBodyWrapper modifyRequestBodyAndContentType(ServletRequest request, String modifyRequestBody, String contentType) {
        log.debug("ContentType改为 -> {}", contentType);
        HttpServletRequest orginalRequest = (HttpServletRequest) request;
        return new ModifyRequestBodyWrapper(orginalRequest, modifyRequestBody, contentType);
    }

    /**
     * 用原响应对象来构建新的http响应包装对象
     *
     * @param response 原响应对象
     * @return
     */
    public static ModifyResponseBodyWrapper getHttpResponseWrapper(ServletResponse response) {
        HttpServletResponse originalResponse = (HttpServletResponse) response;
        return new ModifyResponseBodyWrapper(originalResponse);
    }
}