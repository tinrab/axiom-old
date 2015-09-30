using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Axiom.Internal.Ast
{
    internal class WhileStatement : Statement
    {
        public WhileStatement(Expression condition, Statement body)
        {
            Condition = condition;
            Body = body;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public Expression Condition { get; private set; }
        public Statement Body { get; private set; }
    }
}
