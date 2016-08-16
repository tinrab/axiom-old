package com.moybl.axiom.ast;

public class MemberExpression implements Expression {

	public enum Kind {
		LIST, PROPERTY
	}

	private Kind kind;
	private Expression object;
	private Expression member;

	public MemberExpression(Kind kind, Expression object, Expression member) {
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
