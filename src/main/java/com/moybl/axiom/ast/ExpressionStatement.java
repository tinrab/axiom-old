package com.moybl.axiom.ast;

import com.moybl.axiom.Position;

public class ExpressionStatement extends Statement {

	private Expression expression;

	public ExpressionStatement(Position position, Expression expression) {
		super(position);
		this.expression = expression;
	}

	public void accept(Visitor visitor) {
		expression.accept(visitor);
	}

	public Expression getExpression() {
		return expression;
	}

}
