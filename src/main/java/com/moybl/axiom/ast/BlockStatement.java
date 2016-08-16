package com.moybl.axiom.ast;

import com.moybl.axiom.Position;

import java.util.List;

public class BlockStatement extends Statement {

	private List<Statement> statements;

	public BlockStatement(Position position, List<Statement> statements) {
		super(position);
		this.statements = statements;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public List<Statement> getStatements() {
		return statements;
	}

}
