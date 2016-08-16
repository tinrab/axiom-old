package com.moybl.axiom.ast;

import com.moybl.axiom.Position;

public class WhileStatement extends Statement {

	private Expression condition;
	private Statement body;

	public WhileStatement(Position position, Expression condition, Statement body) {
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

}
