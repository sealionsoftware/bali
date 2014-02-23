package bali.annotation;

/**
 * User: Richard
 * Date: 09/09/13
 */
public enum Kind {

	OBJECT(false, true),
	VALUE(true, false),
	INTERFACE(true, false),
	BEAN(true, true),
	MONITOR(false, true),
	ENUM(true, false),
	XML(true, true),
	TABLE(true, false),
	WEB_SERVICE(true, false);

	private final boolean isReference; // Only abstract types can be used as Sites
	private final boolean isConstructable; // Only constructable types can be instanciated

	Kind(boolean isReference, boolean isConstructable) {
		this.isReference = isReference;
		this.isConstructable = isConstructable;
	}

	public Boolean isReference() {
		return isReference;
	}

	public Boolean isConstructable() {
		return isConstructable;
	}
}
