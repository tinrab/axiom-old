using System;
using System.Text;
using System.Collections.Generic;
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
        [DeploymentItem("Data/SimpleExpressions.xml")]
        [DataSource("Microsoft.VisualStudio.TestTools.DataSource.XML", "|DataDirectory|\\SimpleExpressions.xml", "Row", DataAccessMethod.Sequential)]
        //[Timeout(100)]
        public void ParseSimpleExpressions()
        {
            var source = TestContext.DataRow["Source"].ToString();
            var parser = new Parser(new Lexer(new StringReader(source)));

            var ast = parser.Parse();

            ast.Accept(new NameChecker());
            ast.Accept(new FrameEvaluator());

            Console.WriteLine("AST");
            AstDebug.Debug(ast);

            var cg = new ImCodeGenerator();
            ast.Accept(cg);

            var sw = new StringWriter();

            foreach (var chunk in cg.Chunks) {
                chunk.Dump(sw);
                chunk.LinearCode = chunk.Code.Linear();
            }

            Console.Write(sw.ToString());

            Console.WriteLine("INTERPRETER");
            var inter = new Interpreter(cg.Chunks);
            inter.Run();

            parser.Dispose();
        }

        [TestMethod, TestCategory("Interpreter")]
        [DeploymentItem("Data/Variables.xml")]
        [DataSource("Microsoft.VisualStudio.TestTools.DataSource.XML", "|DataDirectory|\\Variables.xml", "Row", DataAccessMethod.Sequential)]
        public void Variables()
        {
            var source = TestContext.DataRow["Source"].ToString();
            var parser = new Parser(new Lexer(new StringReader(source)));

            var ast = parser.Parse();

            ast.Accept(new NameChecker());
            ast.Accept(new FrameEvaluator());

            AstDebug.Debug(ast);

            Console.WriteLine("IMCODE");

            var cg = new ImCodeGenerator();
            ast.Accept(cg);

            var sw = new StringWriter();

            foreach (var chunk in cg.Chunks) {
                chunk.LinearCode = chunk.Code.Linear();
                chunk.LinearCode.Dump(sw, 0);
            }

            Console.Write(sw.ToString());

            Console.WriteLine("INTERPRETER");
            var inter = new Interpreter(cg.Chunks);
            inter.Run();

            parser.Dispose();
        }
    }
}
