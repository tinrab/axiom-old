package com.moybl.axiom.ast;

import com.moybl.axiom.Position;
import com.moybl.axiom.Token;

public class BinaryExpression extends Expression {

	private Token operator;
	private Expression left;
	private Expression right;

	public BinaryExpression(Position position, Token operator, Expression left, Expression right) {
		super(position);
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
