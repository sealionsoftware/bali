package bali.collection;

import bali.Boolean;
import bali.Integer;
import bali.Iterable;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Nullable;
import bali.annotation.Operator;
import bali.annotation.SelfTyped;

/**
 * User: Richard
 * Date: 16/08/13
 */
@MetaType(Kind.INTERFACE)
public interface Collection<T> extends Iterable<T> {

	public Integer size();

	public Boolean isEmpty();

	@Operator("#")
	@Nullable
	public T get(Integer index);

	@Operator("+")
	@SelfTyped
	public Collection<T> join(Collection<T> operand);

	@SelfTyped
	public Collection<T> head(Integer index);

	@SelfTyped
	public Collection<T> tail(Integer index);

}
