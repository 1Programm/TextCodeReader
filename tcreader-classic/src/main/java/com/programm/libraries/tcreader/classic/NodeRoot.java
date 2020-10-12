package com.programm.libraries.tcreader.classic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.programm.libraries.tcreader.core.Node;

public class NodeRoot extends NodeImpl {

	public NodeRoot() {
		super(null, new ArrayList<>(), new HashMap<>(), new HashMap<>());
	}

	void setChildren(List<Node> children) {
		getChildren().clear();
		getChildren().addAll(children);
	}

	void setAttributes(Map<String, String> attributes) {
		this.attributes.clear();
		this.attributes.putAll(attributes);
	}

	void setVariables(Map<String, String> variables) {
		this.variables.clear();
		this.variables.putAll(variables);
	}

}
