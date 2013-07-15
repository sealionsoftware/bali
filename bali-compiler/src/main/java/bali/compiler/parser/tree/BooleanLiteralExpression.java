package bali.compiler.parser.tree;

import com.sealionsoftware.bali.IdentityBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class BooleanLiteralExpression extends Expression {

	private String serialization;
	private Type type; {
		Type t = new Type();
		t.setClassName(IdentityBoolean.class.getName());
		type = t;
	}

	public BooleanLiteralExpression(Integer line, Integer character) {
		super(line, character);

	}

	public Type getType() {
		return type;
	}

	public void setSerialization(String serialization) {
		this.serialization = serialization;
	}

	public String getSerialization() {
		return serialization;
	}

	public List<Node> getChildren() {
		List<Node> ret = new ArrayList<>();
		ret.add(type);
		return ret;
	}
}
