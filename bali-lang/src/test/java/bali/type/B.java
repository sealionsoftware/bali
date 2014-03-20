package bali.type;

import bali.annotation.Name;
import bali.annotation.Parameters;

/**
 * User: Richard
 * Date: 19/03/14
 */
public class B<T> {

	@Parameters
	public B(@Name("T") T T){
	}
}
