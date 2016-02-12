using Microsoft.VisualStudio.TestTools.UnitTesting;
using Axiom.Internal;
using System.IO;
using Axiom.Internal.Intermediate;
using Axiom.Internal.Frames;
using Axiom.Internal.Semantics;
using Axiom;

namespace Tests
{
    [TestClass]
    public class InterpreterTests
    {
        private TestContext testContextInstance;
        public TestContext TestContext
        {
            get { return testContextInstance; }
            set { testContextInstance = value; }
        }

        [TestMethod, TestCategory("Interpreter")]
        public void ArithmeticExpression()
        {
            var source = "1-(4  /2+(1*3 ))*2+10";
            var parser = new Parser(new Lexer(new StringReader(source)));

            var ast = parser.Parse();

            ast.Accept(new NameChecker());
            ast.Accept(new FrameEvaluator());

            var cg = new ImCodeGenerator();
            ast.Accept(cg);

            foreach (var chunk in cg.Chunks) {
                chunk.LinearCode = chunk.Code.Linear();
            }

            var inter = new Interpreter(cg.Chunks);
            inter.Run();

            Assert.AreEqual((long)1, inter.GetCompletionValue());

            parser.Dispose();
        }
    }
}
