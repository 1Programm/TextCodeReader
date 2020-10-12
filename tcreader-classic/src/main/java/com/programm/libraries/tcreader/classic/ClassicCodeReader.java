package com.programm.libraries.tcreader.classic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.programm.libraries.tcreader.core.CodeInputCleaner;
import com.programm.libraries.tcreader.core.CodeReader;
import com.programm.libraries.tcreader.core.CodeReaderException;
import com.programm.libraries.tcreader.core.Node;

public class ClassicCodeReader extends CodeReader {

	private static Map<String, String> rootAttribs() {
		Map<String, String> attribs = new HashMap<>();
		attribs.put(Node.TYPE_KEY, Node.TYPE_ROOT_VALUE);
		return attribs;
	}

	private CodeInputCleaner cleaner;

	@Override
	protected Node doReadCode(String code) throws CodeReaderException {
		NodeRoot rootNode = new NodeRoot();
		Map<String, String> rootAttribs = rootAttribs();
		Map<String, String> rootVars = new HashMap<>();
		List<Node> children = readNodeChildren(code, 0, code.length(), rootNode, rootVars);

		if (children.size() == 1 && rootVars.size() == 0) {
			Node node = children.get(0);
			if (node instanceof NodeImpl) {
				((NodeImpl) node).parent = null;
			}
			return node;
		} else {
			rootNode.setChildren(children);
			rootNode.setAttributes(rootAttribs);
			rootNode.setVariables(rootVars);
			return rootNode;
		}
	}

	@Override
	protected Node doReadCodeLines(List<String> codeLines) throws CodeReaderException {
		throw new CodeReaderException("Operation not supported");
	}

	@Override
	protected CodeInputCleaner getInputCleaner() {
		if (cleaner == null) {
			cleaner = new ClassicCodeCleaner();
		}

		return cleaner;
	}

//	@Override
//	public Node readCode(InputStream is) throws CodeReaderException {
//		try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
//			String input = readAsOne(reader);
//
//			NodeRoot rootNode = new NodeRoot();
//			Map<String, String> rootAttribs = rootAttribs();
//			Map<String, String> rootVars = new HashMap<>();
//			List<Node> children = readNodeChildren(input, 0, input.length(), rootNode, rootVars);
//
//			if (children.size() == 1 && rootVars.size() == 0) {
//				Node node = children.get(0);
//				if (node instanceof NodeImpl) {
//					((NodeImpl) node).parent = null;
//				}
//				return node;
//			} else {
//				rootNode.setChildren(children);
//				rootNode.setAttributes(rootAttribs);
//				rootNode.setVariables(rootVars);
//				return rootNode;
//			}
//		} catch (IOException e) {
//			throw new CodeReaderException(e);
//		}
//	}
//
//	private String readAsOne(BufferedReader reader) throws IOException {
//		StringBuilder result = new StringBuilder();
//
//		boolean inComment = false;
//		String line;
//		while ((line = reader.readLine()) != null) {
//			line = line.trim();
//
//			if (line.isEmpty() || line.startsWith("//"))
//				continue;
//
//			int nextComment = line.indexOf("//");
//			if (nextComment != -1) {
//				line = line.substring(0, nextComment);
//			}
//
//			String actualLine = "";
//			for (int i = 0; i < line.length(); i++) {
//				char c = line.charAt(i);
//				boolean hasNext = i + 1 < line.length();
//
//				if (inComment) {
//					if (hasNext && c == '*' && line.charAt(i + 1) == '/') {
//						inComment = false;
//						i++;
//					}
//				} else if (hasNext && c == '/' && line.charAt(i + 1) == '*') {
//					inComment = true;
//					i++;
//				} else {
//					actualLine += c;
//				}
//			}
//
//			actualLine = actualLine.trim();
//
//			if (!actualLine.isEmpty()) {
//				result.append(actualLine);
//			}
//		}
//
//		return result.toString();
//	}

