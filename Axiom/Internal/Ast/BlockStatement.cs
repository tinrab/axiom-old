using System.Collections.Generic;

namespace Axiom.Internal.Ast
{
    internal class BlockStatement : Statement
    {
        public BlockStatement(IList<Statement> statements)
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
