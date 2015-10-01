﻿using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Axiom.Internal;
using System.IO;
using Axiom.Internal.Ast;

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

        [TestMethod, TestCategory("Parser")]
        [DeploymentItem("Data/SimpleExpressions.xml")]
        [DataSource("Microsoft.VisualStudio.TestTools.DataSource.XML", "|DataDirectory|\\SimpleExpressions.xml", "Row", DataAccessMethod.Sequential)]
        //[Timeout(100)]
        public void ParseSimpleExpressions()
        {
            var source = TestContext.DataRow["Source"].ToString();
            var parser = new Parser(new Lexer(new StringReader(source)));

            var ast = parser.Parse();
            AstDebug.Debug(ast);

            parser.Dispose();
        }

        [TestMethod, TestCategory("Parser")]
        [DeploymentItem("Data/Functions.xml")]
        [DataSource("Microsoft.VisualStudio.TestTools.DataSource.XML", "|DataDirectory|\\Functions.xml", "Row", DataAccessMethod.Sequential)]
        //[Timeout(100)]
        public void ParseFunctions()
        {
            var source = TestContext.DataRow["Source"].ToString();
            var parser = new Parser(new Lexer(new StringReader(source)));

            var ast = parser.Parse();
            AstDebug.Debug(ast);

            parser.Dispose();
        }
    }
}
