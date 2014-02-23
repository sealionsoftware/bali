package bali;

import bali.annotation.MetaType;
import bali.annotation.Kind;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 15/07/13
 */
@MetaType(Kind.INTERFACE)
public interface String extends Value<String> {

	public Iterable<String> getCharacters();

	public Integer length();

	@Operator("+")
	public String join(String operand);

	@Operator("^")
	public String uppercase();

}
