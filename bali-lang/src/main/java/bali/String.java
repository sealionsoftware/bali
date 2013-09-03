package bali;

import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 15/07/13
 */
public interface String extends Value<String> {

	public Iterable<String> getCharacters();

	public Number length();

	@Operator("+")
	public String join(String operand);

}
