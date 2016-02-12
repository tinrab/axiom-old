using System.IO;

namespace Axiom.Internal
{
    internal static class Report
    {
        public static void Dump(TextWriter tw, int ident, string message)
        {
            for (int i = 0; i < ident * 4; i++) {
                tw.Write(" ");
            }

            tw.WriteLine(message);
        }
    }
}
