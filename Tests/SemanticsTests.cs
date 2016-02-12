using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Axiom.Internal;
using Axiom.Internal.Semantics;
using System.IO;
using Axiom;

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

        [TestMethod, TestCategory("Semantics"), ExpectedException(typeof(Error))]
        public void NotInitialized()
        {
            var source = "x";
            var parser = new Parser(new Lexer(new StringReader(source)));

            var ast = parser.Parse();
            ast.Accept(new NameChecker());

            parser.Dispose();
        }
    }
}
