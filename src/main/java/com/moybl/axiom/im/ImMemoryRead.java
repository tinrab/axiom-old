package com.moybl.axiom.im;

public class ImMemoryRead implements ImExpression {

	private ImExpression expression;

	public ImMemoryRead(ImExpression expression) {
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
