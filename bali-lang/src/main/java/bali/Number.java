package bali;


/**
 * User: Richard
 * Date: 09/06/13
 */
public interface Number extends Quantified<Number> {

	public Boolean isPositive();

	public Boolean isNegative();

	public Boolean isZero();

	@Operator("|")
	public Number magnitude();

	@Operator("-")
	public Number negative();

	@Operator("+")
	public Number add(Number o);

	@Operator("-")
	public Number subtract(Number o);

	@Operator("*")
	public Number multiply(Number o);

	@Operator("/")
	public Number divide(Number o);

}
