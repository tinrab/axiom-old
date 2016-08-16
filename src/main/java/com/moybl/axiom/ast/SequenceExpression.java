package com.moybl.axiom.ast;

import java.util.List;

public class SequenceExpression implements Expression {

	private List<Expression> expressions;

	public SequenceExpression(List<Expression> expressions) {
		this.expressions = expressions;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public List<Expression> getExpressions() {
		return expressions;
	}

}
