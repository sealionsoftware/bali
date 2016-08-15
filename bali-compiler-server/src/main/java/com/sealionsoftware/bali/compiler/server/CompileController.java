package com.sealionsoftware.bali.compiler.server;


import com.sealionsoftware.bali.compiler.Interpreter;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller
@CrossOrigin({"http://sealionsoftware.github.io", "localhost"})
@Scope("request")
public class CompileController {

    @Inject
    private AsyncTaskExecutor executor;
    @Inject
    private Interpreter interpreter;


    @RequestMapping(value = "/fragment", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> compileFragment(@RequestBody String body) throws Exception {
        return runWithTimeout(executor.submit(() -> {
            return interpreter.run(body);
        }));
    }

    @RequestMapping(value = "/expression", method = RequestMethod.POST)
    public @ResponseBody Object compileExpression(@RequestBody String body) throws Exception {
        return runWithTimeout(executor.submit(() -> {
            return interpreter.evaluate(body);
        }));
    }

    private <T> T runWithTimeout(Future<T> future) throws Exception {
        try {
            return future.get(1, TimeUnit.SECONDS);
        }  catch (ExecutionException exception){
            Throwable cause = exception.getCause();
            throw (cause instanceof Exception) ? (Exception) cause : exception;
        } catch (TimeoutException timeout) {
            future.cancel(true);
            throw new RuntimeException("The submitted code timed out before completing", timeout);
        }
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(HttpMessageNotReadableException exception){
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}