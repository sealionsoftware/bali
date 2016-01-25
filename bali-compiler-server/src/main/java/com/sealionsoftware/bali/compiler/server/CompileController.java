package com.sealionsoftware.bali.compiler.server;


import com.sealionsoftware.bali.compiler.Interpreter;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller
@Scope("request")
public class CompileController {

    @Inject
    private AsyncTaskExecutor executor;
    @Inject
    private Interpreter interpreter;

    @RequestMapping("/fragment")
    public @ResponseBody Map<String, Object> compileFragment(@RequestBody String body) throws Exception {

        Future<Map<String, Object>> future = executor.submit(() -> {
            return interpreter.run(body);
        });

        try {
            return future.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException timeout) {
            future.cancel(true);
            throw timeout;
        }
    }

}