import com.moybl.axiom.*;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;

public class LexerTest {

	@Test
	public void testLiterals() {
		Lexer lexer = new Lexer(new ByteArrayInputStream("3 3.0 3E2 3.0E2 3E-2 3.0E-2 7E2-1 \"hi\" false true".getBytes()));

		Symbol s = lexer.next();
		Assert.assertEquals("3", s.getLexeme());
		Assert.assertEquals(Token.LITERAL_INTEGER, s.getToken());
		s = lexer.next();
		Assert.assertEquals("3.0", s.getLexeme());
		Assert.assertEquals(Token.LITERAL_FLOAT, s.getToken());
		s = lexer.next();
		Assert.assertEquals("3E2", s.getLexeme());
		Assert.assertEquals(Token.LITERAL_FLOAT, s.getToken());
		s = lexer.next();
		Assert.assertEquals("3.0E2", s.getLexeme());
		Assert.assertEquals(Token.LITERAL_FLOAT, s.getToken());
		s = lexer.next();
		Assert.assertEquals("3E-2", s.getLexeme());
		Assert.assertEquals(Token.LITERAL_FLOAT, s.getToken());
		s = lexer.next();
		Assert.assertEquals("3.0E-2", s.getLexeme());
		Assert.assertEquals(Token.LITERAL_FLOAT, s.getToken());

		s = lexer.next();
		Assert.assertEquals("7E2", s.getLexeme());
		Assert.assertEquals(Token.LITERAL_FLOAT, s.getToken());
		s = lexer.next();
		Assert.assertEquals(Token.MINUS, s.getToken());
		s = lexer.next();
		Assert.assertEquals(Token.LITERAL_INTEGER, s.getToken());

		s = lexer.next();
		Assert.assertEquals("\"hi\"", s.getLexeme());
		Assert.assertEquals(Token.LITERAL_STRING, s.getToken());
		s = lexer.next();
		Assert.assertEquals("false", s.getLexeme());
		Assert.assertEquals(Token.LITERAL_BOOLEAN, s.getToken());
		s = lexer.next();
		Assert.assertEquals("true", s.getLexeme());
		Assert.assertEquals(Token.LITERAL_BOOLEAN, s.getToken());
	}

}
