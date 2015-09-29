using System;
using System.Collections.Generic;
using System.Text;

namespace Axiom.Internal
{
    internal class Parser : IDisposable
    {
        private Lexer _lexer;
        private Symbol _currentSymbol, _nextSymbol;

        public Parser(Lexer lexer)
        {
            _lexer = lexer;
        }

        public void Parse()
        {
            Symbol symbol;

            while ((symbol = _lexer.Next()).Token != Token.Eof) {
                Console.WriteLine(symbol);
            }
        }

        private bool Match(Token token)
        {
            return Peek() == token;
        }

        private Token Peek()
        {
            return _nextSymbol.Token;
        }

        private void Expect(params Token[] tokens)
        {
            foreach (var token in tokens) {
                if (_nextSymbol.Token == token) {
                    _currentSymbol = _nextSymbol;
                    _nextSymbol = _lexer.Next();
                } else {
                    Error.Report(_currentSymbol.Position, "Expected \"{0}\", got \"{1}\"", token, _nextSymbol.Token);
                }
            }
        }

        public void Dispose()
        {
            _lexer.Dispose();
        }
    }
}
