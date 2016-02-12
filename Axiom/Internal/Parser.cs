using Axiom.Internal.Ast;
using System;
using System.Collections.Generic;
using System.Text;

namespace Axiom.Internal
{
    internal class Parser : IParser
    {
        private static readonly Token[] AssignOperators = {
            Token.Assign,
            Token.AddAssign,
            Token.Subtract,
            Token.MultiplyAssign,
            Token.DivideAssign,
            Token.ModuloAssign,
            Token.BitwiseAndAssign,
            Token.BitwiseOrAssign,
            Token.BitwiseXorAssign,
            Token.LeftShiftAssign,
            Token.RightShiftAssign,
            Token.BitwiseUnsignedRightShiftAssign
        };

        private static readonly Token[] RelationalOperators = {
            Token.LogicalLess,
            Token.LogicalLessOrEqual,
            Token.LogicalGreater,
            Token.LogicalGreaterOrEqual,
            Token.LogicalEqual,
            Token.LogicalNotEqual
        };

        private ILexer _lexer;
        private Symbol _currentSymbol, _nextSymbol;

        public Parser(ILexer lexer)
        {
            _lexer = lexer;
        }

        public AstNode Parse()
        {
            _nextSymbol = _lexer.Next();

            var statements = ParseStatements();

            return new FunctionExpression(new List<Identifier>(), new BlockStatement(statements));
        }

        #region Statements
        private IList<Statement> ParseStatements()
        {
            var statements = new List<Statement>();

            while (!Match(Token.Eof)) {
                var statement = ParseStatement();

                if (statement != null) {
                    statements.Add(statement);
                }
            }

            return statements;
        }

        private Statement ParseStatement()
        {
            switch (Peek()) {
            case Token.OpenBrace:
                return ParseBlockStatement();
            case Token.OpenParenthesis:
                return ParseExpressionStatement();
            case Token.KeywordIf:
                return ParseIfStatement();
            case Token.KeywordWhile:
                return ParseWhileStatement();
            case Token.KeywordFor:
                return ParseForStatement();
            case Token.SemiColon:
                Accept(Token.SemiColon);

                return null;
            }

            return ParseExpressionStatement();
        }

        private Statement ParseIfStatement()
        {
            Check(Token.KeywordIf);

            Check(Token.OpenParenthesis);
            var condition = ParseExpression();
            Check(Token.CloseParenthesis);
            var body = ParseStatement();

            if (Accept(Token.KeywordElse)) {
                var elseBody = ParseStatement();

                return new IfStatement(condition, body, elseBody);
            }

            return new IfStatement(condition, body);
        }

        private Statement ParseWhileStatement()
        {
            Check(Token.KeywordWhile);

            Check(Token.OpenParenthesis);
            var condition = ParseExpression();
            Check(Token.CloseParenthesis);

            var body = ParseStatement();

            return new WhileStatement(condition, body);
        }

        private Statement ParseForStatement()
        {
            return new ForStatement(null, null, null, null);
        }

        private Statement ParseBlockStatement()
        {
            Check(Token.OpenBrace);

            var statements = ParseStatementList();

            Check(Token.CloseBrace);

            return new BlockStatement(statements);
        }

        private IList<Statement> ParseStatementList()
        {
            var list = new List<Statement>();

            while (true) {
                if (Match(Token.CloseBrace)) {
                    break;
                }

                var statement = ParseStatement();

                if (statement == null) {
                    break;
                }

                list.Add(statement);
            }

            return list;
        }

        private Statement ParseExpressionStatement()
        {
            var stmt = new ExpressionStatement(ParseExpression());

            Accept(Token.SemiColon);

            return stmt;
        }
        #endregion

        #region Expressions
        private Expression ParseExpression()
        {
            var expr = ParseAssignmentExpression();

            if (Match(Token.Comma)) {
                var expressions = new List<Expression>();

                expressions.Add(expr);

                while (Accept(Token.Comma)) {
                    expressions.Add(ParseExpression());
                }

                return new SequenceExpression(expressions);
            }

            return expr;
        }

