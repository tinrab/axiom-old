package com.moybl.axiom.ast;

import com.moybl.axiom.Position;

import java.util.List;

public class FunctionExpression extends Expression {

	private List<Identifier> parameters;
	private Statement body;

	public FunctionExpression(Position position, List<Identifier> parameters, Statement body) {
		super(position);
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
