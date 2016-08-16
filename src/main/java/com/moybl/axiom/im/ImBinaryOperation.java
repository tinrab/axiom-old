package com.moybl.axiom.im;

public class ImBinaryOperation implements ImExpression {

	public enum Operator {
		ADD,
		SUBTRACT,
		MULTIPLY,
		DIVIDE,
		MODULO
	}

	private Operator operator;
	private ImExpression left;
	private ImExpression right;

	public ImBinaryOperation(Operator operator, ImExpression left, ImExpression right) {
		this.operator = operator;
		this.left = left;
		this.right = right;
	}

	public Operator getOperator() {
		return operator;
	}

	public ImExpression getLeft() {
		return left;
	}

	public ImExpression getRight() {
		return right;
	}

	@Override
	public void accept(ImVisitor visitor) {
		visitor.visit(this);
	}

}
