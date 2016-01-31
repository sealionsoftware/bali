package bali;

import bali.annotation.Operator;

public interface Collection<T> extends Iterable<T> {

	Integer size();

	Logic isEmpty();

    @Operator("#")
	T get(Integer index);

}
