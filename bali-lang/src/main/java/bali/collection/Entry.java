package bali.collection;

import bali.Value;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 14/08/13
 */
@MetaType(Kind.INTERFACE)
public interface Entry<K extends Value<K>, V> {

	public K getKey();

	public V getValue();

}
