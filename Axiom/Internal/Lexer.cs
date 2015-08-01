using System;
using System.IO;

namespace Axiom.Internal
{
    internal class Lexer : IDisposable
    {
        private TextReader _reader;
        private Position position;

        public Lexer(TextReader textReader)
        {
            _reader = textReader;
            position = new Position();
        }

        public Symbol Next()
        {
            return new Symbol(Token.Eof, position, null);
        }

        public void Dispose()
        {
            _reader.Dispose();
        }
    }
}
