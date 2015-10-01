using Axiom.Internal.Frames;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace Axiom.Internal.Intermediate
{
    internal class ImChunk
    {
        public ImChunk(Frame frame, ImStatement code)
        {
            Frame = frame;
            Code = code;
        }

        public void Dump(TextWriter tw)
        {
            Report.Dump(tw, 0, "Chunk(" + Frame.Label.Name + ")");
            Report.Dump(tw, 1, Frame.ToString());

            Code.Dump(tw, 2);
        }

        public Frame Frame { get; private set; }
        public ImStatement Code { get; private set; }
        public ImStatement LinearCode { get; set; }
    }
}
