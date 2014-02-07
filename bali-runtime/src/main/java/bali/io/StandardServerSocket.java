package bali.io;

import bali.annotation.ThreadSafe;

/**
 * User: Richard
 * Date: 06/02/14
 */
public class StandardServerSocket implements ServerSocket {

	private java.net.ServerSocket delegate;

	public StandardServerSocket(java.net.ServerSocket delegate) {
		this.delegate = delegate;
	}

	@ThreadSafe
	public Socket getConnection() throws Exception {
		java.net.Socket socket = delegate.accept();
		StandardSocket ret =  new StandardSocket(socket);
		ret.initalise();
		return ret;
	}

	public void close() throws java.lang.Exception {
		delegate.close();
	}
}
