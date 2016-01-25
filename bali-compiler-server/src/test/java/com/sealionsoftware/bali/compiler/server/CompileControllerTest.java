package com.sealionsoftware.bali.compiler.server;

import com.sealionsoftware.bali.compiler.Interpreter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.task.AsyncTaskExecutor;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.sealionsoftware.Constant.map;
import static com.sealionsoftware.Constant.put;
import static com.sealionsoftware.Matchers.throwsException;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
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
        Future<Map<String, Object>> future = Mockito.mock(Future.class);
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
    public void testCompileFragmentTimeout() throws Exception {

        @SuppressWarnings("unchecked")
        Future<Map<String, Object>> future = Mockito.mock(Future.class);

        when(executor.submit(Mockito.<Callable<Map<String, Object>>>any())).thenReturn(future);
        when(future.get(any(long.class), any(TimeUnit.class))).thenThrow(new TimeoutException());

        Callable invocation = () -> subject.compileFragment("var Text greeting = \"Hello World\"");
        assertThat(invocation, throwsException(instanceOf(TimeoutException.class)));
        verify(future).cancel(true);
    }
}