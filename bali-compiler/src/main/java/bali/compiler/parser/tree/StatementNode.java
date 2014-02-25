package bali.compiler.parser.tree;

import bali.compiler.type.Site;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Richard
 * Date: 02/05/13
 */
public abstract class StatementNode extends Node {

	// Used by the ReferenceValidator to store type override information for expressions
	private Map<String, Site> referenceTypeOverrides = new HashMap<>();

	protected StatementNode() {
	}

	protected StatementNode(Integer line, Integer character) {
		super(line, character);
	}

	public void overrideType(String reference, Site type){
		referenceTypeOverrides.put(reference, type);
	}

	public Map<String, Site> getTypeOverrides(){
		return Collections.unmodifiableMap(referenceTypeOverrides);
	}
}
