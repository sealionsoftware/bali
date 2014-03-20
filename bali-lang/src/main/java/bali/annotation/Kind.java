package bali.annotation;

/**
 * User: Richard
 * Date: 09/09/13
 */
public enum Kind {

	OBJECT(false, true, false, true),
	VALUE(true, false, true, false),
	INTERFACE(true, false, false, false),
	BEAN(true, true, false, false),
	MONITOR(false, true, true, true),
	ENUM(true, false, false, false),
	XML(true, true, false, false),
	TABLE(true, false, false, false),
	WEB_SERVICE(true, false, false, false);

	private final boolean isReference; // Only abstract types can be used as Sites
	private final boolean isConstructable; // Only constructable types can be instanciated
	private final boolean isThreadSafe; // Some kinds are intrinsically threadsafe
	private final boolean isReified; // Some kinds are passed their type arguments as actual arguments

	Kind(boolean isReference, boolean isConstructable, boolean isThreadSafe, boolean isReified) {
		this.isReference = isReference;
		this.isConstructable = isConstructable;
		this.isThreadSafe = isThreadSafe;
		this.isReified = isReified;
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

	public boolean isReified() {
		return isReified;
	}
}
