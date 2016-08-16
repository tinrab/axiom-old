package com.moybl.axiom.ast;

import com.moybl.axiom.Token;

public class BinaryExpression implements Expression {

	private Token operator;
	private Expression left;
	private Expression right;

	public BinaryExpression(Token operator, Expression left, Expression right) {
		this.operator = operator;
		this.left = left;
		this.right = right;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Token getOperator() {
		return operator;
	}

	public Expression getLeft() {
		return left;
	}

	public Expression getRight() {
		return right;
	}

}
