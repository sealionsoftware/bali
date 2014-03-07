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
@MetaType(Kind.BEAN)
public class Entry<K extends Value<K>, V> implements Value<Entry<K, V>> {

	public K key;
	public V value;

	public Boolean equalTo(@Name("operand") Entry<K, V> operand) {
		return key.equalTo(operand.key);
	}

	public Boolean notEqualTo(@Name("operand") Entry<K, V> operand) {
		return key.notEqualTo(operand.key);
	}
}
