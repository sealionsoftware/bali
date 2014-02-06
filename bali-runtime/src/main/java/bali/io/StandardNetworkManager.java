package bali.io;

import bali.String;
import bali.Integer;

import java.net.InetSocketAddress;

import static bali.number.NumberFactory.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 06/02/14
 */
public class StandardNetworkManager implements NetworkManager {

	public Host resolve(IPAddress address) {
		return null;
	}

	public Host resolve(String name) {
		return null;
	}

	public Socket connect(Host host, Integer port) {
		return null;
	}

	public ServerSocket openPort(Integer port) throws Exception {
		java.net.ServerSocket serverSocket = new java.net.ServerSocket();
		serverSocket.bind(new InetSocketAddress(NUMBER_FACTORY.valueOf(port)));
		return new StandardServerSocket(serverSocket);
	}
}
