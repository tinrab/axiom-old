package com.moybl.axiom;

public enum Token {
	EOF,
	IDENTIFIER,

	LITERAL_INTEGER,
	LITERAL_FLOAT,
	LITERAL_STRING,
	LITERAL_BOOLEAN,

	PLUS,
	MINUS,
	ASTERISK,
	SLASH,
	PERCENT,
	INCREMENT,
	DECREMENT,
	COLON,
	SEMI_COLON,
	COMMA,
	DOT,
	QUESTION_MARK,
	DOLLAR,
	HASH,
	OPEN_PARENTHESIS,
	CLOSE_PARENTHESIS,
	OPEN_BRACKET,
	CLOSE_BRACKET,
	OPEN_BRACE,
	CLOSE_BRACE,

	LOGICAL_AND,
	LOGICAL_OR,
	LOGICAL_NOT,
	LOGICAL_EQUAL,
	LOGICAL_NOT_EQUAL,
	LOGICAL_LESS,
	LOGICAL_GREATER,
	LOGICAL_LESS_OR_EQUAL,
	LOGICAL_GREATER_OR_EQUAL,

	BITWISE_AND,
	BITWISE_OR,
	BITWISE_XOR,
	BITWISE_NOT,
	BITWISE_LEFT_SHIFT,
	BITWISE_RIGHT_SHIFT,
	BITWISE_UNSIGNED_RIGHT_SHIFT,

	ASSIGN,
	ASSIGN_ADD,
	ASSIGN_SUBTRACT,
	ASSIGN_MULTIPLY,
	ASSIGN_DIVIDE,
	ASSIGN_MODULO,
	ASSIGN_BITWISE_AND,
	ASSIGN_BITWISE_OR,
	ASSIGN_BITWISE_XOR,
	ASSIGN_BITWISE_LEFT_SHIFT,
	ASSIGN_BITWISE_RIGHT_SHIFT,
	ASSIGN_BITWISE_UNSIGNED_RIGHT_SHIFT,

	KEYWORD_NIL,
	KEYWORD_IF,
	KEYWORD_ELSE,
	KEYWORD_ELIF,
	KEYWORD_FOR,
	KEYWORD_WHILE,
	KEYWORD_TRY,
	KEYWORD_CATCH,
	KEYWORD_THIS,
	KEYWORD_BASE,
	KEYWORD_DELETE,
	KEYWORD_IS,
	KEYWORD_IN
}
