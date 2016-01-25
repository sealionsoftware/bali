package com.sealionsoftware.bali.compiler.server;

import bali.Boolean;
import bali.Integer;
import bali.Text;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sealionsoftware.bali.compiler.Interpreter;
import com.sealionsoftware.bali.compiler.StandardInterpreter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class BaliInterpreterServer {

    @Inject
    private ObjectMapper mapper;

    @PostConstruct
    public void initialise(){

        mapper.registerModule(new SimpleModule("BaliModule", new Version(1, 0, 0, null, null, null))
                .addSerializer(Boolean.class, new BooleanSerializer())
                .addSerializer(Integer.class, new IntegerSerializer())
                .addSerializer(Text.class, new TextSerializer()));
    }

    @Bean
    public AsyncTaskExecutor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(3);
        return executor;
    }

    @Bean @Scope("request")
    public Interpreter interpreter(){
        return new StandardInterpreter();
    }

    public static void main(String... args) throws Exception {
        SpringApplication.run(new Class[]{BaliInterpreterServer.class}, args);
    }

}
