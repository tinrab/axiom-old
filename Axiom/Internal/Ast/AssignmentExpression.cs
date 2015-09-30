namespace Axiom.Internal.Ast
{
    internal class AssignmentExpression : Expression
    {
        public AssignmentExpression(Token assignmentOperator, Expression left, Expression right)
        {
            Operator = assignmentOperator;
            Destination = left;
            Source = right;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public Expression Destination { get; private set; }
        public Expression Source { get; private set; }
        public Token Operator { get; private set; }
    }
}
