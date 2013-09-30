package example;

import bali.Executable;

/**
 * User: Richard
 * Date: 10/07/13
 */
public class ThreadObject implements Executable {

	public void execute() {

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
