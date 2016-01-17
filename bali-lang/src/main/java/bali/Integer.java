package bali;

import bali.annotation.Operator;

public interface Integer extends Number {

    @Operator("++")
    Integer increment();

    @Operator("--")
    Integer decrement();

}
