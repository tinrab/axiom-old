using System.IO;

namespace Axiom.Internal.Intermediate
{
    internal class ImExpressionStatement : ImStatement
    {
        public ImExpressionStatement(ImExpression expression)
        {
            Expression = expression;
        }

        public override void Dump(TextWriter tw, int ident)
        {
            Report.Dump(tw, ident, "ExpressionStatement");
            Expression.Dump(tw, ident + 1);
        }

        public override ImSequence Linear()
        {
            var linear = new ImSequence();
            var linearExpr = Expression.Linear();

            linear.Statements.AddAll(((ImSequence)linearExpr.Statement).Statements);
            linear.Statements.Add(new ImExpressionStatement(linearExpr.Expression));

            return linear;
        }

        public ImExpression Expression { get; private set; }
    }
}
