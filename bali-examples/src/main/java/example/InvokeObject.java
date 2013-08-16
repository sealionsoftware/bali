package example;

import bali.Number;
import bali.number.Int;

public class InvokeObject {

	public void invokeConstantVoid() {
		return;
	}

	public Number returnValue() {
		Number i = new Int(1);
		return getNumber(i);
	}

	public Number getNumber(Number number) {
		return number;
	}

}
