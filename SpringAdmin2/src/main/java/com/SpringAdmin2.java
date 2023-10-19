package com;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ming.li
 * @date 2023/10/18 11:02
 */
@SpringCloudApplication
@EnableDiscoveryClient
@EnableFeignClients
public class SpringAdmin2 {
    public static void main(String[] args) {
        SpringApplication.run(SpringAdmin2.class,args);
    }
}
