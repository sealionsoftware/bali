package bali.net;

import bali.Integer;
import bali.String;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import static bali.Primitive.convert;
import static bali.net.Primitive.convert;

/**
 * TODO: complete
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
		serverSocket.bind(new InetSocketAddress(convert(port)));
		return new StandardServerSocket(serverSocket);
	}

	public ServerSocket openPortOnAddress(IPAddress address, Integer port) throws Exception {
		java.net.ServerSocket serverSocket = new java.net.ServerSocket();
		serverSocket.bind(new InetSocketAddress(InetAddress.getByAddress(convert(address)), convert(port)));
		return new StandardServerSocket(serverSocket);

	}
}
