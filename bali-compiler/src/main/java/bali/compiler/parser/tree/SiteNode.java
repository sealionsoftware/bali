package bali.compiler.parser.tree;


import bali.compiler.type.ParametrizedSite;
import bali.compiler.type.Site;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Richard
 * Date: 29/04/13
 */
public class SiteNode extends Node {

	private String className;
	private List<SiteNode> parameters = new ArrayList<>();
	private Site site;
	private Boolean erase = false;

	public SiteNode() {
	}

	public SiteNode(Integer line, Integer character) {
		super(line, character);
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public List<SiteNode> getParameters() {
		return parameters;
	}

	public void addParameter(SiteNode parameter) {
		this.parameters.add(parameter);
	}

	public List<Node> getChildren() {
		return new ArrayList<Node>(parameters);
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Boolean getErase() {
		return erase;
	}

	public void setErase(Boolean erase) {
		this.erase = erase;
	}

	public boolean equals(Object o) {

		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		SiteNode other = (SiteNode) o;

		if (!site.getName().equals(other.site.getName())) {
			return false;
		}

		return parameters.equals(other.parameters);
	}

	public String toString() {

		StringBuilder sb = new StringBuilder(className);
		if (parameters.size() > 0) {
			sb.append("<");
			Iterator<SiteNode> i = parameters.iterator();
			sb.append(i.next());
			while (i.hasNext()) {
				sb.append(",").append(i.next());
			}
			sb.append(">");
		}
		return sb.toString();
	}

	public static class CouldNotResolveException extends RuntimeException {
	}
}
