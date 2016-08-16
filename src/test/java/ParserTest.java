import com.moybl.axiom.Lexer;
import com.moybl.axiom.Parser;

import org.junit.Test;

import java.io.ByteArrayInputStream;

public class ParserTest {

	@Test
	public void testSimple() {
		Parser parser = parserFromSource("2 * 3 + 1");
		parser.parse().accept(new DebugAstVisitor());
	}

	private Parser parserFromSource(String source) {
		Lexer lexer = new Lexer(new ByteArrayInputStream(source.getBytes()));
		return new Parser(lexer);
	}

}
