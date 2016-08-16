package com.moybl.axiom;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Lexer {

	private InputStream stream;
	private int line;
	private int column;

	private static final Map<String, Token> RESERVED_WORDS = new HashMap<String, Token>() {{
		put("nil", Token.KEYWORD_NIL);
		put("if", Token.KEYWORD_IF);
		put("else", Token.KEYWORD_ELSE);
		put("for", Token.KEYWORD_FOR);
		put("while", Token.KEYWORD_WHILE);
		put("try", Token.KEYWORD_TRY);
		put("catch", Token.KEYWORD_CATCH);
		put("true", Token.LITERAL_BOOLEAN);
		put("false", Token.LITERAL_BOOLEAN);
		put("elif", Token.KEYWORD_ELIF);
		put("this", Token.KEYWORD_THIS);
		put("base", Token.KEYWORD_BASE);
		put("delete", Token.KEYWORD_DELETE);
		put("is", Token.KEYWORD_IS);
		put("in", Token.KEYWORD_IN);
	}};
	private static final Map<String, Token> PUNCTUATORS = new HashMap<String, Token>() {{
		put("+", Token.PLUS);
		put("-", Token.MINUS);
		put("*", Token.ASTERISK);
		put("/", Token.SLASH);
		put("%", Token.PERCENT);
		put(":", Token.COLON);
		put(";", Token.SEMI_COLON);
		put(",", Token.COMMA);
		put(".", Token.DOT);
		put("?", Token.QUESTION_MARK);
		put("$", Token.DOLLAR);
		put("#", Token.HASH);
		put("(", Token.OPEN_PARENTHESIS);
		put(")", Token.CLOSE_PARENTHESIS);
		put("[", Token.OPEN_BRACKET);
		put("]", Token.CLOSE_BRACKET);
		put("{", Token.OPEN_BRACE);
		put("}", Token.CLOSE_BRACE);

		put("&&", Token.LOGICAL_AND);
		put("||", Token.LOGICAL_OR);
		put("!", Token.LOGICAL_NOT);
		put("==", Token.LOGICAL_EQUAL);
		put("!=", Token.LOGICAL_NOT_EQUAL);
		put("<", Token.LOGICAL_LESS);
		put(">", Token.LOGICAL_GREATER);
		put("<=", Token.LOGICAL_LESS_OR_EQUAL);
		put(">=", Token.LOGICAL_GREATER_OR_EQUAL);

		put("&", Token.BITWISE_AND);
		put("|", Token.BITWISE_OR);
		put("^", Token.BITWISE_XOR);
		put("~", Token.BITWISE_NOT);
		put("<<", Token.BITWISE_LEFT_SHIFT);
		put(">>", Token.BITWISE_RIGHT_SHIFT);
		put(">>>", Token.BITWISE_UNSIGNED_RIGHT_SHIFT);

		put("=", Token.ASSIGN);
		put("+=", Token.ASSIGN_ADD);
		put("-=", Token.ASSIGN_SUBTRACT);
		put("*=", Token.ASSIGN_MULTIPLY);
		put("/=", Token.ASSIGN_DIVIDE);
		put("%=", Token.ASSIGN_MODULO);
		put("&=", Token.ASSIGN_BITWISE_AND);
		put("|=", Token.ASSIGN_BITWISE_OR);
		put("^=", Token.ASSIGN_BITWISE_XOR);
		put("<<=", Token.ASSIGN_BITWISE_LEFT_SHIFT);
		put(">>=", Token.ASSIGN_BITWISE_RIGHT_SHIFT);
		put(">>>=", Token.ASSIGN_BITWISE_UNSIGNED_RIGHT_SHIFT);
	}};

	public Lexer(InputStream stream) {
		this.stream = stream;
		line = 1;
	}

	public Symbol next() {
		Symbol symbol = null;

		try {
			int ch;

			while ((ch = stream.read()) != -1) {
				column++;

				if (ch == '/') {
					stream.mark(1);
					int next = stream.read();

					if (next == '/') {
						do {
							ch = stream.read();
						} while (ch != '\n' && ch != '\r' && ch != -1);

						line++;
						column = 0;

						continue;
					} else if (next == '*') {
						int p;

						do {
							p = ch;
							ch = stream.read();

							if (ch == -1) {
								throw LexerException.unclosedComment(line, column);
							}

							column++;

							if (ch == '\n' || ch == '\r') {
								line++;
								column = 0;
							}
						} while (p != '*' || ch != '/');

						continue;
					}

					stream.reset();
				}

				if (Character.isDigit(ch)) {
					StringBuilder sb = new StringBuilder();
					Token token = Token.LITERAL_INTEGER;
					int endColumn = column;

					sb.append((char) ch);
					boolean isScientific = false;
					boolean hasPower = false;

					while (true) {
						stream.mark(1);
						int next = stream.read();

						if (Character.isDigit(next)) {
							ch = next;
							sb.append((char) ch);
							endColumn++;

							if(isScientific){
								hasPower = true;
							}
						} else if (next == '.') {
							token = Token.LITERAL_FLOAT;

							ch = next;
							sb.append((char) ch);
							endColumn++;
						} else if(next == 'E'){
							token = Token.LITERAL_FLOAT;
							isScientific = true;

							ch = next;
							sb.append((char) ch);
							endColumn++;
						} else if(next == '-'){
							if(isScientific && !hasPower) {
								ch = next;
								sb.append((char) ch);
								endColumn++;
							} else {
								stream.reset();
								break;
							}
						} else {
							stream.reset();
							break;
						}
					}

					String lexeme = sb.toString();
					symbol = new Symbol(token, lexeme, line, column, endColumn);
					column = endColumn;

					break;
				} else if (Character.isLetter(ch) || ch == '_') {
					StringBuilder sb = new StringBuilder();

					do {
						sb.append((char) ch);
						stream.mark(1);
						ch = stream.read();
					} while (Character.isLetterOrDigit(ch) || ch == '_');

					stream.reset();

					String lexeme = sb.toString();
					Token token = null;

					if (RESERVED_WORDS.containsKey(lexeme)) {
						token = RESERVED_WORDS.get(lexeme);
					} else {
						token = Token.IDENTIFIER;
					}

					int endColumn = column + lexeme.length();
					symbol = new Symbol(token, lexeme, line, column, endColumn);
					column = endColumn - 1;

					break;
				} else if (ch == '"') {
					StringBuilder sb = new StringBuilder();
					int endColumn = column + 1;
					sb.append((char) ch);

					do {
						ch = stream.read();
						endColumn++;
						sb.append((char) ch);

						if (ch == '"') {
							break;
						} else if (ch == -1) {
							throw LexerException.unclosedString(line, column);
						}
					} while (true);

					String lexeme = sb.toString();

					symbol = new Symbol(Token.LITERAL_STRING, lexeme, line, column, endColumn);
					column = endColumn - 1;

					break;
				} else if (!Character.isWhitespace(ch)) {
					String lexeme = Character.toString((char) ch);
					Token token = PUNCTUATORS.get(lexeme);

					while (token != null) {
						stream.mark(1);
						ch = stream.read();
						String newLexeme = lexeme + Character.toString((char) ch);

						if (PUNCTUATORS.containsKey(newLexeme)) {
							token = PUNCTUATORS.get(newLexeme);
							lexeme = newLexeme;
						} else {
							stream.reset();
							break;
						}
					}

					if (token == null) {
						throw LexerException.illegalCharacter(line, column, ch);
					}

					int endColumn = column + lexeme.length();
					symbol = new Symbol(token, lexeme, line, column, endColumn);
					column = endColumn;

					break;
				} else if (ch == '\n' || ch == '\r') {
					line++;
					column = 0;

					stream.mark(1);
					int next = stream.read();

					if (next != '\r' && next != '\n') {
						stream.reset();
					}
				}
			}
		} catch (IOException e) {
			throw LexerException.message(e.getMessage());
		}

		if (symbol == null) {
			symbol = new Symbol(Token.EOF, null, getCurrentPosition());
		}

		return symbol;
	}

	private Position getCurrentPosition() {
		return new Position(line, column);
	}

}
