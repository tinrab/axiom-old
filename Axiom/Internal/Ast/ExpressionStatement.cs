namespace Axiom.Internal.Ast
{
    internal class ExpressionStatement : Statement
    {
        public ExpressionStatement(Expression expression)
        {
            Expression = expression;
        }

        public override void Accept(IVisitor visitor)
        {
            Expression.Accept(visitor);
        }

        public Expression Expression { get; set; }
    }
}
