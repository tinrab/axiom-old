package com.moybl.axiom.ast;

public interface Visitor {

	void visit(BinaryExpression acceptor);

	void visit(UnaryExpression acceptor);

	void visit(SequenceExpression acceptor);

	void visit(BlockStatement acceptor);

	void visit(ConditionalExpression acceptor);

	void visit(Literal acceptor);

	void visit(WhileStatement acceptor);

	void visit(CallExpression acceptor);

	void visit(FunctionExpression acceptor);

	void visit(ReferenceExpression acceptor);

	void visit(IfStatement acceptor);

	void visit(ForStatement acceptor);

	void visit(Identifier acceptor);

	void visit(MemberExpression acceptor);

	void visit(AssignmentExpression acceptor);

}
