package bali.net;

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
		StandardSocket ret =  new StandardSocket(socket);
		ret.initalise();
		return ret;
	}

	public void close(){
		try {
			delegate.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing socket", e);
		}
	}
}
