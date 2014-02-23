package bali.compiler.type;

/**
 * User: Richard
 * Date: 21/09/13
 */
public interface Site extends Type {

	Boolean isNullable();

	Boolean isThreadSafe();

	boolean isAssignableTo(Site s);

}
