using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace Axiom.Internal
{
    internal class Lexer : ILexer
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
                int ch = _reader.Read();
                _columnNumber++;

                if (IsNewLine(ch)) {
                    _lineNumber++;
                    _columnNumber = 0;

                    if (IsNewLine(_reader.Peek())) {
                        _reader.Read();
                    }

                    continue;
                }

                #region Comments
                if (ch == '/') {
                    switch (_reader.Peek()) {
                    case '/':
                        do {
                            ch = _reader.Read();
                        } while (!IsNewLine(ch));

                        _lineNumber++;
                        _columnNumber = 0;

                        continue;
                    case '*':
                        int p;

                        do {
                            p = ch;
                            ch = _reader.Read();

                            if (ch == -1) {
                                Error.Report(new Position(_lineNumber, _columnNumber), "Unclosed multi-line comment");
                            }

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

                #region Numbers
                if (IsDigit(ch)) {
                    var sb = new StringBuilder();
                    var pos = new Position(_lineNumber, _columnNumber);
                    var token = Token.IntegerLiteral;

                    sb.Append((char)ch);

                    while (true) {
                        if (!IsDigit(_reader.Peek())) {
                            if (_reader.Peek() == '.') {
                                token = Token.DecimalLiteral;
                            } else {
                                break;
                            }
                        }

                        ch = _reader.Read();
                        sb.Append((char)ch);
                        pos.ColumnEnd++;
                    }

                    _columnNumber = pos.ColumnEnd;

                    return new Symbol(token, pos, sb.ToString());
                }
                #endregion

                #region Words
                if (IsIdentifierStart(ch)) {
                    var sb = new StringBuilder();
                    var pos = new Position(_lineNumber, _columnNumber);

                    sb.Append((char)ch);

                    while (IsIdentifier(_reader.Peek())) {
                        ch = _reader.Read();
                        sb.Append((char)ch);
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
                    case "delete":
                        symbol.Token = Token.KeywordDelete;
                        break;
                    case "is":
                        symbol.Token = Token.KeywordIs;
                        break;
                    case "in":
                        symbol.Token = Token.KeywordIn;
                        break;
                    default:
                        symbol.Token = Token.Identifier;
                        break;
                    }

                    return symbol;
                }
                #endregion

                #region Strings
                if (IsStringDelimiter(ch)) {
                    var sb = new StringBuilder();
                    var pos = new Position(_lineNumber, _columnNumber);

                    sb.Append((char)ch);

                    do {
                        ch = _reader.Read();
                        pos.ColumnEnd++;

                        sb.Append((char)ch);

                        if (IsStringDelimiter(ch)) {
                            break;
                        } else if (IsNewLine(ch) || ch == -1) {
                            Error.Report(new Position(_lineNumber, pos.ColumnEnd), "Unclosed string literal");
                        }
                    } while (true);

                    _columnNumber = pos.ColumnEnd;

                    return new Symbol(Token.StringLiteral, pos, sb.ToString());
                }
                #endregion

                #region Punctuators
                if (!IsWhiteSpace(ch)) {
                    var sb = new StringBuilder();
                    var pos = new Position(_lineNumber, _columnNumber);

                    sb.Append((char)ch);

                    int p = _reader.Peek();

                    if (ch == '=' || ch == '!' || ch == '<' || ch == '>') {
                        if (p == '=') {
                            sb.Append((char)_reader.Read());
                            pos.ColumnEnd++;
                        }
                    } else if (ch == '&') {
                        if (p == '&' || p == '=') {
                            sb.Append((char)_reader.Read());
                            pos.ColumnEnd++;
                        }
                    } else if (ch == '|') {
                        if (p == '|' || p == '=') {
                            sb.Append((char)_reader.Read());
                            pos.ColumnEnd++;
                        }
                    } else if (ch == '^' && p == '=') {
                        sb.Append((char)_reader.Read());
                        pos.ColumnEnd++;
                    }

                    if (ch == '<' && p == '<') {
                        sb.Append((char)_reader.Read());
                        pos.ColumnEnd++;

                        if (_reader.Peek() == '=') {
                            sb.Append((char)_reader.Read());
                            pos.ColumnEnd++;
                        }
                    }

                    if (ch == '>' && p == '>') {
                        sb.Append((char)_reader.Read());
                        pos.ColumnEnd++;

                        if (_reader.Peek() == '=') {
                            sb.Append((char)_reader.Read());
                            pos.ColumnEnd++;
                        } else if (_reader.Peek() == '>') {
                            sb.Append((char)_reader.Read());
                            pos.ColumnEnd++;

                            if (_reader.Peek() == '=') {
                                sb.Append((char)_reader.Read());
                                pos.ColumnEnd++;
                            }
                        }
                    }

                    if (ch == '+' && (p == '+' || p == '=')) {
                        sb.Append((char)_reader.Read());
                        pos.ColumnEnd++;
                    } else if (ch == '-' && (p == '-' || p == '=')) {
                        sb.Append((char)_reader.Read());
                        pos.ColumnEnd++;
                    } else if (ch == '*' && (p == '*' || p == '=')) {
                        sb.Append((char)_reader.Read());
                        pos.ColumnEnd++;
                    } else if (ch == '/' && (p == '/' || p == '=')) {
                        sb.Append((char)_reader.Read());
                        pos.ColumnEnd++;
                    } else if (ch == '%' && (p == '%' || p == '=')) {
                        sb.Append((char)_reader.Read());
                        pos.ColumnEnd++;
                    }

                    var lexeme = sb.ToString();

                    if (Punctuators.ContainsKey(lexeme)) {
                        return new Symbol(Punctuators[lexeme], pos, lexeme);
                    } else {
                        Error.Report(pos, "Illegal character");
                    }
                }
                #endregion
            }

            return new Symbol(Token.Eof, new Position(_lineNumber, _columnNumber), null);
        }

        public void Dispose()
        {
            _reader.Dispose();
        }

        private static bool IsWhiteSpace(int ch)
        {
            return char.IsWhiteSpace((char)ch);
        }

        private static bool IsStringDelimiter(int ch)
        {
            return ch == '"';
        }

        private static bool IsNewLine(int ch)
        {
            return ch == '\n' || ch == '\r';
        }

        private static bool IsDigit(int ch)
        {
            return char.IsDigit((char)ch);
        }

        private static bool IsIdentifierStart(int ch)
        {
            return ch == '_' || char.IsLetter((char)ch);
        }

        private static bool IsIdentifier(int ch)
        {
            return ch == '_' || char.IsLetterOrDigit((char)ch);
        }

        private static readonly IDictionary<string, Token> Punctuators = new SortedDictionary<string, Token> {
            { "+", Token.Add },
            { "-", Token.Subtract },
            { "*", Token.Multiply },
            { "/", Token.Divide },
            { "%", Token.Modulo },
            { "++", Token.Increment },
            { "--", Token.Decrement },
            { "&&", Token.LogicalAnd },
            { "||", Token.LogicalOr },
            { "!", Token.LogicalNot },
            { "==", Token.LogicalEqual },
            { "!=", Token.LogicalNotEqual },
            { "<", Token.LogicalLess },
            { ">", Token.LogicalGreater },
            { "<=", Token.LogicalLessOrEqual },
            { ">=", Token.LogicalGreaterOrEqual },
            { "&", Token.BitwiseAnd },
            { "|", Token.BitwiseOr },
            { "^", Token.BitwiseXor },
            { "~", Token.BitwiseNot },
            { "<<", Token.BitwiseLeftShift },
            { ">>", Token.BitwiseRightShift },
            { ">>>", Token.BitwiseUnsignedRightShift },
            { "=", Token.Assign },
            { "+=", Token.AddAssign },
            { "-=", Token.SubtractAssign },
            { "*=", Token.MultiplyAssign },
            { "/=", Token.DivideAssign },
            { "%=", Token.ModuloAssign },
            { "&=", Token.BitwiseAndAssign },
            { "|=", Token.BitwiseOrAssign },
            { "^=", Token.BitwiseXorAssign },
            { "<<=", Token.LeftShiftAssign },
            { ">>=", Token.RightShiftAssign },
            { ">>>=", Token.BitwiseUnsignedRightShiftAssign },
            { ":", Token.Colon },
            { ";", Token.SemiColon },
            { ",", Token.Comma },
            { ".", Token.Dot },
            { "?", Token.QuestionMark },
            { "(", Token.OpenParenthesis },
            { ")", Token.CloseParenthesis },
            { "[", Token.OpenBracket },
            { "]", Token.CloseBracket },
            { "{", Token.OpenBrace },
            { "}", Token.CloseBrace },
        };
    }
}
