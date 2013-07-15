package bali;

import com.sealionsoftware.bali.collections.Array;

/**
 * User: Richard
 * Date: 15/07/13
 */
public interface String extends Value<String> {


	Iterable<String> getCharacters();

	Number length();

	@Operator("+")
	String join(String operand);

	Boolean equalTo(String operand);
}
