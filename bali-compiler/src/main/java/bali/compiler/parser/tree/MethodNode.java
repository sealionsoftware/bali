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
	private List<ParameterNode> parameters = new ArrayList<>();

	public MethodNode() {
	}

	public MethodNode(Integer line, Integer character) {
		super(line, character);
	}

	public List<ParameterNode> getParameters() {
		return parameters;
	}

	public void addParameter(ParameterNode argumentDeclaration) {
		children.add(argumentDeclaration);
		this.parameters.add(argumentDeclaration);
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

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass().isAssignableFrom(o.getClass())) return false;

		MethodNode that = (MethodNode) o;

		return parameters.equals(that.parameters);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<ParameterNode> i = parameters.iterator();
		if (i.hasNext()){
			sb.append(i.next());
			while (i.hasNext()){
				sb.append(",").append(i.next());
			}
		}
		return getName() + "(" + sb + ")";
	}

}
