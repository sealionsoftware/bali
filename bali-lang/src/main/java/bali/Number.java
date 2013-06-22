package bali;


/**
 * User: Richard
 * Date: 09/06/13
 */
public interface Number extends Value<Number> {

	public Boolean isPositive();

	public Boolean isNegative();

	public Boolean isZero();

	public Number magnitude();

	public Number negative();

	public Boolean greaterThan(Number o);

	public Boolean lessThan(Number o);

	public Number add(Number o);

	public Number subtract(Number o);

	public Number multiply(Number o);

	public Number divide(Number o);

}
