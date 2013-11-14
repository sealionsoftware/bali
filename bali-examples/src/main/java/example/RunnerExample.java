package example;


import bali.CharArrayString;
import bali.Executable;
import bali.String;

/**
 * User: Richard
 * Date: 07/11/13
 */
public class RunnerExample implements Executable {

	public void execute() throws Throwable {

		for (int i = 0 ; i < 100 ; i++){
			final String aString = new CharArrayString(Integer.toString(i).toCharArray());
			new Thread(new Runnable(){
				public void run() {
					_.CONSOLE.writeLine(aString);
				}
			}).start();
		}

	}
}
