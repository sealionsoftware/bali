package bali.maven;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: Richard
 * Date: 31 Mar
 */
public class LinkClassLoader extends ClassLoader {

	private Map<String, Class> links;

	public LinkClassLoader(Set<Class<?>> links) {
		this.links = new HashMap<>();
		for (Class c : links){
			this.links.put(c.getName(), c);
		}
	}

	public Class<?> findClass(String name) throws ClassNotFoundException {
		Class ret = links.get(name);
		if (ret != null){
			return ret;
		}
		return super.findClass(name);
	}
}
