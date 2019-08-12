package com.sealionsoftware.bali.compiler.server;

import bali.Writer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sealionsoftware.bali.compiler.Interpreter;
import com.sealionsoftware.bali.compiler.TextBuffer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.task.AsyncTaskExecutor;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BaliInterpreterServerTest {

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private BaliInterpreterServer subject;

    @Test
    public void testInitialise() {
        subject.initialise();
        verify(mapper).registerModule(any(Module.class));
    }

    @Test
    public void testExecutor() {
        assertThat(subject.executor(), instanceOf(AsyncTaskExecutor.class));
    }

    @Test
    public void testConsole() {
        assertThat(subject.console(), instanceOf(TextBuffer.class));
    }

    @Test
    public void testInterpreter() {
        assertThat(subject.interpreter(mock(Writer.class)), instanceOf(Interpreter.class));
    }

    @Test
    public void testMain() {
        BaliInterpreterServer.main();
    }
}