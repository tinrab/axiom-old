using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace Axiom.Internal.Intermediate
{
    internal class ImExpressionSequence : ImExpression
    {
        public ImExpressionSequence(ImStatement statement, ImExpression expression)
        {
            Statement = statement;
            Expression = expression;
        }

        public override void Dump(TextWriter tw, int ident)
        {
            Report.Dump(tw, ident, "ExpressionSequence");
            Statement.Dump(tw, ident + 1);
            Expression.Dump(tw, ident + 1);
        }

        public override ImExpressionSequence Linear()
        {
            var linearStmt = Statement.Linear();
            var linearExpr = Expression.Linear();

            linearStmt.Statements.AddAll(((ImSequence)linearExpr.Statement).Statements);
            linearExpr.Statement = linearStmt;

            return linearExpr;
        }

        public ImStatement Statement { get; private set; }
        public ImExpression Expression { get; private set; }
    }
}
