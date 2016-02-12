using Axiom.Internal.Frames;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Axiom.Internal.Ast;
using Axiom.Internal.Semantics;

namespace Axiom.Internal.Intermediate
{
    internal class ImCodeGenerator : IVisitor
    {
        private Frame _frame;
        private ImCode _result;

        public ImCodeGenerator()
        {
            Chunks = new List<ImChunk>();
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
            var frame = FrameDescription.Frames[acceptor];

            var tmpFrame = _frame;
            _frame = frame;

            acceptor.Body.Accept(this);

            if (_result is ImExpression) {
                Chunks.Add(new ImChunk(frame, new ImExpressionStatement((ImExpression)_result)));
            } else {
                Chunks.Add(new ImChunk(frame, (ImStatement)_result));
            }

            _frame = tmpFrame;
        }

        public void Visit(IfStatement acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(Identifier acceptor)
        {
            var init = SymbolDescription.Initializations[acceptor];
            //var frame = FrameDescription.Frames[init];
            //var access = FrameDescription.Accesses[init];

            if (init is AssignmentExpression) {
                var access = FrameDescription.Accesses[acceptor];

                //_result = new ImMemoryRead(new ImTemp(Temp.Create(acceptor.Name)));
                _result = new ImTemp(Temp.Create(acceptor.Name));
            }
        }

        public void Visit(AssignmentExpression acceptor)
        {
            acceptor.Destination.Accept(this);

            var dst = (ImExpression)_result;

            acceptor.Source.Accept(this);

            var src = (ImExpression)_result;

            var t1 = new ImTemp(Temp.Create());
            var t2 = new ImTemp(Temp.Create());
            var seq = new ImSequence();

            /*
            seq.Statements.Add(new ImMove(t1, dst));
            seq.Statements.Add(new ImMove(t2, src));
            seq.Statements.Add(new ImMove(new ImMemoryRead(t1), t2));
            */

            /*
            seq.Statements.Add(new ImMemoryWrite(t1, dst));
            seq.Statements.Add(new ImMemoryWrite(t2, src));
            seq.Statements.Add(new ImMemoryWrite(new ImMemoryRead(t1), t2));
            */

            seq.Statements.Add(new ImMemoryWrite(dst, src));

            _result = new ImExpressionSequence(seq, t2);
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
            var seq = new ImSequence();

            foreach (var statement in acceptor.Statements) {
                statement.Accept(this);

                if (_result is ImExpression) {
                    seq.Statements.Add(new ImExpressionStatement((ImExpression)_result));
                } else {
                    seq.Statements.Add((ImStatement)_result);
                }
            }

            _result = seq;
        }

        public void Visit(UnaryExpression acceptor)
        {
            throw new NotImplementedException();
        }

        public void Visit(SequenceExpression acceptor)
        {
            var seq = new ImSequence();

            for (int i = 0; i < acceptor.Expressions.Count - 1; i++) {
                var expr = acceptor.Expressions[i];

                expr.Accept(this);

                if (_result is ImExpression) {
                    seq.Statements.Add(new ImExpressionStatement((ImExpression)_result));
                } else {
                    seq.Statements.Add((ImStatement)_result);
                }
            }

            acceptor.Expressions[acceptor.Expressions.Count - 1].Accept(this);

            _result = new ImExpressionSequence(seq, (ImExpression)_result);
        }

        public void Visit(BinaryExpression acceptor)
        {
            acceptor.Left.Accept(this);
            var left = (ImExpression)_result;

            acceptor.Right.Accept(this);
            var right = (ImExpression)_result;

            _result = new ImBinaryOperation(ImBinaryOperation.OperatorFromToken(acceptor.Operator), left, right);
        }

        public IList<ImChunk> Chunks { get; private set; }
    }
}
