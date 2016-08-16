package com.moybl.axiom.im;

public class ImConstant implements ImExpression {

	public enum Kind {
		INTEGER,
		FLOAT,
		STRING,
		NIL,
		BOOLEAN
	}

	private Kind kind;
	private Object value;

	public ImConstant(Kind kind, Object value) {
		this.kind = kind;
		this.value = value;
	}

	@Override
	public void accept(ImVisitor visitor) {
		visitor.visit(this);
	}

	public Kind getKind() {
		return kind;
	}

	public Object getValue() {
		return value;
	}

}
