using System;
using System.Collections.Generic;
using System.Linq;
using Axiom.Internal.Semantics;
using Axiom.Internal.Ast;

namespace Axiom.Internal.Frames
{
    internal class FrameEvaluator : IVisitor
    {
        private Frame _frame;
        private int _scope;

        public void Visit(FunctionExpression acceptor)
        {
        }

        public void Visit(CallExpression acceptor)
        {
        }

        public void Visit(AssignmentExpression acceptor)
        {
            if (acceptor.Source is FunctionExpression) {
                var expr = (FunctionExpression)acceptor.Source;
                Frame frame = new Frame(Label.Create(((Identifier)acceptor.Destination).Name), expr, _scope);

                _scope++;

                foreach (var par in expr.Parameters) {
                    var access = new Access(par, frame);

                    FrameDescription.Accesses[SymbolDescription.Initializations[par]] = access;

                    par.Accept(this);
                    frame.Variables.AddLast(access);
                }

                _frame = frame;

                expr.Body.Accept(this);
                FrameDescription.Frames[expr] = frame;

                _scope--;
            }
        }

        public void Visit(Identifier acceptor)
        {
        }

        public void Visit(ReferenceExpression acceptor)
        {
        }

        public void Visit(MemberExpression acceptor)
        {
            acceptor.Object.Accept(this);
            acceptor.Member.Accept(this);
        }

        public void Visit(UnaryExpression acceptor)
        {
            acceptor.Expression.Accept(this);
        }

        public void Visit(BlockStatement acceptor)
        {
            foreach (var statement in acceptor.Statements) {
                statement.Accept(this);
            }
        }

        public void Visit(Literal acceptor) { }

        public void Visit(ForStatement acceptor)
        {
            acceptor.Init.Accept(this);
            acceptor.Condition.Accept(this);
            acceptor.Loop.Accept(this);
            acceptor.Body.Accept(this);
        }

        public void Visit(IfStatement acceptor)
        {
            acceptor.Condition.Accept(this);
            acceptor.Body.Accept(this);

            if (acceptor.ElseBody != null) {
                acceptor.ElseBody.Accept(this);
            }
        }

        public void Visit(WhileStatement acceptor)
        {
            acceptor.Condition.Accept(this);
            acceptor.Body.Accept(this);
        }

        public void Visit(ConditionalExpression acceptor)
        {
            acceptor.Test.Accept(this);
            acceptor.Consequent.Accept(this);
            acceptor.Alternate.Accept(this);
        }

        public void Visit(SequenceExpression acceptor)
        {
            foreach (var expr in acceptor.Expressions) {
                expr.Accept(this);
            }
        }

        public void Visit(BinaryExpression acceptor)
        {
            acceptor.Left.Accept(this);
            acceptor.Right.Accept(this);
        }

        public void Visit(Program acceptor)
        {
            foreach (var statement in acceptor.Statements) {
                statement.Accept(this);
            }
        }
    }
}
