using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;

namespace Axiom.Internal
{
    internal static class Report
    {
        public static void Dump(TextWriter tw, int ident, string message)
        {
            for (int i = 0; i < ident; i++) {
                tw.Write(" ");
            }

            tw.WriteLine(message);
        }
    }
}
