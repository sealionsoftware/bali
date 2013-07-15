package example;

import bali.Number;
import com.sealionsoftware.bali.number.Integer;

public class InvokeObject {

	public void invokeConstantVoid() {
		return;
	}

	public Number returnValue() {
		Number i = new Integer(1);
		return getNumber(i);
	}

	public Number getNumber(Number number){
		return number;
	}

}
