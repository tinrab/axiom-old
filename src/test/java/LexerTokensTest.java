import com.moybl.axiom.Lexer;
import com.moybl.axiom.Token;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class LexerTokensTest {

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"42", Collections.singletonList(Token.LITERAL_INTEGER)},
				{
						"nil if else for while try catch this base elif is in // comment",
						Arrays.asList(
								Token.KEYWORD_NIL,
								Token.KEYWORD_IF,
								Token.KEYWORD_ELSE,
								Token.KEYWORD_FOR,
								Token.KEYWORD_WHILE,
								Token.KEYWORD_TRY,
								Token.KEYWORD_CATCH,
								Token.KEYWORD_THIS,
								Token.KEYWORD_BASE,
								Token.KEYWORD_ELIF,
								Token.KEYWORD_IS,
								Token.KEYWORD_IN)
				},
				{"true false", Arrays.asList(Token.LITERAL_BOOLEAN, Token.LITERAL_BOOLEAN)},
				{"\"this is a string\"", Arrays.asList(Token.LITERAL_STRING)},
				{"+  -/ *", Arrays.asList(Token.PLUS, Token.MINUS, Token.SLASH, Token.ASTERISK)},
				{
						"% ++ -- :;,.?/*this is a comment*/$#()[]{}",
						Arrays.asList(
								Token.PERCENT,
								Token.INCREMENT,
								Token.DECREMENT,
								Token.COLON,
								Token.SEMI_COLON,
								Token.COMMA,
								Token.DOT,
								Token.QUESTION_MARK,
								Token.DOLLAR,
								Token.HASH,
								Token.OPEN_PARENTHESIS,
								Token.CLOSE_PARENTHESIS,
								Token.OPEN_BRACKET,
								Token.CLOSE_BRACKET,
								Token.OPEN_BRACE,
								Token.CLOSE_BRACE)
				},
				{
						"= += -= *= /=%=&=|=^=<<=>>=>>>=",
						Arrays.asList(
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
						)
				},
				{
						"first_name x 42 3.14 \"hi\" true 7E11",
						Arrays.asList(
								Token.IDENTIFIER,
								Token.IDENTIFIER,
								Token.LITERAL_INTEGER,
								Token.LITERAL_FLOAT,
								Token.LITERAL_STRING,
								Token.LITERAL_BOOLEAN,
								Token.LITERAL_FLOAT
						)
				}
		});
	}

	private String input;
	private List<Token> expected;

	public LexerTokensTest(String input, List<Token> expected) {
		this.input = input;
		this.expected = expected;
	}

	@Test
	public void test() {
		Lexer lexer = new Lexer(new ByteArrayInputStream(input.getBytes()));

		for (int i = 0; i < expected.size(); i++) {
			Assert.assertEquals(expected.get(i), lexer.next().getToken());
		}

		Assert.assertEquals(Token.EOF, lexer.next().getToken());
	}

}
