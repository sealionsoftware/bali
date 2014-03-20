package bali.type;

import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Parameters;

/**
 * User: Richard
 * Date: 18/03/14
 */
@MetaType(Kind.BEAN)
public class Declaration {

	public String name;
	public Type type;

	@Parameters
	public Declaration(String name, Type type) {
		this.name = name;
		this.type = type;
	}
}
