package bali;

import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 07/07/13
 */
public interface Reader {

	@Operator("<<")
	public String readLine();

}
