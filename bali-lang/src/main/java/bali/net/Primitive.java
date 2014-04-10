package bali.net;

import bali.BaliThrowable;

/**
 * User: Richard
 * Date: 31 Mar
 */
public class Primitive {

	public static byte[] convert(IPAddress in) {
		if(in instanceof IP4Address){
			return ((IP4Address) in).bytes;
		} else if (in instanceof IP6Address) {
			return ((IP6Address) in).bytes;
		}
		throw new BaliThrowable("Cannot convert IPAddress of type " + in.getClass());
	}

}
