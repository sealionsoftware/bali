package bali;

import bali.annotation.Operator;

public interface Reader {

	@Operator("<")
	Character read();

	@Operator("<<")
	Text readLine();

}
