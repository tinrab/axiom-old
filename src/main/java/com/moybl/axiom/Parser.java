package com.moybl.axiom;

import com.moybl.axiom.ast.AssignmentExpression;
import com.moybl.axiom.ast.BinaryExpression;
import com.moybl.axiom.ast.BlockStatement;
import com.moybl.axiom.ast.CallExpression;
import com.moybl.axiom.ast.ConditionalExpression;
import com.moybl.axiom.ast.Expression;
import com.moybl.axiom.ast.ExpressionStatement;
import com.moybl.axiom.ast.FunctionExpression;
import com.moybl.axiom.ast.Identifier;
import com.moybl.axiom.ast.IfStatement;
import com.moybl.axiom.ast.Literal;
import com.moybl.axiom.ast.MemberExpression;
import com.moybl.axiom.ast.Node;
import com.moybl.axiom.ast.ReferenceExpression;
import com.moybl.axiom.ast.SequenceExpression;
import com.moybl.axiom.ast.Statement;
import com.moybl.axiom.ast.UnaryExpression;
import com.moybl.axiom.ast.WhileStatement;

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

		return new FunctionExpression(
				new ArrayList<Identifier>(),
				new BlockStatement(parseStatements()));
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
		Expression condition = parseExpression();
		Statement body = parseStatement();

		return new WhileStatement(condition, body);
	}

	private Statement parseForStatement() {
		return null;
	}

	private Statement parseIfStatement() {
		check(Token.KEYWORD_IF);
		Expression condition = parseExpression();
		Statement body = parseStatement();

		if (accept(Token.KEYWORD_ELSE)) {
			Statement elseBody = parseStatement();

			return new IfStatement(condition, body, elseBody);
		}

		return new IfStatement(condition, body);
	}

	private Statement parseBlockStatement() {
		check(Token.OPEN_BRACE);
		List<Statement> statements = parseStatementList();
		check(Token.CLOSE_BRACE);

		return new BlockStatement(statements);
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
		Statement statement = new ExpressionStatement(parseExpression());
		accept(Token.SEMI_COLON);

		return statement;
	}

	// Expressions
	private Expression parseExpression() {
		Expression expression = parseAssignmentExpression();

		if (match(Token.COMMA)) {
			List<Expression> expressions = new ArrayList<Expression>();
			expressions.add(expression);

			while (accept(Token.COMMA)) {
				expressions.add(parseExpression());
			}

			return new SequenceExpression(expressions);
		}

		return expression;
	}

	private Expression parseAssignmentExpression() {
		Expression expression = parseConditionalExpression();

		if (accept(ASSIGNMENT_OPERATORS)) {
			Token operator = current.getToken();
			Expression destination = parseAssignmentExpression();

			return new AssignmentExpression(operator, expression, destination);
		}

		return expression;
	}

	private Expression parseConditionalExpression() {
		Expression expression = parseOrExpression();

		if (accept(Token.QUESTION_MARK)) {
			Expression consequent = parseAssignmentExpression();
			check(Token.COLON);
			Expression alternate = parseAssignmentExpression();

			return new ConditionalExpression(expression, consequent, alternate);
		}

		return expression;
	}

	private Expression parseOrExpression() {
		Expression expression = parseAndExpression();

		if (accept(Token.LOGICAL_OR)) {
			return new BinaryExpression(Token.LOGICAL_OR, expression, parseOrExpression());
		}

		return expression;
	}

	private Expression parseAndExpression() {
		Expression expression = parseBitwiseOrExpression();

		if (accept(Token.LOGICAL_AND)) {
			return new BinaryExpression(Token.LOGICAL_AND, expression, parseAndExpression());
		}

		return expression;
	}

	private Expression parseBitwiseOrExpression() {
		Expression expression = parseBitwiseXorExpression();

		if (accept(Token.BITWISE_OR)) {
			return new BinaryExpression(Token.BITWISE_OR, expression, parseBitwiseOrExpression());
		}

		return expression;
	}

	private Expression parseBitwiseXorExpression() {
		Expression expression = parseBitwiseAndExpression();

		if (accept(Token.BITWISE_XOR)) {
			return new BinaryExpression(Token.BITWISE_XOR, expression, parseBitwiseXorExpression());
		}

		return expression;
	}

	private Expression parseBitwiseAndExpression() {
		Expression expression = parseEqualityExpression();

		if (accept(Token.BITWISE_AND)) {
			return new BinaryExpression(Token.BITWISE_AND, expression, parseBitwiseAndExpression());
		}

		return expression;
	}

	private Expression parseEqualityExpression() {
		Expression expression = parseRelationalExpression();

		if (accept(Token.LOGICAL_EQUAL, Token.LOGICAL_NOT_EQUAL)) {
			return new BinaryExpression(current.getToken(), expression, parseEqualityExpression());
		}

		return expression;
	}

	private Expression parseRelationalExpression() {
		Expression expression = parseBitwiseShiftExpression();

		if (accept(RELATIONAL_OPERATORS)) {
			return new BinaryExpression(current.getToken(), expression, parseRelationalExpression());
		}

		return expression;
	}

	private Expression parseBitwiseShiftExpression() {
		Expression expression = parseAdditiveExpression();

		if (accept(Token.BITWISE_LEFT_SHIFT, Token.ASSIGN_BITWISE_RIGHT_SHIFT, Token.ASSIGN_BITWISE_UNSIGNED_RIGHT_SHIFT)) {
			return new BinaryExpression(current.getToken(), expression, parseBitwiseShiftExpression());
		}

		return expression;
	}

	private Expression parseAdditiveExpression() {
		Expression expression = parseMultiplicativeExpression();

		if (accept(Token.PLUS, Token.MINUS)) {
			return new BinaryExpression(current.getToken(), expression, parseAdditiveExpression());
		}

		return expression;
	}

	private Expression parseMultiplicativeExpression() {
		Expression expression = parseUnaryExpression();

		if (accept(Token.ASTERISK, Token.SLASH, Token.PERCENT)) {
			return new BinaryExpression(current.getToken(), expression, parseMultiplicativeExpression());
		}

		return expression;
	}

	private Expression parseUnaryExpression() {
		if (accept(Token.INCREMENT, Token.DECREMENT)) {
			Token token = current.getToken();
			Expression expression = parseUnaryExpression();

			if (!isLeftHandSide(expression)) {
				throw ParserException.illegalLeftHandSide(current.getPosition());
			}

			return new UnaryExpression(token, expression, UnaryExpression.Kind.PREFIX);
		}

		if (accept(Token.MINUS, Token.BITWISE_NOT, Token.LOGICAL_NOT, Token.KEYWORD_DELETE)) {
			Token token = current.getToken();
			Expression expression = parseUnaryExpression();

			return new UnaryExpression(token, expression, UnaryExpression.Kind.PREFIX);
		}

		return parsePostfixExpression();
	}

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

	private Expression parseLeftHandSideExpression() {
		Expression expression = parsePrimaryExpression();

		while (match(Token.DOT, Token.OPEN_BRACKET)) {
			if (match(Token.OPEN_BRACKET)) {
				Expression member = parseComputedMemberExpression();
				expression = new MemberExpression(MemberExpression.Kind.LIST, expression, member);
			} else {
				Expression property = parseNonComputedMemberExpression();
				expression = new MemberExpression(MemberExpression.Kind.PROPERTY, expression, property);
			}
		}

		return expression;
	}

	private Expression parseLeftHandSideExpressionAllowCall() {
		Expression expression = parsePrimaryExpression();

		while (match(Token.DOT, Token.OPEN_BRACKET, Token.OPEN_PARENTHESIS)) {
			if (match(Token.OPEN_PARENTHESIS)) {
				List<Expression> arguments = parseArguments();

				expression = new CallExpression(expression, arguments);
			} else if (match(Token.OPEN_BRACKET)) {
				Expression member = parseComputedMemberExpression();
				expression = new MemberExpression(MemberExpression.Kind.LIST, expression, member);
			} else {
				Expression property = parseNonComputedMemberExpression();
				expression = new MemberExpression(MemberExpression.Kind.PROPERTY, expression, property);
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

		return new Identifier(current.getLexeme());
	}

	private Expression parsePrimaryExpression() {
		if (accept(Token.OPEN_PARENTHESIS)) {
			Expression expression = parseExpression();
			check(Token.CLOSE_PARENTHESIS);

			return expression;
		}

		if (accept(Token.IDENTIFIER)) {
			return new Identifier(current.getLexeme());
		}

		if (accept(Token.KEYWORD_THIS)) {
			return new ReferenceExpression(ReferenceExpression.Kind.THIS);
		}

		if (accept(Token.KEYWORD_BASE)) {
			return new ReferenceExpression(ReferenceExpression.Kind.BASE);
		}

		if (accept(Token.LITERAL_INTEGER)) {
			return new Literal(Literal.Kind.INTEGER, Long.parseLong(current.getLexeme()));
		}

		if (accept(Token.LITERAL_FLOAT)) {
			return new Literal(Literal.Kind.FLOAT, Double.parseDouble(current.getLexeme()));
		}

		if (accept(Token.LITERAL_STRING)) {
			return new Literal(Literal.Kind.STRING, current.getLexeme());
		}

		if (accept(Token.LITERAL_BOOLEAN)) {
			return new Literal(Literal.Kind.BOOLEAN, current.getLexeme().equals("true"));
		}

		if (accept(Token.KEYWORD_NIL)) {
			return new Literal(Literal.Kind.NIL, null);
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
		check(Token.DOLLAR, Token.OPEN_PARENTHESIS);
		List<Identifier> parameters = parseParameters();
		check(Token.CLOSE_PARENTHESIS);
		Statement body = parseStatement();

		return new FunctionExpression(parameters, body);
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
			parameters.add(new Identifier(current.getLexeme()));

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
