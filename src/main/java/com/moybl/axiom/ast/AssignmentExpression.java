package com.moybl.axiom.ast;

import com.moybl.axiom.Token;

public class AssignmentExpression implements Expression {

	private Token operator;
	private Expression source;
	private Expression destination;

	public AssignmentExpression(Token operator, Expression source, Expression destination) {
		this.operator = operator;
		this.source = source;
		this.destination = destination;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Token getOperator() {
		return operator;
	}

	public Expression getSource() {
		return source;
	}

	public Expression getDestination() {
		return destination;
	}

}
