using Axiom.Internal.Frames;
using System;
using System.Collections.Generic;

namespace Axiom.Internal.Intermediate
{
    internal class Interpreter
    {
        private IDictionary<string, ImChunk> _chunks;
        private IDictionary<Temp, object> _temps;
        private IDictionary<int, object> _memory;

        public Interpreter(LinkedList<ImChunk> chunks)
        {
            _chunks = new Dictionary<string, ImChunk>();
            _temps = new Dictionary<Temp, object>();
            _memory = new Dictionary<int, object>();

            foreach (var chunk in chunks) {
                _chunks[chunk.Frame.Label.Name] = chunk;
            }
        }

        public void Run()
        {
            ExecuteFunction("main");
        }

        private object ExecuteFunction(string label)
        {
            var chunk = _chunks[label];
            var frame = chunk.Frame;

            var outerTemps = new Dictionary<Temp, object>(_temps);
            _temps = new Dictionary<Temp, object>();

            var statements = ((ImSequence)chunk.LinearCode).Statements;

            for (int pc = 0; pc < statements.Count;) {
                var newLabel = Execute(statements[pc]);

                if (newLabel == null) {
                    pc++;
                } else {
                    pc = statements.IndexOf(new ImLabel(newLabel));

                    if (pc == -1) {
                        break;
                    }
                }
            }

            _temps = outerTemps;

            return null;
        }

        private Label Execute(ImStatement statement)
        {
            if (statement is ImExpressionStatement) {
                var stmt = (ImExpressionStatement)statement;

                var result = Execute(stmt.Expression);

                Console.WriteLine("Expression result: " + result);

                return null;
            }

            return null;
        }

        private object Execute(ImExpression expression)
        {
            if (expression is ImBinaryOperation) {
                var expr = (ImBinaryOperation)expression;

                long a = (long)Execute(expr.Left);
                long b = (long)Execute(expr.Right);

                switch (expr.Operator) {
                case ImBinaryOperation.BinaryOperator.Add:
                    return a + b;
                case ImBinaryOperation.BinaryOperator.Subtract:
                    return a - b;
                case ImBinaryOperation.BinaryOperator.Multiply:
                    return a * b;
                case ImBinaryOperation.BinaryOperator.Divide:
                    return a / b;
                case ImBinaryOperation.BinaryOperator.Modulo:
                    return a % b;
                }
            }

            if (expression is ImConstant) {
                var expr = (ImConstant)expression;

                return expr.Value;
            }

            Error.Report();
            return null;
        }
    }
}
