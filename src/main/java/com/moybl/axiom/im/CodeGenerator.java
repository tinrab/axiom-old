package com.moybl.axiom.im;

import com.moybl.axiom.ast.*;
import com.moybl.axiom.frame.*;
import com.moybl.axiom.semantics.SymbolMap;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerator implements Visitor {

	public static List<Chunk> generate(Node root, SymbolMap symbolMap, FrameMap frameMap) {
		CodeGenerator codeGenerator = new CodeGenerator(symbolMap, frameMap);

		root.accept(codeGenerator);

		return codeGenerator.chunks;
	}

	private SymbolMap symbolMap;
	private FrameMap frameMap;
	private List<Chunk> chunks;
	private Frame currentFrame;
	private ImCode result;

	private CodeGenerator(SymbolMap symbolMap, FrameMap frameMap) {
		this.symbolMap = symbolMap;
		this.frameMap = frameMap;
		chunks = new ArrayList<>();
	}

	@Override
	public void visit(BinaryExpression acceptor) {
		acceptor.getLeft().accept(this);
		ImExpression left = (ImExpression) result;
		acceptor.getRight().accept(this);
		ImExpression right = (ImExpression) result;

		ImBinaryOperation.Operator op = null;

		switch (acceptor.getOperator()) {
			case PLUS:
				op = ImBinaryOperation.Operator.ADD;
				break;
			case MINUS:
				op = ImBinaryOperation.Operator.SUBTRACT;
				break;
			case ASTERISK:
				op = ImBinaryOperation.Operator.MULTIPLY;
				break;
			case SLASH:
				op = ImBinaryOperation.Operator.DIVIDE;
				break;
			case PERCENT:
				op = ImBinaryOperation.Operator.MODULO;
				break;
			default:
				throw ImException.internal();
		}

		result = new ImBinaryOperation(op, left, right);
	}

	@Override
	public void visit(UnaryExpression acceptor) {
	}

	@Override
	public void visit(SequenceExpression acceptor) {
		List<ImStatement> statements = new ArrayList<>();

		for (int i = 0; i < acceptor.getExpressions().size(); i++) {
			Expression e = acceptor.getExpressions().get(i);
			e.accept(this);

			if (result instanceof ImExpression) {
				statements.add(new ImExpressionStatement((ImExpression) result));
			} else {
				statements.add((ImStatement) result);
			}
		}

		result = new ImExpressionSequence(new ImSequence(statements), (ImExpression) result);
	}

	@Override
	public void visit(BlockStatement acceptor) {
		List<ImStatement> statements = new ArrayList<>();

		for (int i = 0; i < acceptor.getStatements().size(); i++) {
			Statement s = acceptor.getStatements().get(i);
			s.accept(this);

			if (result instanceof ImExpression) {
				statements.add(new ImExpressionStatement((ImExpression) result));
			} else {
				statements.add((ImStatement) result);
			}
		}

		result = new ImSequence(statements);
	}

	@Override
	public void visit(ConditionalExpression acceptor) {
	}

	@Override
	public void visit(Literal acceptor) {
		ImConstant.Kind kind = null;

		switch (acceptor.getKind()) {
			case BOOLEAN:
				kind = ImConstant.Kind.BOOLEAN;
				break;
			case FLOAT:
				kind = ImConstant.Kind.FLOAT;
				break;
			case INTEGER:
				kind = ImConstant.Kind.INTEGER;
				break;
			case NIL:
				kind = ImConstant.Kind.NIL;
				break;
			case STRING:
				kind = ImConstant.Kind.STRING;
				break;
			default:
				throw ImException.internal();
		}

		result = new ImConstant(kind, acceptor.getValue());
	}

	@Override
	public void visit(WhileStatement acceptor) {
	}

	@Override
	public void visit(CallExpression acceptor) {
	}

	@Override
	public void visit(FunctionExpression acceptor) {
		Frame frame = frameMap.getFrame(acceptor);
		Frame tmpFrame = currentFrame;
		currentFrame = frame;

		acceptor.getBody().accept(this);
		if (result instanceof ImExpression) {
			chunks.add(new Chunk(frame, new ImExpressionStatement((ImExpression) result)));
		} else {
			chunks.add(new Chunk(frame, (ImStatement) result));
		}

		currentFrame = tmpFrame;
	}

	@Override
	public void visit(ReferenceExpression acceptor) {
	}

	@Override
	public void visit(IfStatement acceptor) {
	}

	@Override
	public void visit(ForStatement acceptor) {
	}

	@Override
	public void visit(Identifier acceptor) {
		AssignmentExpression init = symbolMap.getInitialization(acceptor);
		Access access = frameMap.getAccess(acceptor);

		// TODO fixes
		result = new ImTemp(new Temp(acceptor.getName()));
	}

	@Override
	public void visit(MemberExpression acceptor) {
	}

	@Override
	public void visit(AssignmentExpression acceptor) {
		acceptor.getDestination().accept(this);
		ImExpression d = (ImExpression) result;
		acceptor.getSource().accept(this);
		ImExpression s = (ImExpression) result;

		// TODO fixes
		ImTemp t1 = new ImTemp(Temp.create());
		ImTemp t2 = new ImTemp(Temp.create());
		List<ImStatement> statements = new ArrayList<>();

		statements.add(new ImMemoryWrite(d, s));

		result = new ImExpressionSequence(new ImSequence(statements), t2);
	}

}
