package bali.type;

import bali.annotation.Name;
import bali.annotation.Parameters;

/**
 * User: Richard
 * Date: 19/03/14
 */
public class C<X,Y> {

	@Parameters
	public C(@Name("X") X X, @Name("Y") Y Y){
	}
}
