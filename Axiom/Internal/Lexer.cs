using System;
using System.IO;
using System.Text;

namespace Axiom.Internal
{
    internal class Lexer : IDisposable
    {
        private TextReader _reader;
        private Position _position;

        public Lexer(TextReader textReader)
        {
            _reader = textReader;
            _position = new Position();
        }

        public Symbol Next()
        {
            var symbol = new Symbol(Token.Eof, _position, null);

            while (_reader.Peek() != -1) {
                char ch = (char)_reader.Read();

                #region Comments
                if (ch == '/') {
                    switch (_reader.Peek()) {
                    case '/':
                        do {
                            ch = (char)_reader.Read();
                        } while (!IsNewLine(ch));

                        continue;
                    case '*':
                        char p;

                        do {
                            p = ch;
                            int c = _reader.Read();

                            if (c == -1) {
                                Error.Report("unclosed multi-line comment", _position);
                            }

                            ch = (char)c;
                        } while (p != '*' || ch != '/');

                        continue;
                    }
                }
                #endregion

                #region Integers
                if (IsDigit(ch)) {
                    var sb = new StringBuilder();
                }
                #endregion

                Console.WriteLine(ch);
            }

            return symbol;
        }

        private static bool IsNewLine(char ch)
        {
            return ch == '\n' || ch == '\r';
        }

        private static bool IsDigit(char ch)
        {
            return char.IsDigit(ch);
        }

        private static bool IsIdentifierStart(char ch)
        {
            return ch == '_' || char.IsLetter(ch);
        }

        private static bool IsIdentifier(char ch)
        {
            return ch == '_' || char.IsLetterOrDigit(ch);
        }

        public void Dispose()
        {
            _reader.Dispose();
        }
    }
}
