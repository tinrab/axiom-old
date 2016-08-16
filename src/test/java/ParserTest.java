import com.moybl.axiom.*;
import com.moybl.axiom.vm.StackBasedInterpreter;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;

public class ParserTest {

	@Test
	public void testIntegerArithmeticExpression() {
		StackBasedInterpreter vm = new StackBasedInterpreter();
		Assert.assertEquals(42L, vm.evaluate("-2+ (2*3-(8/2) + 10 * 2) * 2"));
	}

	@Test
	public void testFloatArithmeticExpression() {
		StackBasedInterpreter vm = new StackBasedInterpreter();
		Assert.assertEquals(388701.70370370365, vm.evaluate("1-3+5/9.0E-3*(7E2-1/3.0)"));
	}

	private Parser parserFromSource(String source) {
		Lexer lexer = new Lexer(new ByteArrayInputStream(source.getBytes()));
		return new Parser(lexer);
	}

}
