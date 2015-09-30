using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Axiom.Internal;
using Axiom.Internal.Semantics;
using System.IO;

namespace Tests
{
    [TestClass]
    public class SemanticsTests
    {
        private TestContext testContextInstance;
        public TestContext TestContext
        {
            get { return testContextInstance; }
            set { testContextInstance = value; }
        }

        [TestMethod, TestCategory("Semantics")]
        [DeploymentItem("Data/Initialization.xml")]
        [DataSource("Microsoft.VisualStudio.TestTools.DataSource.XML", "|DataDirectory|\\Initialization.xml", "Row", DataAccessMethod.Sequential)]
        //[Timeout(100)]
        public void TestMethod1()
        {
            var source = TestContext.DataRow["Source"].ToString();
            var parser = new Parser(new Lexer(new StringReader(source)));

            var ast = parser.Parse();
            ast.Accept(new NameChecker());

            AstDebug.Debug(ast);

            parser.Dispose();
        }
    }
}
