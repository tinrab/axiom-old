using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Axiom.Internal;
using System.IO;

namespace Tests
{
    [TestClass]
    public class ParserTests
    {
        private TestContext testContextInstance;
        public TestContext TestContext
        {
            get { return testContextInstance; }
            set { testContextInstance = value; }
        }

        [TestMethod]
        [DeploymentItem("Data/SimpleExpressions.xml")]
        [DataSource("Microsoft.VisualStudio.TestTools.DataSource.XML", "|DataDirectory|\\SimpleExpressions.xml", "Row", DataAccessMethod.Sequential)]
        [Timeout(500)]
        public void ParseSimpleExpressions()
        {
            var source = TestContext.DataRow["Source"].ToString();
            var parser = new Parser(new Lexer(new StringReader(source)));

            parser.Parse();

            parser.Dispose();
        }
    }
}
