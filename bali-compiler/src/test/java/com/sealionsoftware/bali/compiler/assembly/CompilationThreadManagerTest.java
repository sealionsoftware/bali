package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.reference.MonitoredProperty;
import com.sealionsoftware.bali.compiler.tree.Node;
import org.junit.Test;

import java.util.Collection;

import static com.sealionsoftware.Matchers.containsValuesMatching;
import static com.sealionsoftware.Matchers.isEmpty;
import static com.sealionsoftware.bali.compiler.Matchers.blockageOnProperty;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


public class CompilationThreadManagerTest {

    private CompilationThreadManager subject = new CompilationThreadManager();

    @Test
    public void testRunMutuallyCommunicatingThreads() throws Exception {

        Node mockNode = mock(Node.class);
        MonitoredProperty<String> setByWorker1 = new MonitoredProperty<>(mockNode, "setByWorker1", subject);
        MonitoredProperty<Integer> setByWorker2 = new MonitoredProperty<>(mockNode, "setByWorker2", subject);

        NamedRunnable task1 = runWithMonitor(() -> {
            setByWorker2.get();
            setByWorker1.set("aValue");
        });
        NamedRunnable task2 = runWithMonitor(() -> {
            setByWorker2.set(1);
            setByWorker1.get();
        });
        subject.run(asList(task1, task2));

        assertThat(subject.getBlockages(), isEmpty());
    }

    @Test
    public void testRunMutuallyBlockingThreads() throws Exception {

        Node mockNode = mock(Node.class);
        MonitoredProperty<String> setByWorker1 = new MonitoredProperty<>(mockNode, "setByWorker1", subject);
        MonitoredProperty<Integer> setByWorker2 = new MonitoredProperty<>(mockNode, "setByWorker2", subject);

        NamedRunnable task1 = runWithMonitor(() -> {
            setByWorker2.get();
            setByWorker1.set("aValue");
        });
        NamedRunnable task2 = runWithMonitor(() -> {
            setByWorker1.get();
            setByWorker2.set(1);
        });
        subject.run(asList(task1, task2));

        Collection<BlockageDescription> blockages = subject.getBlockages();
        assertThat(blockages, hasSize(2));
        assertThat(blockages, containsValuesMatching(blockageOnProperty("setByWorker1"), blockageOnProperty("setByWorker2")));
    }

    @Test(expected = RuntimeException.class)
    public void testUnhandledException() throws Exception {

        final RuntimeException e = new RuntimeException("No-one expects the Spanish inquisition!");
        NamedRunnable workerThread1 = runWithMonitor(() -> {
            throw e;
        });
        subject.run(asList(workerThread1));
    }

    private NamedRunnable runWithMonitor(Runnable task){
        return new NamedRunnable() {
            public String getName() {
                return "";
            }

            public void run() {
                task.run();
                subject.deregisterThread();
            }
        };
    }

}