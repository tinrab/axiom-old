using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Axiom.Internal.Ast;
using Axiom.Internal.Semantics;

namespace Axiom.Internal
{
    internal class AstDebug : IVisitor
    {
        private static AstDebug instance;

        static AstDebug()
        {
            instance = new AstDebug();
        }

        public static void Debug(AstNode tree)
        {
            tree.Accept(instance);
        }

        private int _ident;

        private void Dump(string message)
        {
            for (int i = 0; i < _ident * 4; i++) {
                Console.Write(" ");
            }

            Console.WriteLine(message);
        }

        public void Visit(UnaryExpression acceptor)
        {
            Dump("UnaryExpression(" + acceptor.Operator + ")" + (acceptor.IsPostfix ? " Postfix" : ""));

            _ident++;
            acceptor.Expression.Accept(this);
            _ident--;
        }

        public void Visit(BlockStatement acceptor)
        {
            Dump("Block");

            _ident++;
            foreach (var statement in acceptor.Statements) {
                statement.Accept(this);
            }
            _ident--;
        }

        public void Visit(Literal acceptor)
        {
            Dump(acceptor.Type + "(" + acceptor.Value + ")");
        }

        public void Visit(ReferenceExpression acceptor)
        {
            Dump("ReferenceExpression(" + acceptor.Type + ")");
        }

        public void Visit(ForStatement acceptor)
        {
            Dump("For");

            _ident++;
            acceptor.Condition.Accept(this);
            _ident--;

            if (acceptor.Init != null) {
                Dump("Init");
                _ident++;
                acceptor.Init.Accept(this);
                _ident--;
            }

            if (acceptor.Condition != null) {
                Dump("Condition");
                _ident++;
                acceptor.Condition.Accept(this);
                _ident--;
            }

            if (acceptor.Loop != null) {
                Dump("Loop");
                _ident++;
                acceptor.Loop.Accept(this);
                _ident--;
            }

            if (acceptor.Body != null) {
                Dump("Body");
                _ident++;
                acceptor.Body.Accept(this);
                _ident--;
            }
        }

        public void Visit(MemberExpression acceptor)
        {
            Dump("MemberExpression(" + acceptor.Type + ")");

            Dump("Object");
            _ident++;
            acceptor.Object.Accept(this);
            _ident--;

            Dump("Member");
            _ident++;
            acceptor.Member.Accept(this);
            _ident--;
        }

        public void Visit(AssignmentExpression acceptor)
        {
            Dump("AssignmentExpression(" + acceptor.Operator + ")");

            Dump("Source");
            _ident++;
            acceptor.Source.Accept(this);
            _ident--;

            Dump("Destination");
            _ident++;
            acceptor.Destination.Accept(this);
            _ident--;
        }

        public void Visit(Identifier acceptor)
        {
            if (SymbolDescription.Initializations.ContainsKey(acceptor)) {
                var init = SymbolDescription.Initializations[acceptor];

                Dump("Identifier(" + acceptor.Name + ") #initialized at " + init.Position);
            } else {
                Dump("Identifier(" + acceptor.Name + ")");
            }
        }

        public void Visit(IfStatement acceptor)
        {
            Dump("If");

            _ident++;
            acceptor.Condition.Accept(this);
            _ident--;

            Dump("Then");

            _ident++;
            acceptor.Body.Accept(this);
            _ident--;

            if (acceptor.ElseBody != null) {
                Dump("Else");

                _ident++;
                acceptor.ElseBody.Accept(this);
                _ident--;
            }
        }

        public void Visit(WhileStatement acceptor)
        {
            Dump("While");

            _ident++;
            acceptor.Condition.Accept(this);
            _ident--;

            Dump("Do");
            _ident++;
            acceptor.Body.Accept(this);
            _ident--;
        }

        public void Visit(ConditionalExpression acceptor)
        {
            Dump("ConditionalExpression");

            Dump("Test");
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

        public void Visit(SequenceExpression acceptor)
        {
            Dump("SequenceExpression");

            _ident++;
            foreach (var expr in acceptor.Expressions) {
                expr.Accept(this);
            }
            _ident--;
        }

        public void Visit(BinaryExpression acceptor)
        {
            Dump(acceptor.Operator.ToString());

            _ident++;
            acceptor.Left.Accept(this);
            acceptor.Right.Accept(this);
            _ident--;
        }

        public void Visit(Program acceptor)
        {
            foreach (var statement in acceptor.Statements) {
                statement.Accept(this);
            }
        }

        public void Visit(FunctionExpression acceptor)
        {
            var sig = "Function (";

            foreach (var par in acceptor.Parameters) {
                sig += par.Name + ", ";
            }

            sig = sig.Substring(0, sig.Length - 2) + ")";

            Dump(sig);

            _ident++;
            acceptor.Body.Accept(this);
            _ident--;
        }

        public void Visit(CallExpression acceptor)
        {
            Dump("Call");

            _ident++;
            {
                Dump("Arguments");

                _ident++;
                foreach (var arg in acceptor.Arguments) {
                    arg.Accept(this);
                }
                _ident--;
            }
            {
                Dump("Callee");

                _ident++;
                acceptor.Callee.Accept(this);
                _ident--;
            }
            _ident--;
        }
    }
}
