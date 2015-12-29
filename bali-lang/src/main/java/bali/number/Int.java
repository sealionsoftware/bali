package bali.number;

import bali.Boolean;
import bali.Integer;
import bali.Number;

import static bali.logic.Primitive.convert;
import static java.lang.Math.addExact;
import static java.lang.Math.decrementExact;
import static java.lang.Math.incrementExact;
import static java.lang.Math.multiplyExact;
import static java.lang.Math.negateExact;
import static java.lang.Math.subtractExact;

public class Int implements Integer {

    final int value;

    public Int(int value) {
        this.value = value;
    }

    public Boolean isPositive() {
        return convert(value > 0);
    }

    public Boolean isNegative() {
        return convert(value < 0);
    }

    public Boolean isZero() {
        return convert(value == 0);
    }

    public Number magnitude() {
        return value > 0 ? this : negate();
    }

    public Number negate() {
        return new Int(negateExact(value));
    }

    public Number add(Number operand) {
        if (operand instanceof Int){
            return new Int(addExact(value, ((Int) operand).value));
        }
        throw new NotImplementedException();
    }

    public Number subtract(Number operand) {
        if (operand instanceof Int){
            return new Int(subtractExact(value, ((Int) operand).value));
        }
        throw new NotImplementedException();
    }

    public Number multiply(Number operand) {
        if (operand instanceof Int){
            return new Int(multiplyExact(value, ((Int) operand).value));
        }
        throw new NotImplementedException();
    }

    public Number divide(Number operand) {
        if (operand instanceof Int){
            return new Int((value / ((Int) operand).value));
        }
        throw new NotImplementedException();
    }

    public Boolean greaterThan(Number operand) {
        if (operand instanceof Int){
            return convert(((Int) operand).value > value);
        }
        throw new NotImplementedException();
    }

    public Boolean lessThan(Number operand) {
        if (operand instanceof Int){
            return convert(((Int) operand).value < value);
        }
        throw new NotImplementedException();
    }

    public Boolean equalTo(Number operand) {
        if (operand instanceof Int){
            return convert(((Int) operand).value == value);
        }
        throw new NotImplementedException();
    }

    public Boolean notEqualTo(Number operand) {
        if (operand instanceof Int){
            return convert(((Int) operand).value != value);
        }
        throw new NotImplementedException();
    }

    public Integer increment() {
        return new Int(incrementExact(value));
    }

    public Integer decrement() {
        return new Int(decrementExact(value));
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Int)) return false;

        Int anInt = (Int) o;

        return value == anInt.value;

    }

    public int hashCode() {
        return value;
    }
}
