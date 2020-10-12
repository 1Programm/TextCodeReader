package com.programm.libraries.tcreader.classic;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.programm.libraries.tcreader.core.Node;

class Test {

	public static void main(String[] args) throws Exception {
		ClassicCodeReader reader = new ClassicCodeReader();
		FileInputStream in = new FileInputStream("C:\\Projekte\\TestWorkspace\\TextCodeReader\\examples\\Rooms.txt");

		Node root = reader.readCode(in);

		printRec(root, "");
	}

	private static void printRec(Node node, String pre) {
		List<Node> children = node.getChildren();

		Set<Entry<String, String>> attributes = node.getAttribSet();
		String attribs = "";
		for (Entry<String, String> entry : attributes) {
			if (!attribs.isEmpty())
				attribs += ", ";
			attribs += entry.getKey() + ": " + node.getAttrib(entry.getKey());
		}

		if (children.isEmpty()) {
			System.out.println(pre + node.getType() + "(" + attribs + ");");
		} else {
			System.out.println(pre + node.getType() + "(" + attribs + ") {");
		}

		for (Node child : node.getChildren()) {
			printRec(child, pre + "  ");
		}

		if (!children.isEmpty()) {
			System.out.println(pre + "}");
		}
	}

}
