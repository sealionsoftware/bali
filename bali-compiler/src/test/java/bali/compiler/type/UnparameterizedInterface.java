package bali.compiler.type;

import bali.String;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.annotation.Name;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 23/08/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface UnparameterizedInterface {

	public void aVoidMethod();

	public void aVoidMethodWithArgument(@Name("argument") bali.String argument);

	public String aStringMethod();

	@Operator("!")
	public void aVoidUnaryOperator();

	@Operator("£")
	public String aStringUnaryOperator();

	@Operator("$")
	public void aVoidOperator(String operand);

	@Operator("%")
	public String aStringOperator(String operand);


}
