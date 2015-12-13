package com.sealionsoftware.bali.compiler.assembly;

import org.junit.Test;

import static com.sealionsoftware.bali.compiler.assembly.Interruptable.wrapException;

public class InterruptableTest {

    @Test(expected = RuntimeException.class)
    public void testWrapException() throws Exception {
        wrapException(()->{
            throw new InterruptedException();
        }, "Task was interrupted");
    }
}