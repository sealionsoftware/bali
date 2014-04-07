package bali.net;

import bali.Serializer;
import org.junit.Assert;
import org.junit.Test;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 01 Apr
 */
public class IPAddressSerializerUnitTest {

	private static final byte MIN = Byte.MIN_VALUE;
	private static final byte MAX = Byte.MAX_VALUE;

	private static final java.lang.String IPV4_ADDRESS_STRING_1 = "0.0.0.0";
	private static final java.lang.String IPV4_ADDRESS_STRING_2 = "127.0.0.1";
	private static final java.lang.String IPV4_ADDRESS_STRING_3 = "255.255.255.255";

	private static final IPAddress IPV4_ADDRESS_1 = new IP4Address(new byte[]{MIN,MIN,MIN,MIN});
	private static final IPAddress IPV4_ADDRESS_2 = new IP4Address(new byte[]{MIN + 127,MIN,MIN,MIN+1});
	private static final IPAddress IPV4_ADDRESS_3 = new IP4Address(new byte[]{MAX,MAX,MAX,MAX});

	private static final Serializer<IPAddress> SERIALIZER = new IPAddressSerializer();

	@Test
	public void testFormatV4Address(){
		Assert.assertEquals(IPV4_ADDRESS_STRING_1, convert(SERIALIZER.format(IPV4_ADDRESS_1)));
		Assert.assertEquals(IPV4_ADDRESS_STRING_2, convert(SERIALIZER.format(IPV4_ADDRESS_2)));
		Assert.assertEquals(IPV4_ADDRESS_STRING_3, convert(SERIALIZER.format(IPV4_ADDRESS_3)));
	}

	@Test
	public void testParseV4Address(){
		Assert.assertTrue(convert(IPV4_ADDRESS_1.equalTo(SERIALIZER.parse(convert(IPV4_ADDRESS_STRING_1)))));
		Assert.assertTrue(convert(IPV4_ADDRESS_2.equalTo(SERIALIZER.parse(convert(IPV4_ADDRESS_STRING_2)))));
		Assert.assertTrue(convert(IPV4_ADDRESS_3.equalTo(SERIALIZER.parse(convert(IPV4_ADDRESS_STRING_3)))));
	}

}
