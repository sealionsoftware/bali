/**
 * User: Richard
 * Date: 08 Apr
 */
public class MainThread {


	public static void main(String[] args) {

		System.out.println("Main thread started");
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Second thread started");
				try {
					Thread.sleep(2000);  // wait two seconds
				} catch(Exception e){}
				System.out.println("Second thread (almost) finished");
			}
		}).start();
		System.out.println("Main thread (almost) finished");
	}

}
