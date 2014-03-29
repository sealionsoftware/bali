package bali.collection;

import bali.Boolean;
import bali.Value;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;

/**
 * User: Richard
 * Date: 14/08/13
 */
@MetaType(Kind.INTERFACE)
public interface Map<K extends Value<K>, V> extends Collection<Entry<K, V>> {

	public V get(@Name("key") K key);

	public void put(@Name("key")K key, @Name("value") V value);

	public Boolean contains(@Name("key")K key);

}
