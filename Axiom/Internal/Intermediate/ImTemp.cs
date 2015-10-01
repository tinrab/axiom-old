using Axiom.Internal.Frames;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Axiom.Internal.Intermediate
{
    internal class ImTemp : ImExpression
    {
        public ImTemp(Temp temp)
        {
            Temp = temp;
        }

        public override void Dump(TextWriter tw, int ident)
        {
            Report.Dump(tw, ident, "TEMP(" + Temp.Name + ")");
        }

        public override ImExpressionSequence Linear()
        {
            return new ImExpressionSequence(new ImSequence(), this);
        }

        public Temp Temp { get; private set; }
    }
}
