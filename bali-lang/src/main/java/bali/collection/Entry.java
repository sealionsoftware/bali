package bali.collection;

import bali.*;
import bali.Boolean;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;

/**
 * User: Richard
 * Date: 14/08/13
 */
@MetaType(Kind.BEAN)
public class Entry<K extends Value<K>, V> implements Value<Entry<K, V>> {

	private K key;
	private V value;

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public Boolean equalTo(@Name("operand") Entry<K, V> operand) {
		return key.equalTo(operand.getKey());
	}

	public Boolean notEqualTo(@Name("operand") Entry<K, V> operand) {
		return key.notEqualTo(operand.getKey());
	}
}
