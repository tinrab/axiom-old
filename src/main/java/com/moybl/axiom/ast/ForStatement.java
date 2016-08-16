package com.moybl.axiom.ast;

public class ForStatement implements Statement {

	private Statement init;
	private Expression condition;
	private Statement loop;
	private Statement body;

	public ForStatement(Statement init, Expression condition, Statement loop, Statement body) {
		this.init = init;
		this.condition = condition;
		this.loop = loop;
		this.body = body;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Statement getInit() {
		return init;
	}

	public Expression getCondition() {
		return condition;
	}

	public Statement getLoop() {
		return loop;
	}

	public Statement getBody() {
		return body;
	}

}
