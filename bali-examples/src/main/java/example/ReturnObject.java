package example;

import bali.Number;
import bali.number.Byte;

public class ReturnObject {

	public void returnVoid() {
		return;
	}

	public Number returnValue() {
		return new Byte((byte) 0);
	}

}
