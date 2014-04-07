package bali.number;

import bali.Integer;
import bali.Number;
import bali.Serializer;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 19/03/14
 */
public class _ {

	public static final Integer ZERO = convert(0);
	public static final Integer ONE = convert(1);

	//TODO: e, pi

	public static final Serializer<Number> NUMBER_SERIALIZER = new NumberSerializer();

}
