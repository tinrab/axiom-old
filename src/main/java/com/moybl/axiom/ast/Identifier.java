package com.moybl.axiom.ast;

import com.moybl.axiom.Position;

public class Identifier extends Expression {

	private String name;

	public Identifier(Position position, String name) {
		super(position);
		this.name = name;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		return name.equals(((Identifier) obj).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public String getName() {
		return name;
	}

}
