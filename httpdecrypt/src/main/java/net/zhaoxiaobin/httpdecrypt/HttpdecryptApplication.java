package net.zhaoxiaobin.httpdecrypt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class HttpdecryptApplication {
    public static void main(String[] args) {
        SpringApplication.run(HttpdecryptApplication.class, args);
    }
}
