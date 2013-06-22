package example;

import bali.Number;

public class CalculatorObject implements Calculator {

	public Number afield;

	public Number calculate(Number argument) {
		return argument.add(_.NUMBER_CONSTANT);
	}

}
