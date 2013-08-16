package bali.collection;

import bali.Value;
import bali.Boolean;

/**
 * User: Richard
 * Date: 14/08/13
 */
public interface Map<K extends Value<K>, V> extends Collection<Entry<K, V>> {

	public V get(K key);

	public void set(K key, V value);

	public Boolean contains(K key);

}
