import com.moybl.axiom.Lexer;
import com.moybl.axiom.Parser;
import com.moybl.axiom.ast.Node;
import com.moybl.axiom.frame.*;
import com.moybl.axiom.im.*;
import com.moybl.axiom.semantics.NameChecker;
import com.moybl.axiom.semantics.SymbolMap;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ImTest {

	@Test
	public void testSimple() {
		List<Chunk> chunks = chunksFromSource("1 + 1");

		for (int i = 0; i < chunks.size(); i++) {
			System.out.println("Chunk " + i);
			Chunk chunk = chunks.get(i);
			Frame frame = chunk.getFrame();
			ImStatement code = chunk.getCode();

			System.out.printf("Frame(%s, level = %d)\n", frame.getLabel().getName(), frame.getLevel());

			code.accept(new DebugImVisitor());
		}
	}

	private List<Chunk> chunksFromSource(String source) {
		Parser parser = new Parser(new Lexer(new ByteArrayInputStream(source.getBytes())));
		Node root = parser.parse();
		SymbolMap symbolMap = NameChecker.check(root);
		FrameMap frameMap = FrameEvaluator.evaluate(root, symbolMap);

		return CodeGenerator.generate(root, symbolMap, frameMap);
	}

}
