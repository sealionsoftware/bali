package example;

/**
 * User: Richard
 * Date: 10/11/13
 */
public class Monitor {

	private int i = 0;

	public synchronized int increment(){
		return i++;
	}

}
