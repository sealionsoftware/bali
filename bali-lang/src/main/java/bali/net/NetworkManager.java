package bali.net;

import bali.Integer;
import bali.String;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(Kind.INTERFACE)
public interface NetworkManager {

	public Host resolve(@Name("address") IPAddress address);

	public Host resolve(@Name("name") String name);

	public Socket connect(@Name("host") Host host, @Name("port") Integer port);

	public ServerSocket openPortOnAddress(@Name("address") IPAddress address, @Name("port") Integer port) throws Exception;

	public ServerSocket openPort(@Name("port") Integer port) throws Exception;



}
