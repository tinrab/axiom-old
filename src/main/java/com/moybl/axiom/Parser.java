package com.moybl.axiom;

import com.moybl.axiom.ast.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {

	private static final Token[] ASSIGNMENT_OPERATORS = new Token[]{
			Token.ASSIGN,
			Token.ASSIGN_ADD,
			Token.ASSIGN_SUBTRACT,
			Token.ASSIGN_MULTIPLY,
			Token.ASSIGN_DIVIDE,
			Token.ASSIGN_MODULO,
			Token.ASSIGN_BITWISE_AND,
			Token.ASSIGN_BITWISE_OR,
			Token.ASSIGN_BITWISE_XOR,
			Token.ASSIGN_BITWISE_LEFT_SHIFT,
			Token.ASSIGN_BITWISE_RIGHT_SHIFT,
			Token.ASSIGN_BITWISE_UNSIGNED_RIGHT_SHIFT
	};
	private static final Token[] RELATIONAL_OPERATORS = new Token[]{
			Token.LOGICAL_LESS,
			Token.LOGICAL_LESS_OR_EQUAL,
			Token.LOGICAL_GREATER,
			Token.LOGICAL_GREATER_OR_EQUAL,
			Token.LOGICAL_EQUAL,
			Token.LOGICAL_NOT_EQUAL,
	};

	private Lexer lexer;
	private Symbol current;
	private Symbol next;

	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}

	public Node parse() {
		next = lexer.next();

		List<Statement> statements = parseStatements();
		Position p = Position.expand(statements.get(0).getPosition(), statements.get(statements.size() - 1).getPosition());

		return new BlockStatement(p, statements);
	}

	private List<Statement> parseStatements() {
		List<Statement> statements = new ArrayList<Statement>();

		while (!match(Token.EOF)) {
			Statement statement = parseStatement();

			if (statement != null) {
				statements.add(statement);
			}
		}

		return statements;
	}

	private Statement parseStatement() {
		switch (next.getToken()) {
			case OPEN_BRACE:
				return parseBlockStatement();
			case OPEN_PARENTHESIS:
				return parseExpressionStatement();
			case KEYWORD_IF:
				return parseIfStatement();
			case KEYWORD_WHILE:
				return parseWhileStatement();
			case KEYWORD_FOR:
				return parseForStatement();
			case SEMI_COLON:
				accept(Token.SEMI_COLON);
				return null;
		}

		return parseExpressionStatement();
	}

	private Statement parseWhileStatement() {
		check(Token.KEYWORD_WHILE);
		Position a = current.getPosition();
		Expression condition = parseExpression();
		Statement body = parseStatement();
		Position b = current.getPosition();

		return new WhileStatement(Position.expand(a, b), condition, body);
	}

	private Statement parseForStatement() {
		return null;
	}

	private Statement parseIfStatement() {
		check(Token.KEYWORD_IF);
		Position a = current.getPosition();
		Expression condition = parseExpression();
		Statement body = parseStatement();

		if (accept(Token.KEYWORD_ELSE)) {
			Statement elseBody = parseStatement();
			Position b = current.getPosition();

			return new IfStatement(Position.expand(a, b), condition, body, elseBody);
		}

		Position b = current.getPosition();

		return new IfStatement(Position.expand(a, b), condition, body);
	}

	private Statement parseBlockStatement() {
		check(Token.OPEN_BRACE);
		Position a = current.getPosition();
		List<Statement> statements = parseStatementList();
		check(Token.CLOSE_BRACE);
		Position b = current.getPosition();

		return new BlockStatement(Position.expand(a, b), statements);
	}

	private List<Statement> parseStatementList() {
		List<Statement> list = new ArrayList<Statement>();

		while (true) {
			if (match(Token.CLOSE_BRACE)) {
				break;
			}

			Statement statement = parseStatement();

			if (statement == null) {
				break;
			}

			list.add(statement);
		}

		return list;
	}

	private Statement parseExpressionStatement() {
		Expression expression = parseExpression();
		Statement statement = new ExpressionStatement(expression.getPosition(), expression);
		accept(Token.SEMI_COLON);

		return statement;
	}

	// Expressions
	private Expression parseExpression() {
		Expression expression = parseAssignmentExpression();
		Position a = expression.getPosition();

		if (match(Token.COMMA)) {
			List<Expression> expressions = new ArrayList<Expression>();
			expressions.add(expression);

			while (accept(Token.COMMA)) {
				expressions.add(parseExpression());
			}

			Position b = current.getPosition();

			return new SequenceExpression(Position.expand(a, b), expressions);
		}

		return expression;
	}

	private Expression parseAssignmentExpression() {
		Expression expression = parseConditionalExpression();
		Position a = expression.getPosition();

		if (accept(ASSIGNMENT_OPERATORS)) {
			Token operator = current.getToken();
			Expression source = parseAssignmentExpression();

			return new AssignmentExpression(Position.expand(a, expression.getPosition()), operator, source, expression);
		}

		return expression;
	}

	private Expression parseConditionalExpression() {
		Expression expression = parseOrExpression();
		Position a = expression.getPosition();

		if (accept(Token.QUESTION_MARK)) {
			Expression consequent = parseAssignmentExpression();
			check(Token.COLON);
			Expression alternate = parseAssignmentExpression();

			return new ConditionalExpression(Position.expand(a, alternate.getPosition()), expression, consequent, alternate);
		}

		return expression;
	}

	private Expression parseOrExpression() {
		Expression expression = parseAndExpression();
		Position a = expression.getPosition();

		if (accept(Token.LOGICAL_OR)) {
			Expression right = parseOrExpression();
			return new BinaryExpression(Position.expand(a, right.getPosition()), Token.LOGICAL_OR, expression, right);
		}

		return expression;
	}

	private Expression parseAndExpression() {
		Expression expression = parseBitwiseOrExpression();
		Position a = expression.getPosition();

		if (accept(Token.LOGICAL_AND)) {
			Expression right = parseAndExpression();
			return new BinaryExpression(Position.expand(a, right.getPosition()), Token.LOGICAL_AND, expression, right);
		}

		return expression;
	}

	private Expression parseBitwiseOrExpression() {
		Expression expression = parseBitwiseXorExpression();
		Position a = expression.getPosition();

		if (accept(Token.BITWISE_OR)) {
			Expression right = parseBitwiseOrExpression();
			return new BinaryExpression(Position.expand(a, right.getPosition()), Token.BITWISE_OR, expression, right);
		}

		return expression;
	}

	private Expression parseBitwiseXorExpression() {
		Expression expression = parseBitwiseAndExpression();
		Position a = expression.getPosition();

		if (accept(Token.BITWISE_XOR)) {
			Expression right = parseBitwiseXorExpression();
			return new BinaryExpression(Position.expand(a, right.getPosition()), Token.BITWISE_XOR, expression, right);
		}

		return expression;
	}

	private Expression parseBitwiseAndExpression() {
		Expression expression = parseEqualityExpression();
		Position a = expression.getPosition();

		if (accept(Token.BITWISE_AND)) {
			Expression right = parseBitwiseAndExpression();
			return new BinaryExpression(Position.expand(a, right.getPosition()), Token.BITWISE_AND, expression, right);
		}

		return expression;
	}

	private Expression parseEqualityExpression() {
		Expression expression = parseRelationalExpression();
		Position a = expression.getPosition();

		if (accept(Token.LOGICAL_EQUAL, Token.LOGICAL_NOT_EQUAL)) {
			Expression right = parseEqualityExpression();
			return new BinaryExpression(Position.expand(a, right.getPosition()), current.getToken(), expression, right);
		}

		return expression;
	}

	private Expression parseRelationalExpression() {
		Expression expression = parseBitwiseShiftExpression();
		Position a = expression.getPosition();

		if (accept(RELATIONAL_OPERATORS)) {
			Expression right = parseRelationalExpression();
			return new BinaryExpression(Position.expand(a, right.getPosition()), current.getToken(), expression, right);
		}

		return expression;
	}

	private Expression parseBitwiseShiftExpression() {
		Expression expression = parseAdditiveExpression();
		Position a = expression.getPosition();

		if (accept(Token.BITWISE_LEFT_SHIFT, Token.ASSIGN_BITWISE_RIGHT_SHIFT, Token.ASSIGN_BITWISE_UNSIGNED_RIGHT_SHIFT)) {
			Expression right = parseBitwiseShiftExpression();
			return new BinaryExpression(Position.expand(a, right.getPosition()), current.getToken(), expression, right);
		}

		return expression;
	}

	private Expression parseAdditiveExpression() {
		Expression expression = parseMultiplicativeExpression();
		Position a = expression.getPosition();

		while (true) {
			if (accept(Token.PLUS, Token.MINUS)) {
				Expression right = parseMultiplicativeExpression();
				expression = new BinaryExpression(Position.expand(a, right.getPosition()), current.getToken(), expression, right);
			} else {
				break;
			}
		}

		return expression;
	}

	private Expression parseMultiplicativeExpression() {
		Expression expression = parseUnaryExpression();
		Position a = expression.getPosition();

		while (true) {
			if (accept(Token.ASTERISK, Token.SLASH, Token.PERCENT)) {
				Expression right = parseUnaryExpression();
				expression = new BinaryExpression(Position.expand(a, right.getPosition()), current.getToken(), expression, right);
			} else {
				break;
			}
		}

		return expression;
	}

	private Expression parseUnaryExpression() {
		if (accept(Token.MINUS, Token.BITWISE_NOT, Token.LOGICAL_NOT, Token.KEYWORD_DELETE)) {
			Position a = current.getPosition();
			Token token = current.getToken();
			Expression expression = parseUnaryExpression();

			return new UnaryExpression(Position.expand(a, expression.getPosition()), token, expression, UnaryExpression.Kind.PREFIX);
		}

		return parseLeftHandSideExpressionAllowCall();
	}

	/*
	private Expression parsePostfixExpression() {
		Expression expression = parseLeftHandSideExpressionAllowCall();

		while (true) {
			if (match(Token.INCREMENT, Token.DECREMENT) && next.getToken() != Token.SEMI_COLON) {
				if (!isLeftHandSide(expression)) {
					throw ParserException.illegalLeftHandSide(current.getPosition());
				}

				accept(Token.INCREMENT, Token.DECREMENT);

				expression = new UnaryExpression(current.getToken(), expression, UnaryExpression.Kind.POSTFIX);
			} else {
				break;
			}
		}

		return expression;
	}
	*/

	private Expression parseLeftHandSideExpression() {
		Expression expression = parsePrimaryExpression();
		Position a = expression.getPosition();

		while (match(Token.DOT, Token.OPEN_BRACKET)) {
			if (match(Token.OPEN_BRACKET)) {
				Expression member = parseComputedMemberExpression();
				expression = new MemberExpression(Position.expand(a, member.getPosition()), MemberExpression.Kind.LIST, expression, member);
			} else {
				Expression property = parseNonComputedMemberExpression();
				expression = new MemberExpression(Position.expand(a, property.getPosition()), MemberExpression.Kind.PROPERTY, expression, property);
			}
		}

		return expression;
	}

	private Expression parseLeftHandSideExpressionAllowCall() {
		Expression expression = parsePrimaryExpression();
		Position a = expression.getPosition();

		while (match(Token.DOT, Token.OPEN_BRACKET, Token.OPEN_PARENTHESIS)) {
			if (match(Token.OPEN_PARENTHESIS)) {
				List<Expression> arguments = parseArguments();
				Position b = current.getPosition();

				if (arguments.size() != 0) {
					b = arguments.get(arguments.size() - 1).getPosition();
				}

				expression = new CallExpression(Position.expand(a, b), expression, arguments);
			} else if (match(Token.OPEN_BRACKET)) {
				Expression member = parseComputedMemberExpression();
				expression = new MemberExpression(Position.expand(a, member.getPosition()), MemberExpression.Kind.LIST, expression, member);
			} else {
				Expression property = parseNonComputedMemberExpression();
				expression = new MemberExpression(Position.expand(a, property.getPosition()), MemberExpression.Kind.PROPERTY, expression, property);
			}
		}

		return expression;
	}

	private Expression parseComputedMemberExpression() {
		check(Token.OPEN_BRACKET);
		Expression expression = parseExpression();
		check(Token.CLOSE_BRACKET);

		return expression;
	}

	private Expression parseNonComputedMemberExpression() {
		check(Token.DOT);

		return parseNonComputedPropertyExpression();
	}

	private Expression parseNonComputedPropertyExpression() {
		check(Token.IDENTIFIER);

		return new Identifier(current.getPosition(), current.getLexeme());
	}

	private Expression parsePrimaryExpression() {
		if (accept(Token.OPEN_PARENTHESIS)) {
			Expression expression = parseExpression();
			check(Token.CLOSE_PARENTHESIS);

			return expression;
		}

		if (accept(Token.IDENTIFIER)) {
			return new Identifier(current.getPosition(), current.getLexeme());
		}

		if (accept(Token.KEYWORD_THIS)) {
			return new ReferenceExpression(current.getPosition(), ReferenceExpression.Kind.THIS);
		}

		if (accept(Token.KEYWORD_BASE)) {
			return new ReferenceExpression(current.getPosition(), ReferenceExpression.Kind.BASE);
		}

		if (accept(Token.LITERAL_INTEGER)) {
			return new Literal(current.getPosition(), Literal.Kind.INTEGER, Long.parseLong(current.getLexeme()));
		}

		if (accept(Token.LITERAL_FLOAT)) {
			return new Literal(current.getPosition(), Literal.Kind.FLOAT, Double.parseDouble(current.getLexeme()));
		}

		if (accept(Token.LITERAL_STRING)) {
			return new Literal(current.getPosition(), Literal.Kind.STRING, current.getLexeme().substring(1, current.getLexeme().length() - 2));
		}

		if (accept(Token.LITERAL_BOOLEAN)) {
			return new Literal(current.getPosition(), Literal.Kind.BOOLEAN, current.getLexeme().equals("true"));
		}

		if (accept(Token.KEYWORD_NIL)) {
			return new Literal(current.getPosition(), Literal.Kind.NIL, null);
		}

		if (match(Token.DOLLAR)) {
			return parseFunctionExpression();
		}

		if (match(Token.HASH)) {
			return parseClassExpression();
		}

		if (match(Token.OPEN_BRACE)) {
			return parseObjectInitializer();
		}

		throw ParserException.internal();
	}

	private Expression parseFunctionExpression() {
		check(Token.DOLLAR);
		Position a = current.getPosition();
		check(Token.OPEN_PARENTHESIS);
		List<Identifier> parameters = parseParameters();
		check(Token.CLOSE_PARENTHESIS);
		Statement body = parseStatement();
		Position p = null;

		if (body == null) {
			p = current.getPosition();
		} else {
			p = Position.expand(a, body.getPosition());
		}

		return new FunctionExpression(p, parameters, body);
	}

	private Expression parseClassExpression() {
		return null;
	}

	private Expression parseObjectInitializer() {
		return null;
	}

	private List<Expression> parseArguments() {
		List<Expression> arguments = new ArrayList<Expression>();

		while (true) {
			if (match(Token.CLOSE_PARENTHESIS)) {
				break;
			}

			arguments.add(parseAssignmentExpression());

			if (!accept(Token.COMMA)) {
				break;
			}
		}

		return arguments;
	}

	private List<Identifier> parseParameters() {
		List<Identifier> parameters = new ArrayList<Identifier>();

		while (accept(Token.IDENTIFIER)) {
			parameters.add(new Identifier(current.getPosition(), current.getLexeme()));

			if (!accept(Token.COMMA)) {
				break;
			}
		}

		return parameters;
	}

	private boolean match(Token token) {
		return next.getToken() == token;
	}

	private boolean match(Token... tokens) {
		for (int i = 0; i < tokens.length; i++) {
			if (match(tokens[i])) {
				return true;
			}
		}

		return false;
	}

	private void check(Token token) {
		if (match(token)) {
			current = next;
			next = lexer.next();
		} else {
			throw ParserException.unexpected(current.getPosition(), token, next.getToken());
		}
	}

	private void check(Token... tokens) {
		for (int i = 0; i < tokens.length; i++) {
			check(tokens[i]);
		}
	}

	private boolean accept(Token token) {
		if (match(token)) {
			current = next;
			next = lexer.next();

			return true;
		}

		return false;
	}

	private boolean accept(Token... tokens) {
		for (int i = 0; i < tokens.length; i++) {
			if (accept(tokens[i])) {
				return true;
			}
		}

		return false;
	}

	private boolean isLeftHandSide(Expression expression) {
		return expression instanceof Identifier || expression instanceof MemberExpression;
	}

}
