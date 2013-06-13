package bali;

import java.lang.*;

import static bali._.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 11/06/13
 */
public class TestRunner {

	public static void main(java.lang.String[] args){

		long one = java.lang.Long.MAX_VALUE;
		long two = java.lang.Long.MAX_VALUE;
		long three = one + two;

		System.out.println(three);

		Number baliOne = NUMBER_FACTORY.forLong(java.lang.Long.MAX_VALUE);
		Number baliTwo = NUMBER_FACTORY.forLong(java.lang.Long.MAX_VALUE);
		Number baliThree = baliOne.add(baliTwo);

		System.out.println(baliThree);


	}
}
