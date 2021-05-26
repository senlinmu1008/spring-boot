package net.zhaoxiaobin.httpdecrypt.filter;

import net.zhaoxiaobin.httpdecrypt.utils.HttpUtil;
import jodd.util.Base64;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.function.Function;

/**
 * 实现加解密的过滤器
 *
 * @author zhaoxb
 * @create 2019-10-04 20:21
 */
@WebFilter(urlPatterns = {"/decrypt/*"}, filterName = "decryptFilter")
@Slf4j
public class DecryptFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Function<String, String> modifyRequestBodyFun = Base64::decodeToString; // 解密函数
        Function<String, String> modifyResponseBodyFun = Base64::encodeToString; // 加密函数
        HttpUtil.modifyHttpData(request, response, chain, modifyRequestBodyFun, modifyResponseBodyFun);
    }
}