package bali.net;

import bali.Serializer;
import bali.String;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 01 Apr
 */
public class IPAddressSerializer implements Serializer<IPAddress> {

	private static final Pattern IPV4_PATTERN = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");

	public IPAddress parse(String in) {
		Matcher ipV4Matcher = IPV4_PATTERN.matcher(convert(in));
		if (ipV4Matcher.matches()){

			byte[] bytes = new byte[4];
			for (int i = 0; i < bytes.length ; i++){
				int number = Byte.MIN_VALUE + (Integer.parseInt(ipV4Matcher.group(i + 1)));
				if (number > Byte.MAX_VALUE){
					throw new RuntimeException("Invalid IP Address");
				}
				bytes[i] = (byte) number;
			}
			return new IP4Address(bytes);
		}
		throw new java.lang.RuntimeException("Cannot parse IPAddress " + in);
	}

	public String format(IPAddress in) {
		if (in instanceof IP4Address){
			IP4Address address = (IP4Address) in;
			byte[] bytes = address.bytes;
			StringBuilder sb = new StringBuilder();
			sb.append(bytes[0] + 128);
			for(int i = 1 ; i < bytes.length ; i++){
				sb.append('.');
				sb.append(bytes[i] + 128);
			}
			return convert(sb.toString());
		}
		throw new RuntimeException("Cannot format IPAddress " + in);
	}
}
