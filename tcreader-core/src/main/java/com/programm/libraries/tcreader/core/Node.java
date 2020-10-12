package com.programm.libraries.tcreader.core;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public interface Node {

	String TYPE_KEY = "node.type";
	String TYPE_ROOT_VALUE = "node.root";

	List<Node> getChildren();

	String getAttrib(String key);

	Set<Entry<String, String>> getAttribSet();

	default String getType() {
		return getAttrib(TYPE_KEY);
	}

}
