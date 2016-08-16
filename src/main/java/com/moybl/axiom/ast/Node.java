package com.moybl.axiom.ast;

public interface Node {

	public void accept(Visitor visitor);

}
