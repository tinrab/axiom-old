namespace Axiom.Internal.Ast
{
    internal class MemberExpression : Expression
    {
        internal enum AccessType
        {
            ListAccess, Property, Call
        }

        public MemberExpression(AccessType type, Expression obj, Expression member)
        {
            Object = obj;
            Member = member;
            Type = type;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public Expression Object { get; private set; }
        public Expression Member { get; private set; }
        public AccessType Type { get; private set; }
    }
}
