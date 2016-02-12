using System.IO;

namespace Axiom.Internal.Intermediate
{
    internal class ImMemoryWrite : ImStatement
    {
        public ImMemoryWrite(ImExpression destination, ImExpression expression)
        {
            Destination = destination;
            Expression = expression;
        }

        public override void Dump(TextWriter tw, int ident)
        {
            Report.Dump(tw, ident, "MemoryWrite");
            Destination.Dump(tw, ident + 1);
            Expression.Dump(tw, ident + 1);
        }

        public override ImSequence Linear()
        {
            var lin = new ImSequence();
            var dst = Destination.Linear();
            var src = Expression.Linear();

            lin.Statements.AddAll(((ImSequence)dst.Statement).Statements);
            lin.Statements.AddAll(((ImSequence)src.Statement).Statements);

            lin.Statements.Add(new ImMemoryWrite(dst.Expression, src.Expression));

            return lin;
        }

        public ImExpression Destination { get; set; }
        public ImExpression Expression { get; set; }
    }
}
