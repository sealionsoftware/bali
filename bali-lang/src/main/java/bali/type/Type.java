package bali.type;

import bali.Boolean;
import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.collection.Collection;

/**
 * User: Richard
 * Date: 18/03/14
 */
@MetaType(Kind.INTERFACE)
public interface Type {

	public String getClassName();
	public Collection<Type> getTypeArguments();
	public Collection<Declaration> getParameters();
	public Object createObject(@Name("arguments") Collection<?> arguments);
	public Boolean assignableTo(@Name("otherType") Type otherType);

}
