package com.sealionsoftware.bali.compiler.assembly;

import com.sealionsoftware.bali.compiler.tree.Node;

public class BlockageDescription {

	public final String threadName;
	public final Node node;
	public final String propertyName;

	public BlockageDescription(String threadName, Node node, String propertyName) {
		this.threadName = threadName;
		this.node = node;
		this.propertyName = propertyName;
	}

    public String toString() {
        return "BlockageDescription{" +
                "threadName='" + threadName + '\'' +
                ", node=" + node +
                ", propertyName='" + propertyName + '\'' +
                '}';
    }
}
