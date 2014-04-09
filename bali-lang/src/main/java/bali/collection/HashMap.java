package bali.collection;

import bali.Boolean;
import bali.Integer;
import bali.Iterator;
import bali.Value;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Nullable;
import bali.annotation.Parameters;
import bali.type.Type;

import static bali.Primitive.convert;

/**
 * TODO - non delegated implementation that respects bali key equality nativly
 * User: Richard
 * Date: 05/03/14
 */
@MetaType(Kind.OBJECT)
public class HashMap<K extends Value<K>, V> implements Map<K,V> {

	private java.util.Map<Key<K>, V> delegate = new java.util.HashMap<>();

	public HashMap() {
	}

	@Parameters
	public HashMap(@Name("K") Type K, @Name("V") Type V, @Name("entries") @Nullable Collection<Entry<K,V>> entries) {
		if (entries != null){
			Iterator<Entry<K,V>> entryIterator = entries.iterator();
			while(convert(entryIterator.hasNext())){
				Entry<K,V> entry = entryIterator.next();
				delegate.put(new Key<>(entry.key), entry.value);
			}
		}
	}

	public V get(K key) {
		return delegate.get(new Key<>(key));
	}

	public void put(K key, V value) {
		delegate.put(new Key<>(key), value);
	}

	public Boolean contains(K key) {
		return convert(delegate.containsKey(new Key<>(key)));
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

		final java.util.Iterator<java.util.Map.Entry<Key<K>, V>> delegateIterator = this.delegate.entrySet().iterator();

		return new Iterator<Entry<K, V>>() {
			public Boolean hasNext() {
				return convert(delegateIterator.hasNext());
			}

			public Entry<K, V> next() {
				java.util.Map.Entry<Key<K>, V> delegateEntry = delegateIterator.next();
				return new Entry<>(
						delegateEntry.getKey().delegate,
						delegateEntry.getValue()
				);
			}
		};
	}

	private static class Key<K extends Value> {

		private K delegate;

		private Key(K delegate) {
			this.delegate = delegate;
		}

		public boolean equals(Object o) {
			if (o instanceof Key){
				return convert(delegate.equalTo(((Key<K>) o).delegate));
			}
			return false;
		}

		public int hashCode(){
			return 1;
		}

	}
}
