package bali;

import bali.annotation.Operator;

public interface Value<T extends Value> {

    @Operator("==")
	Boolean equalTo(T operand);

    @Operator("!=")
	Boolean notEqualTo(T operand);

}
