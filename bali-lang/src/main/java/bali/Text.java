package bali;

import bali.annotation.Operator;

public interface Text extends ValueGroup<Character>, Value<Text> {

	Text uppercase();

    @Operator("+")
    Text concatenate(Text operand);

}
