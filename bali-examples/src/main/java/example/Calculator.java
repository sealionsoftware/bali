package example;

import bali.Number;

import java.io.Serializable;

public interface Calculator extends Serializable {

	public Number calculate(Number argument);

}
