package bali.compiler.parser.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class MethodNode extends DeclarationNode {

	private String operator;
	private List<ArgumentDeclarationNode> argumentDeclarations = new ArrayList<>();

	public MethodNode() {
	}

	public MethodNode(Integer line, Integer character) {
		super(line, character);
	}

	public List<ArgumentDeclarationNode> getArguments() {
		return argumentDeclarations;
	}

	public void addArgument(ArgumentDeclarationNode argumentDeclaration) {
		this.argumentDeclarations.add(argumentDeclaration);
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Boolean getFinal() {
		return true;
	}

	public List<Node> getChildren() {
		List<Node> ret = super.getChildren();
		ret.addAll(argumentDeclarations);
		return ret;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass().isAssignableFrom(o.getClass())) return false;

		MethodNode that = (MethodNode) o;

		if (!argumentDeclarations.equals(that.argumentDeclarations)) return false;

		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<ArgumentDeclarationNode> i = argumentDeclarations.iterator();
		if (i.hasNext()){
			sb.append(i.next());
			while (i.hasNext()){
				sb.append(",").append(i.next());
			}
		}
		return getName() + "(" + sb + ")";
	}

}
