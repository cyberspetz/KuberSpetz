package com.swami.transactionengine.kuberspetz;

import org.springframework.boot.SpringApplication;

public class TestKuberSpetzApplication {

    public static void main(String[] args) {
        SpringApplication.from(KuberSpetzApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
