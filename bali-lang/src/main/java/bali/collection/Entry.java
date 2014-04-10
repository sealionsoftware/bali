package bali.collection;

import bali.Value;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Parameters;
import bali.type.Type;

/**
 * User: Richard
 * Date: 14/08/13
 */
@MetaType(Kind.BEAN)
public class Entry<K extends Value<K>, V> {

	public K key;
	public V value;

	public Entry(K key, V value) {
		this(null, null, key, value);
	}

	@Parameters
	public Entry(Type K, Type V, @Name("key") K key, @Name("value") V value) {
		this.key = key;
		this.value = value;
	}

}
