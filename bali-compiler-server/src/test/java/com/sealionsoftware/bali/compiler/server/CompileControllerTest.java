package com.sealionsoftware.bali.compiler.server;

import com.sealionsoftware.bali.compiler.CompilationException;
import com.sealionsoftware.bali.compiler.CompileError;
import com.sealionsoftware.bali.compiler.ErrorCode;
import com.sealionsoftware.bali.compiler.Interpreter;
import com.sealionsoftware.bali.compiler.TextBuffer;
import com.sealionsoftware.bali.compiler.tree.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static bali.text.Primitive.convert;
import static com.sealionsoftware.Matchers.throwsException;
import static com.sealionsoftware.Matchers.withMessage;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompileControllerTest {

    @Mock
    private TextBuffer buffer;
    @Mock
    private AsyncTaskExecutor executor;
    @Mock
    private Interpreter interpreter;

    @InjectMocks
    private CompileController subject;

    @Test @SuppressWarnings("unchecked")
    public void testCompileFragment() throws Exception {

        String value = "Hello World";
        String fragment = "console << " + value;

        Future future = mock(Future.class);
        @SuppressWarnings("unchecked")
        List<String> mockOutput = mock(List.class);

        when(executor.submit(any(Runnable.class))).thenReturn(future);
        when(future.get(any(long.class), any(TimeUnit.class))).thenReturn(mockOutput);
        when(buffer.getWrittenLines()).thenReturn(asList(convert(value)));

        List<String> output = subject.compileFragment(fragment);
        assertThat(output, contains(value));

        ArgumentCaptor<Runnable> callCapture = ArgumentCaptor.forClass(Runnable.class);
        verify(executor).submit(callCapture.capture());
        callCapture.getValue().run();
        verify(interpreter).run(fragment);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCompileTimeout() throws Exception {

        Future future = mock(Future.class);

        when(executor.submit(any(Runnable.class))).thenReturn(future);
        when(future.get(any(long.class), any(TimeUnit.class))).thenThrow(new TimeoutException());

        Runnable invocation = () -> {
            try {
                subject.compileFragment("var Text greeting = \"Hello World\"");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        };
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
    @SuppressWarnings("unchecked")
    public void testCompileInvalidExpression() throws Exception {

        String value = "1 + true";

        @SuppressWarnings("unchecked")
        Future<Object> future = mock(Future.class);

        when(executor.submit(Mockito.<Callable<Object>>any())).thenReturn(future);

        CompileError mockError = mock(CompileError.class);
        when(mockError.toString()).thenReturn("SUCKY_CODE");
        Exception toThrow = new CompilationException(asList(mockError));

        when(future.get(any(long.class), any(TimeUnit.class))).thenThrow(new ExecutionException(toThrow));

        Runnable output = () -> {
            try {
                subject.compileExpression(value);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        };
        assertThat(output, throwsException(withMessage(equalTo("Compilation Failed: [SUCKY_CODE]"))));

        ArgumentCaptor<Callable> callCapture = ArgumentCaptor.forClass(Callable.class);
        verify(executor).submit(callCapture.capture());
        callCapture.getValue().call();
        verify(interpreter).evaluate(value);
    }

    @Test
    public void testHandleBadRequest(){

        subject.handle(mock(HttpMessageNotReadableException.class));

    }

    @Test
    public void testHandleCompileError(){

        CompileError payload = new CompileError(ErrorCode.INVALID_TYPE, mock(Node.class));
        assertThat(subject.handle(new CompilationException(asList(payload))), hasItem(is(payload)));

    }

    @Test
    public void testHandleRuntimeException(){

        assertThat(subject.handle(new bali.RuntimeException("test")), equalTo("test"));

    }
}