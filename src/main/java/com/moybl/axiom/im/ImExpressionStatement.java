package com.moybl.axiom.im;

public class ImExpressionStatement implements ImStatement {

	private ImExpression expression;

	public ImExpressionStatement(ImExpression expression) {
		this.expression = expression;
	}

	@Override
	public void accept(ImVisitor visitor) {
		visitor.visit(this);
	}

	public ImExpression getExpression() {
		return expression;
	}

}
