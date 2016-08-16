package com.moybl.axiom;

import com.moybl.axiom.ast.Node;
import com.moybl.axiom.semantics.SymbolMap;

public class Program {

	private Node root;
	private SymbolMap symbolMap;

	public Program(Node root, SymbolMap symbolMap) {
		this.root = root;
		this.symbolMap = symbolMap;
	}

	public Node getRoot() {
		return root;
	}

	public SymbolMap getSymbolMap() {
		return symbolMap;
	}

}
