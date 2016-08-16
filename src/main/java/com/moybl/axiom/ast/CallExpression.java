package com.moybl.axiom.ast;

import java.util.List;

public class CallExpression implements Expression {

	private Expression callee;
	private List<Expression> arguments;

	public CallExpression(Expression callee, List<Expression> arguments) {
		this.callee = callee;
		this.arguments = arguments;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Expression getCallee() {
		return callee;
	}

	public List<Expression> getArguments() {
		return arguments;
	}

}
