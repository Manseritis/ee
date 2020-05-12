package com.example.demo;

import org.apache.tomcat.jni.Global;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        String classpath = Global.class.getResource("/").getPath();
        System.out.println(classpath);
    }

}
