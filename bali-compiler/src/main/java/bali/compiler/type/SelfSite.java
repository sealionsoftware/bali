package bali.compiler.type;

/**
 * User: Richard
 * Date: 12/02/14
 */
public class SelfSite extends SelfType implements Site {

	private Site self;

	public SelfSite(Site self) {
		super(self);
		this.self = self;
	}

	public Boolean isNullable() {
		return self.isNullable();
	}

	public Boolean isThreadSafe() {
		return self.isThreadSafe();
	}

	public boolean isAssignableTo(Site s) {
		return self.isAssignableTo(s);
	}
}
