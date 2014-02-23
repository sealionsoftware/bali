package bali.collection;

import bali.Boolean;
import bali.Value;
import bali.annotation.MetaType;
import bali.annotation.Kind;

/**
 * User: Richard
 * Date: 14/08/13
 */
@MetaType(Kind.INTERFACE)
public interface Map<K extends Value<K>, V> extends Collection<Entry<K, V>> {

	public V get(K key);

	public void set(K key, V value);

	public Boolean contains(K key);

}
