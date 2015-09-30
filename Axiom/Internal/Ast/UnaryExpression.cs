namespace Axiom.Internal.Ast
{
    internal class UnaryExpression : Expression
    {
        public UnaryExpression(Token unaryOperator, Expression expression, bool isPostfix = false)
        {
            Operator = unaryOperator;
            Expression = expression;
            IsPostfix = isPostfix;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public Token Operator { get; private set; }
        public Expression Expression { get; private set; }
        public bool IsPostfix { get; private set; }
    }
}
