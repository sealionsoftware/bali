package bali;

import bali.annotation.Operator;

public interface Group<T> extends Iterable<T> {

	Integer size();

	Logic isEmpty();

    @Operator("#")
	T get(Integer index);

	Group<T> head(Integer number);

	Group<T> tail(Integer number);


}
