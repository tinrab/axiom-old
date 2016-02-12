using Axiom.Internal;
using Axiom.Internal.Frames;
using Axiom.Internal.Intermediate;
using System;
using System.Collections.Generic;

namespace Axiom
{
    internal class Interpreter
    {
        private IDictionary<string, ImChunk> _chunks;
        private IDictionary<string, object> _memory;

        public Interpreter(IList<ImChunk> chunks)
        {
            _chunks = new Dictionary<string, ImChunk>();
            _memory = new Dictionary<string, object>();

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

            var statements = ((ImSequence)chunk.LinearCode).Statements;
            int pc = 0;

            while (pc < statements.Count) {
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

            return null;
        }

        private Label Execute(ImStatement statement)
        {
            if (statement is ImMemoryWrite) {
                var write = (ImMemoryWrite)statement;

                if (write.Destination is ImTemp) {
                    var dst = (ImTemp)write.Destination;
                    var src = Execute(write.Expression);

                    _memory[dst.Temp.Name] = src;

                    Log("Memory write: " + dst.Temp.Name + " <- " + src.ToString());

                    return null;
                }
            }

            if (statement is ImExpressionStatement) {
                var stmt = (ImExpressionStatement)statement;

                var result = Execute(stmt.Expression);

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

            if (expression is ImTemp) {
                var temp = (ImTemp)expression;

                /*
                if (!_memory.ContainsKey(temp.Temp.Name)) {
                    _memory[temp.Temp.Name] = null;
                }
                */
                // return _memory[temp.Temp.Name];

                return temp.Temp.Name;
            }

            if (expression is ImMemoryRead) {
                var read = (ImMemoryRead)expression;

                string addr = (string)Execute(read.Expression);

                return _memory[addr];
            }

            Error.Report();

            return null;
        }

        private void Log(string message)
        {
            Console.WriteLine("[Interpreter] " + message);
        }
    }
}
