package bali;

import bali.annotation.Operator;

public interface List<T> extends Group<T> {

	Group<T> remove(Integer index);

	Group<T> insert(Integer index, T item);

	Group<T> replace(Integer index, T item);

    @Operator("<<")
	Group<T> add(T item);

	@Operator("<<")
	Group<T> join(Group<T> operand);

}
