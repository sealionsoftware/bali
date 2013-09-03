package bali;

import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 07/07/13
 */
public interface Writer {

	@Operator("<<")
	public void writeLine(String in);

}
