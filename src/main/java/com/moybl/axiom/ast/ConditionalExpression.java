package com.moybl.axiom.ast;

public class ConditionalExpression implements Expression {

	private Expression test;
	private Expression consequent;
	private Expression alternate;

	public ConditionalExpression(Expression test, Expression consequent, Expression alternate) {
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
