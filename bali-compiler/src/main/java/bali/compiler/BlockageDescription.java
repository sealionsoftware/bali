package bali.compiler;

import org.antlr.v4.runtime.RuleContext;

/**
 * User: Richard
 * Date: 09 Dec
 */
public class BlockageDescription {

	public final String threadName;
	public final RuleContext node;
	public final String propertyName;

	public BlockageDescription(String threadName, RuleContext node, String propertyName) {
		this.threadName = threadName;
		this.node = node;
		this.propertyName = propertyName;
	}
}
