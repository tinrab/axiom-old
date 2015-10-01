using Axiom.Internal.Frames;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Axiom.Internal.Ast;

namespace Axiom.Internal.Intermediate
{
    internal class ImCodeGenerator : IVisitor
    {
        private Frame _frame;
        private ImCode _result;

        public ImCodeGenerator()
        {
            Chunks = new LinkedList<ImChunk>();
        }

        public void Visit(Program acceptor)
        {
            var frame = new Frame(new Label("main"), new FunctionExpression(null, null), 0);

            _frame = frame;

            foreach (var statement in acceptor.Statements) {
                statement.Accept(this);
            }

            if (_result is ImExpression) {
                Chunks.AddLast(new ImChunk(frame, new ImExpressionStatement((ImExpression)_result)));
            } else {
                Chunks.AddLast(new ImChunk(frame, (ImStatement)_result));
            }
        }

        public void Visit(ConditionalExpression acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(WhileStatement acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(FunctionExpression acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(IfStatement acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(Identifier acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(AssignmentExpression acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(MemberExpression acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(ForStatement acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(ReferenceExpression acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(CallExpression acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(Literal acceptor)
        {
            _result = new ImConstant(acceptor.Value);
        }

        public void Visit(BlockStatement acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(UnaryExpression acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(SequenceExpression acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(BinaryExpression acceptor)
        {
            acceptor.Left.Accept(this);
            var left = (ImExpression)_result;

            acceptor.Right.Accept(this);
            var right = (ImExpression)_result;

            _result = new ImBinaryOperation(ImBinaryOperation.OperatorFromToken(acceptor.Operator), left, right);
        }

        public LinkedList<ImChunk> Chunks { get; private set; }
    }
}
