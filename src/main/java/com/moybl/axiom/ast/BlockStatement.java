package com.moybl.axiom.ast;

import java.util.List;

public class BlockStatement implements Statement {

	private List<Statement> statements;

	public BlockStatement(List<Statement> statements) {
		this.statements = statements;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public List<Statement> getStatements() {
		return statements;
	}

}
