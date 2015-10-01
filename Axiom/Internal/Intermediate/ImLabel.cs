using Axiom.Internal.Frames;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace Axiom.Internal.Intermediate
{
    internal class ImLabel : ImStatement
    {
        public ImLabel(Label label)
        {
            Label = label;
        }

        public override void Dump(TextWriter tw, int ident)
        {
            Report.Dump(tw, ident, "LABEL(" + Label.Name +")");
        }

        public override ImSequence Linear()
        {
            var linear = new ImSequence();

            linear.Statements.Add(this);

            return linear;
        }

        public override bool Equals(object obj)
        {
            if (obj is ImLabel) {
                return ((ImLabel)obj).Label.Name == Label.Name;
            }

            return false;
        }

        public override int GetHashCode()
        {
            return Label.GetHashCode();
        }

        public Label Label { get; private set; }
    }
}
