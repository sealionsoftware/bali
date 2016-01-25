package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;
import com.sealionsoftware.bali.compiler.tree.Node;
import org.junit.Test;

import java.util.Collection;

import static com.sealionsoftware.bali.compiler.Matchers.blockageOnProperty;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


public class CompilationThreadManagerTest {

    private CompilationThreadManager subject = new CompilationThreadManager();

    @Test
    public void testRunMutuallyCommunicatingThreads() throws Exception {

        Node mockNode = mock(Node.class);
        MonitoredProperty<String> setByWorker1 = new MonitoredProperty<>(mockNode, "setByWorker1", subject);
        MonitoredProperty<Integer> setByWorker2 = new MonitoredProperty<>(mockNode, "setByWorker2", subject);

        AssemblyTask task1 = new AssemblyTask("", () -> {
            setByWorker2.get();
            setByWorker1.set("aValue");
        });
        AssemblyTask task2 = new AssemblyTask("", () -> {
            setByWorker2.set(1);
            setByWorker1.get();
        });
        subject.run(asList(task1, task2));

        assertThat(subject.getBlockages(), is(empty()));
    }

    @Test
    public void testRunMutuallyBlockingThreads() throws Exception {

        Node mockNode = mock(Node.class);
        MonitoredProperty<String> setByWorker1 = new MonitoredProperty<>(mockNode, "setByWorker1", subject);
        MonitoredProperty<Integer> setByWorker2 = new MonitoredProperty<>(mockNode, "setByWorker2", subject);

        AssemblyTask task1 = new AssemblyTask("", () -> {
            setByWorker2.get();
            setByWorker1.set("aValue");
        });
        AssemblyTask task2 = new AssemblyTask("", () -> {
            setByWorker1.get();
            setByWorker2.set(1);
        });
        subject.run(asList(task1, task2));

        Collection<BlockageDescription> blockages = subject.getBlockages();
        assertThat(blockages, hasSize(2));
        assertThat(blockages, containsInAnyOrder(asList(blockageOnProperty("setByWorker1"), blockageOnProperty("setByWorker2"))));
    }

    @Test(expected = RuntimeException.class)
    public void testUnhandledException() throws Exception {

        final RuntimeException e = new RuntimeException("No-one expects the Spanish inquisition!");
        subject.run(asList(new AssemblyTask("", () -> {throw e;})));
    }


}