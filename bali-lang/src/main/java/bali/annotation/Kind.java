package bali.annotation;

/**
 * User: Richard
 * Date: 09/09/13
 */
public enum Kind {

	OBJECT(false, true, false),
	VALUE(true, false, true),
	INTERFACE(true, false, false),
	BEAN(true, true, false),
	MONITOR(false, true, true),
	ENUM(true, false, false),
	XML(true, true, false),
	TABLE(true, false, false),
	WEB_SERVICE(true, false, false);

	private final boolean isReference; // Only abstract types can be used as Sites
	private final boolean isConstructable; // Only constructable types can be instanciated
	private final boolean isThreadSafe; // Some kinds are intrinsically threadsafe

	Kind(boolean isReference, boolean isConstructable, boolean isThreadSafe) {
		this.isReference = isReference;
		this.isConstructable = isConstructable;
		this.isThreadSafe = isThreadSafe;
	}

	public Boolean isReference() {
		return isReference;
	}

	public Boolean isConstructable() {
		return isConstructable;
	}

	public boolean isThreadSafe() {
		return isThreadSafe;
	}
}
