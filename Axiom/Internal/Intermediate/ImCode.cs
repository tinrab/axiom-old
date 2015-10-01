using System.IO;

namespace Axiom.Internal.Intermediate
{
    internal abstract class ImCode
    {
        public abstract void Dump(TextWriter tw, int ident);
    }
}
