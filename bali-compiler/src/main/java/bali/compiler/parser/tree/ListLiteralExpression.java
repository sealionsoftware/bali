package bali.compiler.parser.tree;

import bali.collection.Array;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ListLiteralExpression extends Expression {

	private List<Expression> values = new ArrayList<>();
	private TypeReference type;

	public ListLiteralExpression(Integer line, Integer character) {
		super(line, character);
	}

	public TypeReference getType() {
		return type;
	}

	public void addValue(Expression v) {
		values.add(v);
		if (type == null){
			TypeReference newType = new TypeReference();
			newType.setClassName(Array.class.getName());
			newType.addParameter(v.getType());
			newType.setErase(true);
			type = newType;
		}
	}

	public List<Expression> getValues() {
		return values;
	}

	public List<Node> getChildren() {
		List<Node> ret = new ArrayList<>();
		if(type != null){
			ret.add(type);
		}
		ret.addAll(values);
		return ret;
	}
}
