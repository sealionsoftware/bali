package bali.compiler.example;

import bali.Number;

public class ConditionalObject {

	private boolean flag = true;

	public int doConditionally() {
		int i = 0 ;
		if (flag){
			i = 1;
		}
		if (!flag){
			i = 2;
		}
		return i;
	}

}
