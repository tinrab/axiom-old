import com.moybl.axiom.Lexer;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;

public class LexerTest {

	@Test
	public void testLiterals() {
		Lexer lexer = new Lexer(new ByteArrayInputStream("42 3.14 7E11 \"hi\" false true".getBytes()));

		Assert.assertEquals("42", lexer.next().getLexeme());
		Assert.assertEquals("3.14", lexer.next().getLexeme());
		Assert.assertEquals("7E11", lexer.next().getLexeme());
		Assert.assertEquals("\"hi\"", lexer.next().getLexeme());
		Assert.assertEquals("false", lexer.next().getLexeme());
		Assert.assertEquals("true", lexer.next().getLexeme());
	}

}
