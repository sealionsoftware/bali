package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.tree.Node;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;

public class BlockageDescriptionTest {

    private static final String THREAD_NAME = "THREAD1";
    private static final Node BLOCKED_NODE = mock(Node.class);
    private static final String PROPERTY_NAME = "aProperty";

    private BlockageDescription subject = new BlockageDescription(THREAD_NAME, BLOCKED_NODE, PROPERTY_NAME);

    @Test
    public void testToString() throws Exception {
        String string = subject.toString();

        assertThat(string, containsString(THREAD_NAME));
        assertThat(string, containsString(PROPERTY_NAME));
    }
}