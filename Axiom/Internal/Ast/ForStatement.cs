using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Axiom.Internal.Ast
{
    internal class ForStatement : Statement
    {
        public ForStatement(Statement init, Expression condition, Statement loop, Statement body)
        {
            Init = init;
            Condition = condition;
            Loop = loop;
            Body = body;
        }

        public override void Accept(IVisitor visitor)
        {
            visitor.Visit(this);
        }

        public Statement Init { get; private set; }
        public Expression Condition { get; private set; }
        public Statement Loop { get; set; }
        public Statement Body { get; set; }
    }
}
