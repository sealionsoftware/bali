package bali.collection;

import bali.Boolean;
import bali.Value;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;

/**
 * User: Richard
 * Date: 08/05/13
 */
@MetaType(Kind.INTERFACE)
public interface ValueCollection<E extends Value<E>> extends Collection<E>, Value<ValueCollection<E>> {

	public Boolean contains(@Name("value") E value);

}
