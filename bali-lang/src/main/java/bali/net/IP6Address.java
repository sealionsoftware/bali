package bali.net;

import bali.annotation.Name;

/**
 * TODO
 * User: Richard
 * Date: 06/02/14
 */
public class IP6Address implements IPAddress {

	byte[] bytes;

	public IP6Address(byte[] bytes) {
		this.bytes = bytes;
	}

	public bali.Boolean equalTo(@Name("operand") IPAddress operand) {
		return null;
	}

	public bali.Boolean notEqualTo(@Name("operand") IPAddress operand) {
		return equalTo(operand).not();
	}
}
