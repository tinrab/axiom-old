using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace Axiom.Internal.Intermediate
{
    internal class ImSequence : ImStatement
    {
        public ImSequence()
        {
            Statements = new List<ImStatement>();
        }

        public override void Dump(TextWriter tw, int ident)
        {
            Report.Dump(tw, ident, "Sequence");

            foreach (var statement in Statements) {
                statement.Dump(tw, ident + 1);
            }
        }

        public override ImSequence Linear()
        {
            var linear = new ImSequence();

            foreach (var stmt in Statements) {
                var linearStmt = stmt.Linear();

                linear.Statements.Add(linearStmt);
            }

            return linear;
        }

        public IList<ImStatement> Statements { get; set; }
    }
}
