using System.Collections.Generic;

namespace Axiom.Internal.Ast
{
    internal class Program : Statement
    {
        public Program(IList<Statement> statements)
        {
            Statements = statements;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public IList<Statement> Statements { get; private set; }
    }
}
