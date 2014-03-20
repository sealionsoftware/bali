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

	public void waitForConnection() throws java.lang.Exception {
		Socket connection = serverSocket.getConnection();
		synchronized (this){
			if (this.connection != null){
				wait();
			}
			this.connection = connection;
		}
	}

	public synchronized Socket getConnection() throws java.lang.Exception {
		Socket connection = this.connection;
		this.connection = null;
		notify();
		return connection;
	}
}
