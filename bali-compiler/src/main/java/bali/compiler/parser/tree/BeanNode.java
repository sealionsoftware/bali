package bali.compiler.parser.tree;

import bali.compiler.reference.BlockingReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class BeanNode extends TypeNode {

	private SiteNode superType;
	private final List<PropertyNode> properties = new ArrayList<>();

	public BeanNode() {
		this(null, null);
	}

	public BeanNode(Integer line, Integer character) {
		super(line, character);
	}

	public List<PropertyNode> getProperties() {
		return properties;
	}

	public void addProperty(PropertyNode propertyNode) {
		this.properties.add(propertyNode);
		children.add(propertyNode);
	}

	public SiteNode getSuperType() {
		return superType;
	}

	public void setSuperType(SiteNode superType) {
		children.add(superType);
		this.superType = superType;
	}
}
