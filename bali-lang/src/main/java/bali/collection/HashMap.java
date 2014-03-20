package bali.collection;

import bali.Boolean;
import bali.Integer;
import bali.Iterator;
import bali.Value;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Parameters;
import bali.type.Type;

import static bali.Primitive.convert;

/**
 * TODO - non delegated implementation that respects bali key equality
 * User: Richard
 * Date: 05/03/14
 */
@MetaType(Kind.OBJECT)
public class HashMap<K extends Value<K>, V> implements Map<K,V> {

	private java.util.Map<K, V> delegate = new java.util.HashMap<>();

	public HashMap() {
	}

	@Parameters
	public HashMap(Type K, Type V) {
	}

	public V get(K key) {
		return delegate.get(key);
	}

	public void put(K key, V value) {
		delegate.put(key, value);
	}

	public Boolean contains(K key) {
		return convert(delegate.containsKey(key));
	}

	public Integer size() {
		return convert(delegate.size());
	}

	public Boolean isEmpty() {
		return convert(delegate.isEmpty());
	}

	public Entry<K, V> get(Integer index) {
		throw new RuntimeException("Not Implemented Yet");
	}

	public Collection<Entry<K, V>> join(Collection<Entry<K, V>> operand) {
		throw new RuntimeException("Not Implemented Yet");
	}

	public Collection<Entry<K, V>> head(Integer index) {
		throw new RuntimeException("Not Implemented Yet");
	}

	public Collection<Entry<K, V>> tail(Integer index) {
		throw new RuntimeException("Not Implemented Yet");
	}

	public Iterator<Entry<K, V>> iterator() {

		final java.util.Iterator<java.util.Map.Entry<K, V>> delegateIterator = this.delegate.entrySet().iterator();

		return new Iterator<Entry<K, V>>() {
			public Boolean hasNext() {
				return convert(delegateIterator.hasNext());
			}

			public Entry<K, V> next() {
				java.util.Map.Entry<K, V> delegateEntry = delegateIterator.next();
				return new Entry<>(
						null,
						null,
						delegateEntry.getKey(),
						delegateEntry.getValue()
				);
			}
		};
	}
}
