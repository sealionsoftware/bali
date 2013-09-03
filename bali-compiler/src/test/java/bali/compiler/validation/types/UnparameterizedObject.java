package bali.compiler.validation.types;

import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 23/08/13
 */
public class UnparameterizedObject {

	public void aVoidMethod(){
	}

	public void aVoidMethodWithArgument(Integer argument){
	}

	public String aStringMethod(){
		return "";
	}

	@Operator("^&*")
	public void anOperatorMethod(){
	}

}
