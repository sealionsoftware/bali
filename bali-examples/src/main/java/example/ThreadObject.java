package example;

import bali.*;

import java.lang.Exception;

/**
 * User: Richard
 * Date: 10/07/13
 */
public class ThreadObject implements Executable {

	public void execute() throws bali.Exception {

		Thread thread = new Thread(new Runnable() {
			public void run() {

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}

			}
		});

		thread.run();



	}
}
