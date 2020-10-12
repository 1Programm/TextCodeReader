package com.programm.libraries.tcreader.classic;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.programm.libraries.tcreader.core.CodeReaderException;
import com.programm.libraries.tcreader.core.Node;

public class ClassicReaderTest {

	@Test
	public void testVariable() throws CodeReaderException {
		InputStream in = new ByteArrayInputStream("<var1=\"foo\", var2=\"bar\">\n%var1[]\n%var2[]".getBytes());
		ClassicCodeReader reader = new ClassicCodeReader();
		Node root = reader.readCode(in);

		List<Node> rootChildren = root.getChildren();

		assert rootChildren.size() == 2;

		Node child1 = rootChildren.get(0);
		assert child1.getType().equals("foo");

		Node child2 = rootChildren.get(1);
		assert child2.getType().equals("bar");
	}

	@Test
	public void testFileInput() throws CodeReaderException {
		InputStream in = getClass().getResourceAsStream("/TestFile2.txt");
		ClassicCodeReader reader = new ClassicCodeReader();
		Node root = reader.readCode(in);

		assert root.getType().equals(Node.TYPE_ROOT_VALUE);

		List<Node> rootChildren = root.getChildren();

		assert rootChildren.size() == 1;

		Node myRoot = rootChildren.get(0);
		String myRootType = myRoot.getType();
		String myRootName = myRoot.getAttrib("name");

		assert myRootType.equals("MyRoot");
		assert myRootName.equals("bar");

		List<Node> myRootChildren = myRoot.getChildren();

		assert myRootChildren.size() == 2;

		Node node1 = myRootChildren.get(0);
		String node1Type = node1.getType();
		String node1Name = node1.getAttrib("name");

		assert node1Type.equals("Node1");
		assert node1Name.equals("Node 1");

		Node node2 = myRootChildren.get(1);
		String node2Type = node2.getType();
		String node2Secret = node2.getAttrib("mySecret");

		assert node2Type.equals("Node2");
		assert node2Secret.equals("cake");

	}

}
