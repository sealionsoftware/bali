package bali;

import bali.annotation.Name;
import bali.type.TypeFactory;


/**
 * User: Richard
 * Date: 01 Apr
 */
public class ValueFormatter implements Formatter<Value<?>> {

	private SerializerRepository serializers = new DefaultSerializerRepository();

	public String format(@Name("in") Value<?> in) {

		if (in instanceof String){
			return (String) in;
		}

		Formatter<? super Value<?>> formatter = serializers.getSerializer(TypeFactory.getType(in.getClass().getName()));
		if (formatter == null){
			throw new BaliThrowable("Cannot format Value of type " + in.getClass().getName());
		}

		return formatter.format(in);
	}

}
