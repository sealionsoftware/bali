package bali.net;

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
		throw new java.lang.RuntimeException("Cannot convert IPAddress of type " + in.getClass());
	}

}
