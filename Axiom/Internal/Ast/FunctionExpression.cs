using System.Collections.Generic;

namespace Axiom.Internal.Ast
{
    internal class FunctionExpression : Expression
    {
        public FunctionExpression(IList<Identifier> parameters, Statement body)
        {
            Parameters = parameters;
            Body = body;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public IList<Identifier> Parameters { get; private set; }
        public Statement Body { get; set; }
    }
}
