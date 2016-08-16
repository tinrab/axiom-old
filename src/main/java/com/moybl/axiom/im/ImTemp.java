package com.moybl.axiom.im;

import com.moybl.axiom.frame.Temp;

public class ImTemp implements ImExpression {

	private Temp temp;

	public ImTemp(Temp temp) {
		this.temp = temp;
	}

	@Override
	public void accept(ImVisitor visitor) {
		visitor.visit(this);
	}

	public Temp getTemp() {
		return temp;
	}

}
