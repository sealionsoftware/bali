package bali.io;

import bali.String;
import bali.Integer;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(MetaTypes.INTERFACE)
public interface NetworkManager {

	public Host resolve(IPAddress address);

	public Host resolve(String name);

	public Socket connect(Host host, Integer port);

	public ServerSocket openPort(Integer port) throws Exception;

}
