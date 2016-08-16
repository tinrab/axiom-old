package com.moybl.axiom.im;

public class ImMemoryWrite implements ImStatement {

	private ImExpression destination;
	private ImExpression source;

	public ImMemoryWrite(ImExpression destination, ImExpression source) {
		this.destination = destination;
		this.source = source;
	}

	@Override
	public void accept(ImVisitor visitor) {
		visitor.visit(this);
	}

	public ImExpression getDestination() {
		return destination;
	}

	public ImExpression getSource() {
		return source;
	}

}