        private Expression ParseAssignmentExpression()
        {
            var expr = ParseConditionalExpression();

            if (Accept(AssignOperators)) {
                var left = expr;
                var oper = _currentSymbol.Token;
                var right = ParseAssignmentExpression();

                return new AssignmentExpression(oper, left, right);
            }

            return expr;
        }

        private Expression ParseConditionalExpression()
        {
            var expr = ParseOrExpression();

            if (Accept(Token.QuestionMark)) {
                var consequent = ParseAssignmentExpression();
                Check(Token.Colon);
                var alternate = ParseAssignmentExpression();

                return new ConditionalExpression(expr, consequent, alternate);
            }

            return expr;
        }

        private Expression ParseOrExpression()
        {
            var expr = ParseAndExpression();

            if (Accept(Token.LogicalOr)) {
                return new BinaryExpression(Token.LogicalOr, expr, ParseOrExpression());
            }

            return expr;
        }

        private Expression ParseAndExpression()
        {
            var expr = ParseBitwiseOrExpression();

            if (Accept(Token.LogicalAnd)) {
                return new BinaryExpression(Token.LogicalAnd, expr, ParseAndExpression());
            }

            return expr;
        }

        private Expression ParseBitwiseOrExpression()
        {
            var expr = ParseBitwiseXorExpression();

            if (Accept(Token.BitwiseOr)) {
                return new BinaryExpression(Token.BitwiseOr, expr, ParseBitwiseOrExpression());
            }

            return expr;
        }

        private Expression ParseBitwiseXorExpression()
        {
            var expr = ParseBitwiseAndExpression();

            if (Accept(Token.BitwiseXor)) {
                return new BinaryExpression(Token.BitwiseXor, expr, ParseBitwiseXorExpression());
            }

            return expr;
        }

        private Expression ParseBitwiseAndExpression()
        {
            var expr = ParseEqualityExpression();

            if (Accept(Token.BitwiseAnd)) {
                return new BinaryExpression(Token.BitwiseAnd, expr, ParseBitwiseAndExpression());
            }

            return expr;
        }

        private Expression ParseEqualityExpression()
        {
            var expr = ParseRelationalExpression();

            if (Accept(Token.LogicalEqual, Token.LogicalNotEqual)) {
                return new BinaryExpression(_currentSymbol.Token, expr, ParseEqualityExpression());
            }

            return expr;
        }

        private Expression ParseRelationalExpression()
        {
            var expr = ParseShiftExpression();

            if (Accept(RelationalOperators)) {
                return new BinaryExpression(_currentSymbol.Token, expr, ParseRelationalExpression());
            }

            return expr;
        }

        private Expression ParseShiftExpression()
        {
            var expr = ParseAdditiveExpression();

            if (Accept(Token.BitwiseLeftShift, Token.BitwiseRightShift, Token.BitwiseUnsignedRightShift)) {
                return new BinaryExpression(_currentSymbol.Token, expr, ParseShiftExpression());
            }

            return expr;
        }

        private Expression ParseAdditiveExpression()
        {
            var expr = ParseMultiplicativeExpression();

            if (Accept(Token.Add, Token.Subtract)) {
                return new BinaryExpression(_currentSymbol.Token, expr, ParseAdditiveExpression());
            }

            return expr;
        }

        private Expression ParseMultiplicativeExpression()
        {
            var expr = ParseUnaryExpression();

            if (Accept(Token.Multiply, Token.Divide, Token.Modulo)) {
                return new BinaryExpression(_currentSymbol.Token, expr, ParseMultiplicativeExpression());
            }

            return expr;
        }

