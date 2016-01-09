package com.sealionsoftware.bali.compiler.reference;

import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.assembly.ExceptionGatherer;
import com.sealionsoftware.bali.compiler.tree.Node;
import org.junit.Test;

import static com.sealionsoftware.bali.compiler.Matchers.hasNoExceptions;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class MonitoredPropertyTest {

    private ExceptionGatherer gatherer = new ExceptionGatherer(mock(CompilationThreadManager.class));
    private MonitoredProperty<String> subject = new MonitoredProperty<>(mock(Node.class), "aProperty", mock(CompilationThreadManager.class));

    @Test
    public void testUnsetPropertyBlocks() throws Exception {
        Thread t = runOnNewThread(() -> {
            subject.get();
            fail();
        });
        while (!t.getState().equals(Thread.State.WAITING)){
            sleep(100);
        }
        t.interrupt();
        t.join();
        assertThat(gatherer, hasNoExceptions());
    }

    @Test
    public void testSetPropertyDoesNotBlock() throws Exception {
        subject.set("aValue");
        Thread t = runOnNewThread(() -> {
            subject.get();
        });
        t.join();
        assertThat(gatherer, hasNoExceptions());
    }

    private Thread runOnNewThread(Runnable task){
        Thread t = new Thread(task);
        t.setUncaughtExceptionHandler(gatherer);
        t.start();
        return t;
    }

}