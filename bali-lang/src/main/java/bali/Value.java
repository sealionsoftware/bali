package bali;

import bali.annotation.Operator;

public interface Value<T extends Value> {

    @Operator("==")
    Logic equalTo(T operand);

    @Operator("!=")
    Logic notEqualTo(T operand);

}
