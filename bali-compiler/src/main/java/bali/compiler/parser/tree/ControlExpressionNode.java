package bali.compiler.parser.tree;

import bali.compiler.type.Site;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Richard
 * Date: 29/04/13
 */
public abstract class ControlExpressionNode extends StatementNode {

	// Used by the ReferenceValidator to store type override information for conditional statements
	private Map<String, Site> referenceTypeOverrides = new HashMap<>();

	public ControlExpressionNode() {
	}

	public ControlExpressionNode(Integer line, Integer character) {
		super(line, character);
	}

	public void overrideType(String reference, Site type){
		referenceTypeOverrides.put(reference, type);
	}

	public Map<String, Site> getTypeOverrides(){
		return Collections.unmodifiableMap(referenceTypeOverrides);
	}

}
