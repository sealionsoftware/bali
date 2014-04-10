package bali.net;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Parameters;
import bali.monitor.ConnectionMonitor;

/**
 * User: Richard
 * Date: 07/02/14
 */
@MetaType(Kind.MONITOR)
public class SocketMonitor implements ConnectionMonitor<Socket> {

	private ServerSocket serverSocket;
	private Socket connection;

	@Parameters
	public SocketMonitor(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void waitForConnection() {
		try {
			Socket connection = serverSocket.getConnection();
			synchronized (this){
				if (this.connection != null){
					wait();
				}
				this.connection = connection;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}


	}

	public synchronized Socket getConnection() {
		Socket connection = this.connection;
		this.connection = null;
		notify();
		return connection;
	}
}
