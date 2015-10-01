using System;
using System.Text;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Axiom.Internal;
using System.IO;
using Axiom.Internal.Frames;

namespace Tests
{
    [TestClass]
    public class FrameTests
    {
        private TestContext testContextInstance;
        public TestContext TestContext
        {
            get { return testContextInstance; }
            set { testContextInstance = value; }
        }

        [TestMethod, TestCategory("Frames")]
        [DeploymentItem("Data/Frames.xml")]
        [DataSource("Microsoft.VisualStudio.TestTools.DataSource.XML", "|DataDirectory|\\Frames.xml", "Row", DataAccessMethod.Sequential)]
        //[Timeout(100)]
        public void TestMethod1()
        {
            var source = TestContext.DataRow["Source"].ToString();
            var parser = new Parser(new Lexer(new StringReader(source)));

            var ast = parser.Parse();
            ast.Accept(new FrameEvaluator());

            AstDebug.Debug(ast);

            parser.Dispose();
        }
    }
}
