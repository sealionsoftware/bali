package example;

import bali.Number;

import static bali._.NUMBER_FACTORY;

public class NumberObject {

	public void goDo() {
		Number one = NUMBER_FACTORY.forDecimalString("1".toCharArray());
	}

}
