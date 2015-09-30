using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Axiom.Internal.Ast
{
    internal class IfStatement : Statement
    {
        public IfStatement(Expression condition, Statement body, Statement elseBody = null)
        {
            Condition = condition;
            Body = body;
            ElseBody = elseBody;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public Expression Condition { get; private set; }
        public Statement Body { get; set; }
        public Statement ElseBody { get; set; }
    }
}
