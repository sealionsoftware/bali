package com.sealionsoftware.bali.compiler.server;


import com.sealionsoftware.bali.compiler.CompilationException;
import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.Interpreter;
import com.sealionsoftware.bali.compiler.TextBuffer;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Controller
@CrossOrigin({"https://sealionsoftware.github.io", "http://localhost"})
@Scope("request")
public class CompileController {

    @Inject
    private AsyncTaskExecutor executor;
    @Inject
    private Interpreter interpreter;
    @Inject
    private TextBuffer console;


    @RequestMapping(value = "/fragment", method = RequestMethod.POST)
    public @ResponseBody List<String> compileFragment(@RequestBody String body) throws Exception {
        runWithTimeout(executor.submit(() -> interpreter.run(body)));
        return console.getWrittenLines().stream().map(bali.text.Primitive::convert).collect(Collectors.toList());
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

    @ExceptionHandler @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException exception){
        exception.printStackTrace();
    }

    @ExceptionHandler @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody List<CompileError> handle(CompilationException exception){
        return exception.errorList;
    }

    @ExceptionHandler @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object handle(bali.RuntimeException exception){
        return exception.payload;
    }

}