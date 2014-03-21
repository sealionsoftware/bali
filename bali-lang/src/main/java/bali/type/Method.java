package bali.type;

import bali.Boolean;
import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Parameters;
import bali.collection.Collection;

/**
 * User: Richard
 * Date: 18/03/14
 */
@MetaType(Kind.BEAN)
public class Method extends Declaration {

	public Collection<Declaration> arguments;

	@Parameters
	public Method(String name, Type type, Boolean nullable, Collection<Declaration> arguments) {
		super(name, type, nullable);
		this.arguments = arguments;
	}
}
