package com.moybl.axiom.ast;

import com.moybl.axiom.Position;

import java.util.List;

public class SequenceExpression extends Expression {

	private List<Expression> expressions;

	public SequenceExpression(Position position, List<Expression> expressions) {
		super(position);
		this.expressions = expressions;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public List<Expression> getExpressions() {
		return expressions;
	}

}
