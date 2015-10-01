namespace Axiom.Internal.Ast
{
    internal class Identifier : Expression
    {
        public Identifier(string name)
        {
            Name = name;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public override int GetHashCode()
        {
            return Name.GetHashCode();
        }

        public override bool Equals(object obj)
        {
            return Name == ((Identifier)obj).Name;
        }

        public static bool operator ==(Identifier left, Identifier right) {
            return left.Equals(right);
        }

        public static bool operator !=(Identifier left, Identifier right)
        {
            return !left.Equals(right);
        }

        public string Name { get; private set; }
    }
}
