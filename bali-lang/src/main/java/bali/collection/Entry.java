package bali.collection;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 14/08/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Entry<K,V> {

	public K getKey();

	public V getValue();

}
