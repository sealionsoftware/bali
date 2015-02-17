package bali.compiler;

import org.antlr.v4.runtime.RuleContext;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * User: Richard
 * Date: 30 Nov
 */
public class TreeProperty<T> {

	private String name;
	private CompilationProgressMonitor monitor;

	private Map<RuleContext, T> nodeMap = new IdentityHashMap<>();

	public TreeProperty(String name, CompilationProgressMonitor monitor) {
		this.name = name;
		this.monitor = monitor;
	}

	public synchronized T get(RuleContext node){
		if (!nodeMap.containsKey(node)) try {
			monitor.registerBlockage(node, name);
			wait();
		} catch (InterruptedException e){
			throw new RuntimeException("Interrupted", e);
		}
		monitor.deregisterBlockage();
		return nodeMap.get(node);
	}

	public synchronized void set(RuleContext node, T value){
		nodeMap.put(node, value);
		notifyAll();
	}

}
