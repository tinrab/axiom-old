package com.moybl.axiom.ast;

public class WhileStatement implements Statement {

	private Expression condition;
	private Statement body;

	public WhileStatement(Expression condition, Statement body) {
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

}