        private Expression ParseUnaryExpression()
        {
            if (Accept(Token.Increment, Token.Decrement)) {
                var t = _currentSymbol.Token;
                var expr = ParseUnaryExpression();

                if (!IsLeftHandSide(expr)) {
                    Error.Report(expr.Position, "Illegal lhs value");
                }

                return new UnaryExpression(t, expr);
            }

            if (Accept(Token.Subtract, Token.BitwiseNot, Token.LogicalNot, Token.KeywordDelete)) {
                var t = _currentSymbol.Token;
                var expr = ParseUnaryExpression();

                return new UnaryExpression(t, expr);
            }

            return ParsePostfixExpression();
        }

        private Expression ParsePostfixExpression()
        {
            var expr = ParseLeftHandSideExpressionAllowCall();

            if ((Match(Token.Increment, Token.Decrement)) && Peek() != Token.SemiColon) {
                if (!IsLeftHandSide(expr)) {
                    Error.Report(expr.Position, "Illegal lhs value");
                }

                Accept(Token.Increment, Token.Decrement);

                expr = new UnaryExpression(_currentSymbol.Token, expr, true);
            }

            return expr;
        }

        private Expression ParseLeftHandSideExpression()
        {
            var expr = ParsePrimaryExpression();

            while (Match(Token.Dot, Token.OpenBracket)) {
                if (Match(Token.OpenBracket)) {
                    var member = ParseComputedMember();

                    expr = new MemberExpression(MemberExpression.AccessType.ListAccess, expr, member);
                } else {
                    var property = ParseNonComputedMember();

                    expr = new MemberExpression(MemberExpression.AccessType.Property, expr, property);
                }
            }

            return expr;
        }

        private Expression ParseLeftHandSideExpressionAllowCall()
        {
            var expr = ParsePrimaryExpression();

            while (Match(Token.Dot, Token.OpenBracket, Token.OpenParenthesis)) {
                if (Match(Token.OpenParenthesis)) {
                    var args = ParseArguments();

                    expr = new CallExpression(expr, args);
                } else if (Match(Token.OpenBracket)) {
                    var member = ParseComputedMember();

                    expr = new MemberExpression(MemberExpression.AccessType.ListAccess, expr, member);
                } else {
                    var property = ParseNonComputedMember();

                    expr = new MemberExpression(MemberExpression.AccessType.Property, expr, property);
                }
            }

            return expr;
        }

        private Expression ParseComputedMember()
        {
            Check(Token.OpenBracket);
            var expr = ParseExpression();
            Check(Token.CloseBracket);

            return expr;
        }

        private Identifier ParseNonComputedMember()
        {
            Check(Token.Dot);

            return ParseNonComputedProperty();
        }

        private Identifier ParseNonComputedProperty()
        {
            Check(Token.Identifier);

            return new Identifier(_currentSymbol.Lexeme);
        }

        private Expression ParseMemberExpression()
        {
            var expr = ParsePrimaryExpression();

            while (true) {
                if (Accept(Token.OpenBracket)) {
                    var left = expr;
                    var right = ParseExpression();

                    Check(Token.CloseBracket);

                    expr = new MemberExpression(MemberExpression.AccessType.ListAccess, left, right);
                } else if (Accept(Token.Dot)) {
                    var left = expr;
                    var right = ParseMemberExpression();

                    expr = new MemberExpression(MemberExpression.AccessType.Property, left, right);
                } else if (Accept(Token.OpenParenthesis)) {
                    var left = expr;
                    var arguments = ParseArguments();

                    Check(Token.CloseParenthesis);

                    if (!(left is Identifier)) {
                        Error.Report(left.Position, "Callable variable expected");
                    }

                    expr = new CallExpression((Identifier)left, arguments);
                } else {
                    break;
                }
            }

            return expr;
        }

        private IList<Expression> ParseArguments()
        {
            var arguments = new List<Expression>();

            while (true) {
                if (Match(Token.CloseParenthesis)) {
                    break;
                }

                arguments.Add(ParseAssignmentExpression());

                if (!Accept(Token.Comma)) {
                    break;
                }
            }

            return arguments;
        }

