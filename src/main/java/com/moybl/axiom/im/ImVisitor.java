package com.moybl.axiom.im;

public interface ImVisitor {

	void visit(ImBinaryOperation acceptor);

	void visit(ImConstant acceptor);

	void visit(ImExpressionSequence acceptor);

	void visit(ImExpressionStatement acceptor);

	void visit(ImLabel acceptor);

	void visit(ImMemoryRead acceptor);

	void visit(ImMemoryWrite acceptor);

	void visit(ImSequence acceptor);

	void visit(ImTemp acceptor);
}
