using System.Collections.Generic;

namespace Axiom.Internal.Ast
{
    internal class CallExpression : Expression
    {
        public CallExpression(Expression callee, IList<Expression> arguments)
        {
            Callee = callee;
            Arguments = arguments;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public Expression Callee { get; private set; }
        public IList<Expression> Arguments { get; private set; }
    }
}
