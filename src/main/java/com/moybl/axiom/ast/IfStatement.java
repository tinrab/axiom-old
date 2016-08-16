package com.moybl.axiom.ast;

public class IfStatement implements Statement {

	private Expression condition;
	private Statement body;
	private Statement elseBody;

	public IfStatement(Expression condition, Statement body, Statement elseBody) {
		this.condition = condition;
		this.body = body;
		this.elseBody = elseBody;
	}

	public IfStatement(Expression condition, Statement body) {
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
