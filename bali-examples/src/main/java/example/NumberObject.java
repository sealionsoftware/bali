package example;

import bali.Number;

import static bali.number.NumberFactory.NUMBER_FACTORY;

public class NumberObject {

	public void goDo() {
		Number one = NUMBER_FACTORY.forDecimalString("1".toCharArray());
	}

}
