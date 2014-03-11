package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;
import bali.compiler.type.Site;

import java.util.Iterator;

/**
 * User: Richard
 * Date: 05/05/13
 */
public class ConstructionExpressionNode extends ParametrisedExpressionNode {

	private String className;
	private BlockingReference<Site> type = new BlockingReference<>();

	public ConstructionExpressionNode(Integer line, Integer character) {
		super(line, character);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setType(Site type) {
		this.type.set(type);
	}

	public Site getType() {
		return type.get();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("new ");
		sb.append(className).append("(");
		Iterator<ArgumentNode> i = getArguments().iterator();
		if (i.hasNext()){
			sb.append(i.next());
		}
		while(i.hasNext()){
			sb.append(", ").append(i.next());
		}
		return sb.append(")").toString();
	}
}
