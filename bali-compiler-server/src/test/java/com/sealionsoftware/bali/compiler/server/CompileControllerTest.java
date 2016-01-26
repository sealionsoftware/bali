package com.sealionsoftware.bali.compiler.server;

import com.sealionsoftware.bali.compiler.CompilationException;
import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.Interpreter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.sealionsoftware.Constant.map;
import static com.sealionsoftware.Constant.put;
import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.Matchers.withMessage;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompileControllerTest {

    @Mock
    private AsyncTaskExecutor executor;
    @Mock
    private Interpreter interpreter;

    @InjectMocks
    private CompileController subject;

    @Test
    public void testCompileFragment() throws Exception {

        String name = "greeting";
        String value = "Hello World";
        String fragment = "var Text " + name + " = \"" + value + "\"";

        @SuppressWarnings("unchecked")
        Future<Map<String, Object>> future = mock(Future.class);
        Map<String, Object> mockOutput = map(put(name, value));

        when(executor.submit(Mockito.<Callable<Map<String, Object>>>any())).thenReturn(future);
        when(future.get(any(long.class), any(TimeUnit.class))).thenReturn(mockOutput);

        Map<String, Object> output = subject.compileFragment(fragment);
        assertThat(output, hasEntry(name, value));

        ArgumentCaptor<Callable> callCapture = ArgumentCaptor.forClass(Callable.class);
        verify(executor).submit(callCapture.capture());
        callCapture.getValue().call();
        verify(interpreter).run(fragment);
    }

    @Test
    public void testCompileTimeout() throws Exception {

        @SuppressWarnings("unchecked")
        Future<Map<String, Object>> future = mock(Future.class);

        when(executor.submit(Mockito.<Callable<Map<String, Object>>>any())).thenReturn(future);
        when(future.get(any(long.class), any(TimeUnit.class))).thenThrow(new TimeoutException());

        Callable invocation = () -> subject.compileFragment("var Text greeting = \"Hello World\"");
        assertThat(invocation, throwsException(withMessage("The submitted code timed out before completing")));
        verify(future).cancel(true);
    }

    @Test
    public void testCompileExpression() throws Exception {

        String value = "Hello World";

        @SuppressWarnings("unchecked")
        Future<Object> future = mock(Future.class);

        when(executor.submit(Mockito.<Callable<Object>>any())).thenReturn(future);
        when(future.get(any(long.class), any(TimeUnit.class))).thenReturn(value);

        Object output = subject.compileExpression(value);
        assertThat(output, equalTo(value));

        ArgumentCaptor<Callable> callCapture = ArgumentCaptor.forClass(Callable.class);
        verify(executor).submit(callCapture.capture());
        callCapture.getValue().call();
        verify(interpreter).evaluate(value);
    }

    @Test
    public void testCompileInvalidExpression() throws Exception {

        String value = "1 + true";

        @SuppressWarnings("unchecked")
        Future<Object> future = mock(Future.class);

        when(executor.submit(Mockito.<Callable<Object>>any())).thenReturn(future);

        CompileError mockError = mock(CompileError.class);
        when(mockError.toString()).thenReturn("SUCKY_CODE");
        Exception toThrow = new CompilationException(asList(mockError));

        when(future.get(any(long.class), any(TimeUnit.class))).thenThrow(new ExecutionException(toThrow));

        Callable output = () -> subject.compileExpression(value);
        assertThat(output, throwsException(withMessage(equalTo("Compilation Failed: [SUCKY_CODE]"))));

        ArgumentCaptor<Callable> callCapture = ArgumentCaptor.forClass(Callable.class);
        verify(executor).submit(callCapture.capture());
        callCapture.getValue().call();
        verify(interpreter).evaluate(value);
    }

    @Test
    public void testHandleBadRequest(){

        ResponseEntity entity = subject.handle(mock(HttpMessageNotReadableException.class));

        assertThat(entity, notNullValue());
        assertThat(entity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }
}