package com.moybl.axiom.frame;

import com.moybl.axiom.ast.*;
import com.moybl.axiom.semantics.SymbolMap;

public class FrameEvaluator implements Visitor {

	private SymbolMap symbolMap;
	private Frame currentFrame;
	private int scope;
	private FrameMap frameMap;

	private FrameEvaluator(SymbolMap symbolMap) {
		frameMap = new FrameMap();
		this.symbolMap = symbolMap;
	}

	public static FrameMap evaluate(Node root, SymbolMap symbolMap) {
		FrameEvaluator frameEvaluator = new FrameEvaluator(symbolMap);
		root.accept(frameEvaluator);

		return frameEvaluator.frameMap;
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
		for (int i = 0; i < acceptor.getStatements().size(); i++) {
			acceptor.getStatements().get(i).accept(this);
		}
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
		acceptor.getCallee().accept(this);

		for (int i = 0; i < acceptor.getArguments().size(); i++) {
			acceptor.getArguments().get(i).accept(this);
		}
	}

	@Override
	public void visit(FunctionExpression acceptor) {
		Frame frame = new Frame(Label.create(), acceptor, scope);

		scope++;
		for (int i = 0; i < acceptor.getParameters().size(); i++) {
			Identifier p = acceptor.getParameters().get(i);
			frameMap.putAccess(p, new Access(p.getName(), frame));
			p.accept(this);
		}

		currentFrame = frame;
		acceptor.getBody().accept(this);

		scope--;
		frameMap.putFrame(acceptor, frame);
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
		/*
		Access access = new Access(acceptor.getName(), currentFrame);

		frameMap.putAccess(acceptor, access);
		currentFrame.getVariables().add(access);
		*/
	}

	@Override
	public void visit(MemberExpression acceptor) {
		acceptor.getObject().accept(this);
		acceptor.getMember().accept(this);
	}

	@Override
	public void visit(AssignmentExpression acceptor) {
		acceptor.getDestination().accept(this);
		acceptor.getSource().accept(this);

		if (acceptor.getSource() instanceof FunctionExpression) {
			FunctionExpression functionExpression = (FunctionExpression) acceptor.getSource();
			Frame frame = new Frame(new Label(((Identifier) acceptor.getDestination()).getName()), functionExpression, scope);

			scope++;
			for (int i = 0; i < functionExpression.getParameters().size(); i++) {
				Identifier p = functionExpression.getParameters().get(i);

				Access access = new Access(p.getName(), frame);
				frameMap.putAccess(symbolMap.getInitialization(p), access);

				p.accept(this);
				frame.getVariables().add(access);
			}

			currentFrame = frame;

			functionExpression.getBody().accept(this);
			frameMap.putFrame(functionExpression, frame);

			scope--;
		}
	}

}
