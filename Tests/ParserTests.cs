using System;
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
            ast.Accept(new DebugVisitor());

            parser.Dispose();
        }

        private class DebugVisitor : IVisitor
        {
            private int _ident;

            public void Visit(Program acceptor)
            {
                foreach (var statement in acceptor.Statements) {
                    statement.Accept(this);
                }
            }

            public void Visit(Literal acceptor)
            {
                Dump(string.Format("{0}({1})", acceptor.Type, acceptor.Value));
            }

            public void Visit(CompoundStatement acceptor)
            {
                // TODO
            }

            public void Visit(ReferenceExpression acceptor)
            {
                Dump("Reference " + acceptor.Type);
            }

            public void Visit(ConditionalExpression acceptor)
            {
                Dump("Conditional");
                _ident++;
                acceptor.Test.Accept(this);
                _ident--;
                Dump("Consequent");
                _ident++;
                acceptor.Consequent.Accept(this);
                _ident--;
                Dump("Alternate");
                _ident++;
                acceptor.Alternate.Accept(this);
                _ident--;
            }

            public void Visit(AssignmentExpression acceptor)
            {
                Dump("Assignment " + acceptor.Operator);
                _ident++;
                acceptor.Source.Accept(this);
                _ident--;
                Dump("To ");
                _ident++;
                acceptor.Destination.Accept(this);
                _ident--;
            }

            public void Visit(MemberExpression acceptor)
            {
                Dump("Member " + acceptor.Type);

                _ident++;
                acceptor.Object.Accept(this);
                acceptor.Member.Accept(this);
                _ident--;
            }

            public void Visit(SequenceExpression acceptor)
            {
                Dump("Sequence");

                _ident++;
                foreach (var expr in acceptor.Expressions) {
                    expr.Accept(this);
                }
                _ident--;
            }

            public void Visit(Identifier acceptor)
            {
                Dump("Identifier " + acceptor.Name);
            }

            public void Visit(UnaryExpression acceptor)
            {
                Dump("Unary " + acceptor.Operator + (acceptor.IsPostfix ? " Postfix" : ""));

                _ident++;
                acceptor.Expression.Accept(this);
                _ident--;
            }

            public void Visit(BinaryExpression acceptor)
            {
                Dump("Binary " + acceptor.Operator);

                _ident++;
                acceptor.Left.Accept(this);
                acceptor.Right.Accept(this);
                _ident--;
            }

            private void Dump(object message)
            {
                for (int i = 0; i < _ident * 8; i++) {
                    Console.Write(" ");
                }

                Console.WriteLine(message);
            }
        }
    }
}
