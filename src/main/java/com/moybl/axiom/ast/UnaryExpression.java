package com.moybl.axiom.ast;

import com.moybl.axiom.Position;
import com.moybl.axiom.Token;

public class UnaryExpression extends Expression {

	public enum Kind {
		POSTFIX, PREFIX
	}

	private Token operator;
	private Expression expression;
	private Kind kind;

	public UnaryExpression(Position position, Token operator, Expression expression, Kind kind) {
		super(position);
		this.operator = operator;
		this.expression = expression;
		this.kind = kind;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public Token getOperator() {
		return operator;
	}

	public Expression getExpression() {
		return expression;
	}

	public Kind getKind() {
		return kind;
	}

}
