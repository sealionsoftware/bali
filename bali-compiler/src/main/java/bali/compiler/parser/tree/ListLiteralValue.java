package bali.compiler.parser.tree;

import bali.compiler.Array;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class ListLiteralValue extends Value {

	private List<Value> values = new ArrayList<>();

	public ListLiteralValue(Integer line, Integer character) {
		super(line, character);
	}

	public String getValueTypeName() {
		return bali.List.class.getName();
	}

	public void addValue(Value v) {
		values.add(v);
	}

	public List<Value> getValues() {
		return values;
	}

	public List<Node> getChildren() {
		return new ArrayList<Node>(values);
	}
}
