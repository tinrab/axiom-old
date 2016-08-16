package com.moybl.axiom.ast;

import com.moybl.axiom.Position;

public class IfStatement extends Statement {

	private Expression condition;
	private Statement body;
	private Statement elseBody;

	public IfStatement(Position position, Expression condition, Statement body, Statement elseBody) {
		super(position);
		this.condition = condition;
		this.body = body;
		this.elseBody = elseBody;
	}

	public IfStatement(Position position, Expression condition, Statement body) {
		super(position);
		this.condition = condition;
		this.body = body;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Expression getCondition() {
		return condition;
	}

	public Statement getBody() {
		return body;
	}

	public Statement getElseBody() {
		return elseBody;
	}

}
