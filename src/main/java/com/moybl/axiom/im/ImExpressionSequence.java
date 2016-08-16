package com.moybl.axiom.im;

public class ImExpressionSequence implements ImExpression{

	private ImStatement statement;
	private ImExpression expression;

	public ImExpressionSequence(ImStatement statement, ImExpression expression) {
		this.statement = statement;
		this.expression = expression;
	}

	@Override
	public void accept(ImVisitor visitor) {
		visitor.visit(this);
	}

	public ImStatement getStatement() {
		return statement;
	}

	public ImExpression getExpression() {
		return expression;
	}

}
