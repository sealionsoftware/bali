package example;

import com.sealionsoftware.bali.number.Byte;
import bali.Number;

public class ReturnObject {

	public void returnVoid() {
		return;
	}

	public Number returnValue() {
		return new Byte((byte) 0);
	}

}