        private Expression ParsePrimaryExpression()
        {
            if (Accept(Token.OpenParenthesis)) {
                var expr = ParseExpression();
                Check(Token.CloseParenthesis);

                return expr;
            }

            if (Accept(Token.Identifier)) {
                return new Identifier(_currentSymbol.Lexeme);
            }

            if (Accept(Token.KeywordThis)) {
                return new ReferenceExpression(ReferenceExpression.ReferenceType.This);
            }

            if (Accept(Token.KeywordBase)) {
                return new ReferenceExpression(ReferenceExpression.ReferenceType.Base);
            }

            if (Accept(Token.IntegerLiteral)) {
                return new Literal(long.Parse(_currentSymbol.Lexeme), Literal.LiteralType.Integer);
            }

            if (Accept(Token.DecimalLiteral)) {
                return new Literal(double.Parse(_currentSymbol.Lexeme), Literal.LiteralType.Decimal);
            }

            if (Accept(Token.StringLiteral)) {
                return new Literal(_currentSymbol.Lexeme, Literal.LiteralType.String);
            }

            if (Accept(Token.LogicalLiteral)) {
                return new Literal(_currentSymbol.Lexeme == "true", Literal.LiteralType.Logical);
            }

            if (Accept(Token.KeywordNil)) {
                return new Literal(null, Literal.LiteralType.Nil);
            }

            if (Match(Token.Dollar)) {
                return ParseFunctionExpression();
            }

            if (Match(Token.Hash)) {
                return ParseClassExpression();
            }

            if (Match(Token.OpenBrace)) {
                return ParseObjectInitializer();
            }

            Error.Report();

            return null;
        }

        private Expression ParseFunctionExpression()
        {
            Check(Token.Dollar, Token.OpenParenthesis);

            var parameters = ParseParameters();

            Check(Token.CloseParenthesis);

            var body = ParseStatement();

            return new FunctionExpression(parameters, body);
        }

        private Expression ParseClassExpression()
        {
            return null;
        }

        private Expression ParseObjectInitializer()
        {
            return null;
        }

        private IList<Identifier> ParseParameters()
        {
            var parameters = new List<Identifier>();

            while (Accept(Token.Identifier)) {
                parameters.Add(new Identifier(_currentSymbol.Lexeme));

                if (!Accept(Token.Comma)) {
                    break;
                }
            }

            return parameters;
        }
        #endregion

        #region Helpers
        private Token Peek()
        {
            return _nextSymbol.Token;
        }

        private bool Match(Token token)
        {
            return Peek() == token;
        }

        private bool Match(params Token[] tokens)
        {
            foreach (var token in tokens) {
                if (Peek() == token) {
                    return true;
                }
            }

            return false;
        }

        private void Check(Token token)
        {
            if (Match(token)) {
                _currentSymbol = _nextSymbol;
                _nextSymbol = _lexer.Next();
            } else {
                Error.Report(_currentSymbol.Position, "Expected \"{0}\", got \"{1}\"", token, _nextSymbol.Token);
            }
        }

        private void Check(params Token[] tokens)
        {
            foreach (var token in tokens) {
                Check(token);
            }
        }

        private bool Accept(Token token)
        {
            if (Match(token)) {
                _currentSymbol = _nextSymbol;
                _nextSymbol = _lexer.Next();

                return true;
            }

            return false;
        }

        private bool Accept(params Token[] tokens)
        {
            foreach (var token in tokens) {
                if (Accept(token)) {
                    return true;
                }
            }

            return false;
        }

        private bool IsLeftHandSide(Expression expr)
        {
            return expr is Identifier || expr is MemberExpression;
        }
        #endregion

        public void Dispose()
        {
            _lexer.Dispose();
        }
    }
}
