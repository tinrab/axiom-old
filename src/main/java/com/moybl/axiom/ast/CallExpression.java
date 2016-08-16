package com.moybl.axiom.ast;

import com.moybl.axiom.Position;

import java.util.List;

public class CallExpression extends Expression {

	private Expression callee;
	private List<Expression> arguments;

	public CallExpression(Position position, Expression callee, List<Expression> arguments) {
		super(position);
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
