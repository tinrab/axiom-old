package com.moybl.axiom.im;

import java.util.List;

public class ImSequence implements ImStatement {

	private List<ImStatement> statements;

	public ImSequence(List<ImStatement> statements) {
		this.statements = statements;
	}

	@Override
	public void accept(ImVisitor visitor) {
		visitor.visit(this);
	}

	public List<ImStatement> getStatements() {
		return statements;
	}

}
