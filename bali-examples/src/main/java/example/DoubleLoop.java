package example;

import java.util.Arrays;

/**
 * User: Richard
 * Date: 17/11/13
 */
public class DoubleLoop  {

	public void execute(){

		int z = 0;
		top: for (int j : Arrays.asList(1,2,3)){
			for (int k : Arrays.asList(5,6,7)){
				z = j * k;
				if (z % 10 == 0){
					break top;
				}
			}
		}

		System.out.println(z);
	}

}
