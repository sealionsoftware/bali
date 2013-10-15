package bali.compiler.validation.visitor;

/**
 * User: Richard
 * Date: 06/10/13
 */
public interface Control {

	public void validateChildrenNow();

	public void doNotValidateChildren();
}
