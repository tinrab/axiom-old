package com.moybl.axiom.ast;

import java.util.List;

public class FunctionExpression implements Expression {

	private List<Identifier> parameters;
	private Statement body;

	public FunctionExpression(List<Identifier> parameters, Statement body) {
		this.parameters = parameters;
		this.body = body;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public List<Identifier> getParameters() {
		return parameters;
	}

	public Statement getBody() {
		return body;
	}

}
