package bali;

import bali.annotation.Operator;

public interface Collection<T> extends Iterable<T> {

	Integer size();

	Boolean isEmpty();

    @Operator("#")
	T get(Integer index);

}
