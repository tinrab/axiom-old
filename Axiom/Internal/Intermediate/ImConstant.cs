using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace Axiom.Internal.Intermediate
{
    internal class ImConstant : ImExpression
    {
        public ImConstant(object value)
        {
            Value = value;
        }

        public override void Dump(TextWriter tw, int ident)
        {
            Report.Dump(tw, ident, "Constant(" + Value + ")");
        }

        public override ImExpressionSequence Linear()
        {
            return new ImExpressionSequence(new ImSequence(), this);
        }

        public object Value { get; private set; }
    }
}
