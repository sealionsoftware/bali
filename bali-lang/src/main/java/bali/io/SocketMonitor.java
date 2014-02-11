package bali.io;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.monitor.ConnectionMonitor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: Richard
 * Date: 07/02/14
 */
@MetaType(MetaTypes.MONITOR)
public class SocketMonitor implements ConnectionMonitor<Socket> {

	private ServerSocket serverSocket;
	private Socket connection;
	private Lock waitLock = new ReentrantLock();
	private Lock readLock = new ReentrantLock();

	public SocketMonitor(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void waitForConnection() throws java.lang.Exception {
		waitLock.lock();
		readLock.lock();
		connection = serverSocket.getConnection();
		readLock.unlock();
		waitLock.unlock();
	}

	public Socket getConnection() throws java.lang.Exception {
		readLock.lock();
		Socket ret = connection;
		connection = null;
		readLock.unlock();
		return ret;
	}
}
