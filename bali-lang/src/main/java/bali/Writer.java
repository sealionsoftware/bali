package bali;


import bali.annotation.Operator;

public interface Writer {

	@Operator("<")
	void write(Character in);

	@Operator("<<")
	void writeLine(Text in);

}
