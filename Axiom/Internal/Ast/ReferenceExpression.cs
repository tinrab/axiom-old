namespace Axiom.Internal.Ast
{
    internal class ReferenceExpression : Expression
    {
        internal enum ReferenceType
        {
            This, Base
        }

        public ReferenceExpression(ReferenceType type)
        {
            Type = type;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public ReferenceType Type { get; private set; }
    }
}
