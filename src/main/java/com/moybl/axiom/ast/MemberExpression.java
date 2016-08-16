package com.moybl.axiom.ast;

import com.moybl.axiom.Position;

public class MemberExpression extends Expression {

	public enum Kind {
		LIST, PROPERTY
	}

	private Kind kind;
	private Expression object;
	private Expression member;

	public MemberExpression(Position position, Kind kind, Expression object, Expression member) {
		super(position);
		this.kind = kind;
		this.object = object;
		this.member = member;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Kind getKind() {
		return kind;
	}

	public Expression getObject() {
		return object;
	}

	public Expression getMember() {
		return member;
	}

}
