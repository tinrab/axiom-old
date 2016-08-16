package com.moybl.axiom.ast;

public class ExpressionStatement implements Statement {

	private Expression expression;

	public ExpressionStatement(Expression expression) {
		this.expression = expression;
	}

	public void accept(Visitor visitor) {
		expression.accept(visitor);
	}

	public Expression getExpression() {
		return expression;
	}

}
