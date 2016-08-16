package com.moybl.axiom;

import com.moybl.axiom.ast.Node;
import com.moybl.axiom.semantics.NameChecker;

import java.io.ByteArrayInputStream;

public class Axiom {

	public static Program parse(String source) {
		Lexer lexer = new Lexer(new ByteArrayInputStream(source.getBytes()));
		Parser parser = new Parser(lexer);
		Node root = parser.parse();
		NameChecker nameChecker = new NameChecker();

		root.accept(nameChecker);

		return new Program(root, nameChecker.getSymbolMap());
	}

}
