package net.zhaoxiaobin.socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:spring-socket.xml")
public class SocketActionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocketActionApplication.class, args);
    }

}
