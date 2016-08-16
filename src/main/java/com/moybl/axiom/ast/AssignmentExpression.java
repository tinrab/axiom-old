package com.moybl.axiom.ast;

import com.moybl.axiom.Position;
import com.moybl.axiom.Token;

public class AssignmentExpression extends Expression {

	private Token operator;
	private Expression source;
	private Expression destination;

	public AssignmentExpression(Position position, Token operator, Expression source, Expression destination) {
		super(position);

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
