package com.moybl.axiom.im;

import com.moybl.axiom.frame.Label;

public class ImLabel implements ImStatement {

	private Label label;

	public ImLabel(Label label) {
		this.label = label;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		return label.equals(((ImLabel) obj).label);
	}

	@Override
	public int hashCode() {
		return label.hashCode();
	}

	@Override
	public void accept(ImVisitor visitor) {
		visitor.visit(this);
	}

	public Label getLabel() {
		return label;
	}

}
