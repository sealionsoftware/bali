package bali.compiler.parser.tree;

import bali.Array;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ListLiteralExpression extends Expression {

	private List<Expression> values = new ArrayList<>();
	private Type type;

	public ListLiteralExpression(Integer line, Integer character) {
		super(line, character);
	}

	public Type getType() {
		return type;
	}

	public void addValue(Expression v) {
		values.add(v);
	}

	public List<Expression> getValues() {
		return values;
	}

	public void setListType(Type listType) {
		Type newType = new Type();
		newType.setQualifiedClassName(Array.class.getName());
		newType.addParameter(listType);
		type = newType;
	}

	public List<Node> getChildren() {
		return new ArrayList<Node>(values);
	}
}
