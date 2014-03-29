package bali.compiler.type;

/**
 * User: Richard
 * Date: 12/02/14
 */
public class SelfSite extends ErasedSite  {

	public SelfSite(Site self, Type erasure) {
		super(self, erasure);
	}

	public SelfSite(Site self) {
		super(self, self);
	}
}
