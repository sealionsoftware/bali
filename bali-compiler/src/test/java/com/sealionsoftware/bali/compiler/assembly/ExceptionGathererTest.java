package com.sealionsoftware.bali.compiler.assembly;

import org.junit.Test;

import java.util.Map;

import static com.sealionsoftware.Matchers.containsOneEntry;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class ExceptionGathererTest {

    private ExceptionGatherer subject = new ExceptionGatherer();

    @Test
    public void testGatherUncaughtException(){

        Thread mockThread = mock(Thread.class);
        Throwable mockException = mock(Throwable.class);
        mockThread.setName("Test Thread");

        subject.uncaughtException(mockThread, mockException);

        Map<String, Throwable> gathered = subject.getGatheredExceptions();
        assertThat(gathered, containsOneEntry(equalTo("Test Thread"), is(mockException)));
    }

}