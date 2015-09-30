namespace Axiom.Internal.Ast
{
    internal class Literal : Expression
    {
        internal enum LiteralType
        {
            Integer,
            Decimal,
            String,
            Nil,
            Logical
        }

        public Literal(object value, LiteralType type)
        {
            Value = value;
            Type = type;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public object Value { get; private set; }
        public LiteralType Type { get; private set; }
    }
}
