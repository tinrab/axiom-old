using System.IO;

namespace Axiom.Internal.Intermediate
{
    internal class ImMemoryRead : ImExpression
    {
        public ImMemoryRead(ImExpression expression)
        {
            Expression = expression;
        }

        public override void Dump(TextWriter tw, int ident)
        {
            Report.Dump(tw, ident, "MemoryRead");
            Expression.Dump(tw, ident + 1);
        }

        public override ImExpressionSequence Linear()
        {
            var lin = Expression.Linear();

            lin.Expression = new ImMemoryRead(lin.Expression);

            return lin;
        }

        public ImExpression Expression { get; set; }
    }
}
