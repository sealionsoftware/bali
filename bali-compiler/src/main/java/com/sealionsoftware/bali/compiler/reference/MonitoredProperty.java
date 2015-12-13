package com.sealionsoftware.bali.compiler.reference;

import com.sealionsoftware.bali.compiler.assembly.CompilationThreadManager;
import com.sealionsoftware.bali.compiler.tree.Node;

/**
 * User: Richard
 * Date: 06/10/13
 */
public class MonitoredProperty<T> implements Reference<T> {

	private volatile boolean isSet = false;
    private T referenced;
    private CompilationThreadManager monitor;
    private Node node;
    private String property;

    public MonitoredProperty(Node node, String property, CompilationThreadManager monitor) {
        this.monitor = monitor;
        this.node = node;
        this.property = property;
    }

    public synchronized T get() {
		if (!isSet){
            monitor.registerBlockage(this);
            try {
                wait();
            } catch (InterruptedException ignored) {
                throw new RuntimeException(ignored);
            }
        }
		return referenced;
	}

	public synchronized void set(T referenced) {
		this.referenced = referenced;
		isSet = true;
        monitor.deregisterBlockage(this);
        notifyAll();
	}

    public Node getNode() {
        return node;
    }

    public String getProperty() {
        return property;
    }
}
