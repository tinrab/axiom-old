package com.moybl.axiom.semantics;

import com.moybl.axiom.ast.*;

public class NameChecker implements Visitor {

	private SymbolMap symbolMap;

	public NameChecker() {
		symbolMap = new SymbolMap();
	}

	@Override
	public void visit(BinaryExpression acceptor) {
		acceptor.getLeft().accept(this);
		acceptor.getRight().accept(this);
	}

	@Override
	public void visit(UnaryExpression acceptor) {
		acceptor.getExpression().accept(this);
	}

	@Override
	public void visit(SequenceExpression acceptor) {
		for (int i = 0; i < acceptor.getExpressions().size(); i++) {
			acceptor.getExpressions().get(i).accept(this);
		}
	}

	@Override
	public void visit(BlockStatement acceptor) {
		symbolMap.enterScope();

		for (int i = 0; i < acceptor.getStatements().size(); i++) {
			acceptor.getStatements().get(i).accept(this);
		}

		symbolMap.exitScope();
	}

	@Override
	public void visit(ConditionalExpression acceptor) {
		acceptor.getTest().accept(this);
		acceptor.getConsequent().accept(this);
		acceptor.getAlternate().accept(this);
	}

	@Override
	public void visit(Literal acceptor) {
	}

	@Override
	public void visit(WhileStatement acceptor) {
		acceptor.getCondition().accept(this);
		acceptor.getBody().accept(this);
	}

	@Override
	public void visit(CallExpression acceptor) {
		// TODO
	}

	@Override
	public void visit(FunctionExpression acceptor) {
		symbolMap.enterScope();

		for (int i = 0; i < acceptor.getParameters().size(); i++) {
			acceptor.getParameters().get(i).accept(this);
		}

		acceptor.getBody().accept(this);

		symbolMap.exitScope();
	}

	@Override
	public void visit(ReferenceExpression acceptor) {
	}

	@Override
	public void visit(IfStatement acceptor) {
		acceptor.getCondition().accept(this);
		acceptor.getBody().accept(this);

		if (acceptor.getElseBody() != null) {
			acceptor.getElseBody().accept(this);
		}
	}

	@Override
	public void visit(ForStatement acceptor) {
		acceptor.getInit().accept(this);
		acceptor.getCondition().accept(this);
		acceptor.getLoop().accept(this);
		acceptor.getBody().accept(this);
	}

	@Override
	public void visit(Identifier acceptor) {
		AssignmentExpression init = symbolMap.find(acceptor);

		if (init == null) {
			throw SemanticException.undefined(acceptor.getName());
		}

		symbolMap.setInitialization(acceptor, init);
	}

	@Override
	public void visit(MemberExpression acceptor) {
	}

	@Override
	public void visit(AssignmentExpression acceptor) {
		symbolMap.insert(acceptor.getDestination(), acceptor);
		acceptor.getDestination().accept(this);
		acceptor.getSource().accept(this);
	}

	public SymbolMap getSymbolMap() {
		return symbolMap;
	}

}
