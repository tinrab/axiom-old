import com.moybl.axiom.Axiom;
import com.moybl.axiom.semantics.SemanticException;

import org.junit.Test;

public class SemanticsTest {

	@Test(expected = SemanticException.class)
	public void testUndefined() {
		Axiom.parse("x");
	}

	@Test
	public void testDefined() {
		Axiom.parse("x = 42; x");
	}

}
