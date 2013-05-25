package bali.compiler.bytecode;

/**
 * User: Richard
 * Date: 13/05/13
 */
public class ByteArrayClassLoader extends ClassLoader {

	private byte[] clazz;

	public ByteArrayClassLoader(byte[] clazz) {
		this.clazz = clazz;
	}

	protected java.lang.Class<?> findClass(String name) throws ClassNotFoundException {
		return defineClass(name, clazz, 0, clazz.length);
	}
}
