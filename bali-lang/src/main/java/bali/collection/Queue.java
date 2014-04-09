package bali.collection;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Nullable;

/**
 * User: Richard
 * Date: 14/08/13
 */
@MetaType(Kind.INTERFACE)
public interface Queue<T> extends Collection<T> {

	public void push(T object);

	@Nullable
	public T pop();

}
