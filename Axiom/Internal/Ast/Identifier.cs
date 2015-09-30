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

        public string Name { get; private set; }
    }
}
