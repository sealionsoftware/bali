package bali.net;

import bali.Boolean;
import bali.annotation.Name;

import java.util.Arrays;

import static bali.Primitive.convert;

/**
 * TODO
 * User: Richard
 * Date: 06/02/14
 */
public class IP4Address implements IPAddress {

	byte[] bytes;

	public IP4Address(byte[] bytes) {
		this.bytes = bytes;
	}

	public Boolean equalTo(@Name("operand") IPAddress operand) {
		if (operand instanceof IP4Address){
			IP4Address other = (IP4Address) operand;
			return convert(Arrays.equals(bytes, other.bytes));
		}
		return Boolean.FALSE;
	}

	public bali.Boolean notEqualTo(@Name("operand") IPAddress operand) {
		return equalTo(operand).not();
	}
}
