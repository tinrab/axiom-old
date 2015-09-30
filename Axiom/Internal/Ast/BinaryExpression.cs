using System;

namespace Axiom.Internal.Ast
{
    internal class BinaryExpression : Expression
    {
        public BinaryExpression(Token binaryOperator, Expression left, Expression right)
        {
            Operator = binaryOperator;
            Left = left;
            Right = right;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public Token Operator { get; private set; }
        public Expression Left { get; private set; }
        public Expression Right { get; private set; }
    }
}
