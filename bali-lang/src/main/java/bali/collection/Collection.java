package bali.collection;

import bali.Boolean;
import bali.Integer;
import bali.Iterable;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 16/08/13
 */
@MetaType(Kind.INTERFACE)
public interface Collection<T> extends Iterable<T> {

	public Integer size();

	public Boolean isEmpty();

	@Operator("#")
	public T get(Integer index);

	@Operator("+")
	public Collection<T> join(Collection<T> operand);

	public Collection<T> head(Integer index);

	public Collection<T> tail(Integer index);

}
