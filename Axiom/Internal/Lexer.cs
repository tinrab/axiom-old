using System;
using System.IO;
using System.Text;

namespace Axiom.Internal
{
    internal class Lexer : IDisposable
    {
        private TextReader _reader;
        private int _lineNumber, _columnNumber;

        public Lexer(TextReader textReader)
        {
            _reader = textReader;
        }

        public Symbol Next()
        {
            while (_reader.Peek() != -1) {
                char ch = (char)_reader.Read();
                _columnNumber++;

                if (IsNewLine(ch)) {
                    _lineNumber++;
                    _columnNumber = 0;

                    if (IsNewLine((char)_reader.Peek())) {
                        _reader.Read();
                    }

                    continue;
                }

                #region Comments
                if (ch == '/') {
                    switch (_reader.Peek()) {
                    case '/':
                        do {
                            ch = (char)_reader.Read();
                        } while (!IsNewLine(ch));

                        _lineNumber++;
                        _columnNumber = 0;

                        continue;
                    case '*':
                        char p;

                        do {
                            p = ch;
                            int c = _reader.Read();

                            if (c == -1) {
                                Error.Report("unclosed multi-line comment", new Position(_lineNumber, _columnNumber));
                            }

                            ch = (char)c;
                            _columnNumber++;

                            if (IsNewLine(ch)) {
                                _lineNumber++;
                                _columnNumber = 0;
                            }
                        } while (p != '*' || ch != '/');

                        continue;
                    }
                }
                #endregion

                #region Integers
                if (IsDigit(ch)) {
                    var sb = new StringBuilder();
                    var pos = new Position(_lineNumber, _columnNumber);

                    sb.Append(ch);

                    while (IsDigit((char)_reader.Peek())) {
                        ch = (char)_reader.Read();
                        sb.Append(ch);
                        pos.ColumnEnd++;
                    }

                    _columnNumber = pos.ColumnEnd;

                    return new Symbol(Token.IntegerLiteral, pos, sb.ToString());
                }
                #endregion

                #region Words
                if (IsIdentifierStart(ch)) {
                    var sb = new StringBuilder();
                    var pos = new Position(_lineNumber, _columnNumber);

                    sb.Append(ch);

                    while (IsIdentifier((char)_reader.Peek())) {
                        ch = (char)_reader.Read();
                        sb.Append(ch);
                        pos.ColumnEnd++;
                    }

                    _columnNumber = pos.ColumnEnd;
                    var symbol = new Symbol();
                    symbol.Lexeme = sb.ToString();
                    symbol.Position = pos;

                    switch (symbol.Lexeme) {
                    case "true":
                    case "false":
                        symbol.Token = Token.LogicalLiteral;
                        break;
                    case "nil":
                        symbol.Token = Token.KeywordNil;
                        break;
                    case "if":
                        symbol.Token = Token.KeywordIf;
                        break;
                    case "else":
                        symbol.Token = Token.KeywordElse;
                        break;
                    case "elif":
                        symbol.Token = Token.KeywordElif;
                        break;
                    case "for":
                        symbol.Token = Token.KeywordFor;
                        break;
                    case "while":
                        symbol.Token = Token.KeywordWhile;
                        break;
                    case "class":
                        symbol.Token = Token.KeywordClass;
                        break;
                    case "this":
                        symbol.Token = Token.KeywordThis;
                        break;
                    case "base":
                        symbol.Token = Token.KeywordBase;
                        break;
                    case "try":
                        symbol.Token = Token.KeywordTry;
                        break;
                    case "catch":
                        symbol.Token = Token.KeywordCatch;
                        break;
                    default:
                        symbol.Token = Token.Identifier;
                        break;
                    }

                    return symbol;
                }
                #endregion
            }

            return new Symbol(Token.Eof, new Position(_lineNumber, _columnNumber), null);
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
