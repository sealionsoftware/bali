package bali.net;

import bali.BaliThrowable;

import java.io.IOException;

/**
 * User: Richard
 * Date: 06/02/14
 */
public class StandardServerSocket implements ServerSocket {

	private java.net.ServerSocket delegate;

	public StandardServerSocket(java.net.ServerSocket delegate) {
		this.delegate = delegate;
	}

	public Socket getConnection() throws Exception {
		java.net.Socket socket = delegate.accept();
		socket.setSoTimeout(5000);
		StandardSocket ret = new StandardSocket(socket);
		ret.initialise();
		return ret;
	}

	public void close(){
		try {
			delegate.close();
		} catch (IOException e) {
			throw new BaliThrowable(e.getMessage());
		}
	}
}
