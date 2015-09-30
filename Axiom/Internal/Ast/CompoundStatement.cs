using System.Collections.Generic;

namespace Axiom.Internal.Ast
{
    internal class CompoundStatement : Statement
    {
        public CompoundStatement(IList<Statement> statements)
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
