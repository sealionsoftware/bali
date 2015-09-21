package bali;

public interface Value<T extends Value> {

	Boolean equalTo(T operand);

	Boolean notEqualTo(T operand);

}
