package bali.net;

import bali.Integer;
import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(Kind.INTERFACE)
public interface NetworkManager {

	public Host resolve(IPAddress address);

	public Host resolve(String name);

	public Socket connect(Host host, Integer port);

	public ServerSocket openPort(Integer port) throws Exception;

}
