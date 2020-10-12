package com.programm.libraries.tcreader.classic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.programm.libraries.tcreader.core.Node;

import java.util.Set;

public class NodeImpl implements Node {

	Node parent;
	private final List<Node> children;
	final Map<String, String> attributes;
	final Map<String, String> variables;

	public NodeImpl(Node parent, List<Node> children, Map<String, String> attribs, Map<String, String> vars) {
		this.parent = parent;
		this.children = children;
		this.attributes = attribs;
		this.variables = vars;
	}

	public NodeImpl(Node parent, Map<String, String> attribs) {
		this.parent = parent;
		this.children = new ArrayList<>();
		this.attributes = attribs;
		this.variables = new HashMap<>();
	}

	@Override
	public List<Node> getChildren() {
		return children;
	}

	@Override
	public String getAttrib(String key) {
		String value = attributes.get(key);
		value = getVaredValue(value);

		return value;
	}

	@Override
	public Set<Entry<String, String>> getAttribSet() {
		return attributes.entrySet();
	}

	private String getVaredValue(String value) {
		if (value == null) {
			return null;
		}

		int indexPerc = value.indexOf('%');

		if (indexPerc != -1) {
			int indexSpace = value.indexOf(' ', indexPerc);
			if (indexSpace == -1) {
				indexSpace = value.length();
			}

			String var = value.substring(indexPerc, indexSpace);
			String val = getVar(var);

			if (val != null) {
				value = value.substring(0, indexPerc) + val + value.substring(indexSpace);
			}
		}

		return value;
	}

	private String getVar(String key) {
		String val = null;
		if (parent != null && parent instanceof NodeImpl) {
			NodeImpl nparent = (NodeImpl) parent;
			if (nparent.variables != null) {
				val = nparent.variables.get(key);
			}

			if (val == null) {
				val = nparent.getVar(key);
			}

			if (val != null) {
				return val;
			}
		}

		return null;
	}

}