	private Node readNode(String input, IntPointer index, Node parent) throws CodeReaderException {

		// Next [
		int nextBracket = input.indexOf('[', index.get());

		if (nextBracket == -1) {
			throw new CodeReaderException("No opening bracket found!");
		}

		// Next ]
		int nextBracketClosing = input.indexOf(']', nextBracket);

		if (nextBracket == -1) {
			throw new CodeReaderException("No opening bracket found!");
		}

		String type = input.substring(index.get(), nextBracket).trim();
		Map<String, String> attributes = readNodeAttributes(input, nextBracket, nextBracketClosing, "");
		attributes.put(Node.TYPE_KEY, type);

		index.set(nextBracketClosing + 1);

		int nextCBracket = index.get();

		while (nextCBracket < input.length() && input.charAt(nextCBracket) == ' ') {
			nextCBracket++;
		}

		boolean hasCBracket = nextCBracket < input.length() && input.charAt(nextCBracket) == '{';

		if (!hasCBracket) {
			return new NodeImpl(parent, new ArrayList<>(), attributes, null);
		}

		int nextCBracketClosing = findNextBracketClosing(input, '{', '}', index.get() + 1);

		if (nextCBracketClosing == -1) {
			throw new CodeReaderException("");
		}

		NodeImpl node = new NodeImpl(parent, attributes);

		Map<String, String> variables = new HashMap<>();
		List<Node> children = readNodeChildren(input, nextCBracket + 1, nextCBracketClosing - 1, node, variables);

		index.set(nextCBracketClosing); // + 1 ?

		node.getChildren().addAll(children);
		node.variables.putAll(variables);

		return node;
	}

	private int findNextBracketClosing(String input, char open, char close, int index) throws CodeReaderException {
		int numOpen = 1;
		IntPointer pIndex = new IntPointer(index);

		while (numOpen > 0) {
			boolean nextClosing = isNextClosing(input, open, close, pIndex);
			pIndex.add(1);

			if (nextClosing) {
				numOpen--;
			} else {
				numOpen++;
			}
		}

		return pIndex.get();
	}

	private boolean isNextClosing(String input, char open, char close, IntPointer index) throws CodeReaderException {
		int nextOpen = input.indexOf(open, index.get());
		int nextClosed = input.indexOf(close, index.get());

		if (nextOpen == -1 && nextClosed == -1) {
			throw new CodeReaderException("No closing '" + close + "' bracket!");
		} else if (nextOpen != -1 && nextClosed != -1) {
			if (nextOpen < nextClosed) {
				index.set(nextOpen);
				return false;
			} else {
				index.set(nextClosed);
				return true;
			}
		} else if (nextOpen != -1) {
			index.set(nextOpen);
			return false;
		} else {
			index.set(nextClosed);
			return true;
		}
	}

	private Map<String, String> readNodeAttributes(String input, int start, int end, String keyPrefix)
			throws CodeReaderException {
		Map<String, String> attributes = new HashMap<>();

		String _attribs0 = input.substring(start + 1, end).trim();

		if (!_attribs0.isEmpty()) {
			String[] _attribs1 = _attribs0.split(",");

			for (String _attrib0 : _attribs1) {
				_attrib0 = _attrib0.trim();
				String[] _attrib1 = _attrib0.split("=");

				if (_attrib1.length != 2) {
					throw new CodeReaderException("Attributes must define key - value pairs!");
				}

				String key = keyPrefix + _attrib1[0];
				String _value = _attrib1[1];

				if (!_value.startsWith("\"") || !_value.endsWith("\"")) {
					throw new CodeReaderException("Attribute value must be placed in quotation marks!");
				}

				String value = _value.substring(1, _value.length() - 1);

				attributes.put(key, value);
			}
		}

		return attributes;
	}

	private List<Node> readNodeChildren(String input, int start, int end, Node parent, Map<String, String> variables)
			throws CodeReaderException {
		List<Node> children = new ArrayList<>();

		String _children = input.substring(start, end).trim();
		IntPointer index = new IntPointer();

		while (index.get() < _children.length()) {
			if (_children.charAt(index.get()) == '<') {
				int nextClosing = findNextBracketClosing(_children, '<', '>', index.get() + 1);
				Map<String, String> vars = readNodeAttributes(_children, index.get(), nextClosing - 1, "%");
				variables.putAll(vars);
				index.set(nextClosing);
			} else {
				Node node = readNode(_children, index, parent);
				children.add(node);
			}
		}

		return children;
	}

	public void setInputCleaner(CodeInputCleaner cleaner) {
		this.cleaner = cleaner;
	}

}
