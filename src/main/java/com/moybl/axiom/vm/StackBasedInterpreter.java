package com.moybl.axiom.vm;

import com.moybl.axiom.Lexer;
import com.moybl.axiom.Parser;
import com.moybl.axiom.ast.*;

import java.io.ByteArrayInputStream;

public class StackBasedInterpreter implements Visitor {

	private Object result;

	public Object evaluate(String source) {
		Lexer lexer = new Lexer(new ByteArrayInputStream(source.getBytes()));
		Parser parser = new Parser(lexer);
		Node program = parser.parse();

		return evaluate(program);
	}

	public Object evaluate(Node program) {
		program.accept(this);

		return result;
	}

	@Override
	public void visit(BinaryExpression acceptor) {
		switch (acceptor.getOperator()) {
			case PLUS:
			case MINUS:
			case ASTERISK:
			case SLASH:
				acceptor.getLeft().accept(this);
				Object a = result;
				acceptor.getRight().accept(this);
				Object b = result;

				if (a instanceof Long) {
					long intA = (Long) a;

					if (b instanceof Long) {
						long intB = (Long) b;

						switch (acceptor.getOperator()) {
							case PLUS:
								result = intA + intB;
								break;
							case MINUS:
								result = intA - intB;
								break;
							case ASTERISK:
								result = intA * intB;
								break;
							case SLASH:
								result = intA / intB;
								break;
						}
					} else {
						double floatB = (Double) b;

						switch (acceptor.getOperator()) {
							case PLUS:
								result = intA + floatB;
								break;
							case MINUS:
								result = intA - floatB;
								break;
							case ASTERISK:
								result = intA * floatB;
								break;
							case SLASH:
								result = intA / floatB;
								break;
						}
					}
				} else {
					double floatA = (Double) a;

					if (b instanceof Long) {
						long intB = (Long) b;

						switch (acceptor.getOperator()) {
							case PLUS:
								result = floatA + intB;
								break;
							case MINUS:
								result = floatA - intB;
								break;
							case ASTERISK:
								result = floatA * intB;
								break;
							case SLASH:
								result = floatA / intB;
								break;
						}
					} else {
						double floatB = (Double) b;

						switch (acceptor.getOperator()) {
							case PLUS:
								result = floatA + floatB;
								break;
							case MINUS:
								result = floatA - floatB;
								break;
							case ASTERISK:
								result = floatA * floatB;
								break;
							case SLASH:
								result = floatA / floatB;
								break;
						}
					}
				}

				break;
		}
	}

	@Override
	public void visit(UnaryExpression acceptor) {
		if (acceptor.getKind() == UnaryExpression.Kind.PREFIX) {
			acceptor.getExpression().accept(this);

			switch (acceptor.getOperator()) {
				case MINUS:
				case BITWISE_NOT:
					long a = (long) result;

					switch (acceptor.getOperator()) {
						case MINUS:
							result = -a;
							break;
						case BITWISE_NOT:
							result = ~a;
							break;
					}

					break;
				case LOGICAL_NOT:
					boolean b = (boolean) result;
					result = !b;
					break;
			}
		}
	}

	@Override
	public void visit(SequenceExpression acceptor) {

	}

	@Override
	public void visit(BlockStatement acceptor) {
		for (int i = 0; i < acceptor.getStatements().size(); i++) {
			acceptor.getStatements().get(i).accept(this);
		}
	}

	@Override
	public void visit(ConditionalExpression acceptor) {

	}

	@Override
	public void visit(Literal acceptor) {
		switch (acceptor.getKind()) {
			case INTEGER:
				result = (long) acceptor.getValue();
				break;
			case FLOAT:
				result = (double) acceptor.getValue();
				break;
			case STRING:
				result = (String) acceptor.getValue();
				break;
			case BOOLEAN:
				result = (boolean) acceptor.getValue();
				break;
			case NIL:
				result = null;
				break;
		}
	}

	@Override
	public void visit(WhileStatement acceptor) {

	}

	@Override
	public void visit(CallExpression acceptor) {

	}

	@Override
	public void visit(FunctionExpression acceptor) {
		for (int i = 0; i < acceptor.getParameters().size(); i++) {
			acceptor.getParameters().get(i).accept(this);
		}

		acceptor.getBody().accept(this);
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

	}

	@Override
	public void visit(MemberExpression acceptor) {

	}

	@Override
	public void visit(AssignmentExpression acceptor) {

	}

}
