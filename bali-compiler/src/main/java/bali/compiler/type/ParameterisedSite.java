package bali.compiler.type;

import bali.compiler.reference.Reference;

import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 12/02/14
 */
public class ParameterisedSite extends ParameterisedType implements Site {

	private Boolean nullable;
	private Boolean threadsafe;

	public ParameterisedSite(Reference<Class> template) {
		this(template, Collections.<Site>emptyList(), false, false);
	}

	public ParameterisedSite(Reference<Class> template, Boolean nullable, Boolean threadsafe) {
		this(template, Collections.<Site>emptyList(), nullable, threadsafe);
	}

	public ParameterisedSite(Reference<Class> template, List<Site> typeArguments, Boolean nullable, Boolean threadsafe) {
		super(template, typeArguments);
		this.nullable = nullable;
		this.threadsafe = threadsafe;
	}

	public boolean isAssignableTo(Site t) {

		if (t == null) {
			return true;
		}

		if (isNullable() && !t.isNullable()){
			return false;
		}

		if (t.isThreadSafe() && !isThreadSafe()){
			return false;
		}

		return super.isAssignableTo(t);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		if (nullable){
			sb.append("?");
		}
		if (threadsafe){
			sb.append("@");
		}
		return sb.toString();
	}

	public Boolean isNullable() {
		return nullable;
	}

	public Boolean isThreadSafe() {
		return threadsafe;
	}
}
