using System.Collections.Generic;

namespace Axiom.Internal.Ast
{
    internal class SequenceExpression : Expression
    {
        public SequenceExpression(IList<Expression> expressions)
        {
            Expressions = expressions;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public IList<Expression> Expressions { get; private set; }
    }
}
