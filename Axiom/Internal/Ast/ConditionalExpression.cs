namespace Axiom.Internal.Ast
{
    internal class ConditionalExpression : Expression
    {
        public ConditionalExpression(Expression test, Expression consequent, Expression alternate)
        {
            Test = test;
            Consequent = consequent;
            Alternate = alternate;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public Expression Test { get; private set; }
        public Expression Consequent { get; private set; }
        public Expression Alternate { get; private set; }
    }
}
