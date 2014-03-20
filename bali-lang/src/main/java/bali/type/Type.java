package bali.type;

import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.collection.Collection;

import java.util.Objects;

/**
 * User: Richard
 * Date: 18/03/14
 */
@MetaType(Kind.INTERFACE)
public interface Type {

	public String getClassName();
	public Collection<Type> getTypeArguments();
	public Collection<Declaration> getParameters();
	public Object createObject(Collection<?> arguments);

}
