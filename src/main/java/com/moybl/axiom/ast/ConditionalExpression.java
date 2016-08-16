package com.moybl.axiom.ast;

import com.moybl.axiom.Position;

public class ConditionalExpression extends Expression {

	private Expression test;
	private Expression consequent;
	private Expression alternate;

	public ConditionalExpression(Position position, Expression test, Expression consequent, Expression alternate) {
		super(position);
		this.test = test;
		this.consequent = consequent;
		this.alternate = alternate;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Expression getTest() {
		return test;
	}

	public Expression getConsequent() {
		return consequent;
	}

	public Expression getAlternate() {
		return alternate;
	}

}
