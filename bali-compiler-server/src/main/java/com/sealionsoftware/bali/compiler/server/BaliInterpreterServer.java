package com.sealionsoftware.bali.compiler.server;

import bali.Character;
import bali.Group;
import bali.Integer;
import bali.Iterator;
import bali.Logic;
import bali.Text;
import bali.Writer;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sealionsoftware.bali.compiler.Interpreter;
import com.sealionsoftware.bali.compiler.ListTextBufferWriter;
import com.sealionsoftware.bali.compiler.StandardInterpreter;
import com.sealionsoftware.bali.compiler.execution.ReflectiveExecutor;
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
                .addSerializer(Logic.class, new LogicSerializer())
                .addSerializer(Integer.class, new IntegerSerializer())
                .addSerializer(Text.class, new TextSerializer())
                .addSerializer(Character.class, new CharacterSerializer())
                .addSerializer(Group.class, new GroupSerializer())
                .addSerializer(Iterator.class, new IteratorSerializer()));
    }

    @Bean
    public AsyncTaskExecutor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(3);
        return executor;
    }

    @Bean @Scope("request")
    public ListTextBufferWriter console() {
        return new ListTextBufferWriter();
    }

    @Bean @Scope("request")
    public Interpreter interpreter(Writer console) {
        return new StandardInterpreter(
               null, null, null, new ReflectiveExecutor(console)
        );
    }

    public static void main(String... args) throws Exception {
        SpringApplication.run(new Class[]{BaliInterpreterServer.class}, args);
    }

}
