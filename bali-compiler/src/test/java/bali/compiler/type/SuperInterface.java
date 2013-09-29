package bali.compiler.type;

import bali.String;
import bali.annotation.Operator;
/**
 * User: Richard
 * Date: 28/09/13
 */
public interface SuperInterface {

	public void aMethod();

	@Operator("#")
	public void anUnaryOperator();

	@Operator("~")
	public void anOperator(String operand);

}
