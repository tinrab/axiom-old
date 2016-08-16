package com.moybl.axiom;

import com.moybl.axiom.ast.Node;
import com.moybl.axiom.semantics.NameChecker;
import com.moybl.axiom.semantics.SymbolMap;

import java.io.ByteArrayInputStream;

public class Axiom {

	public static Program parse(String source) {
		Lexer lexer = new Lexer(new ByteArrayInputStream(source.getBytes()));
		Parser parser = new Parser(lexer);
		Node root = parser.parse();
		SymbolMap symbolMap = NameChecker.check(root);

		return new Program(root, symbolMap);
	}

}
