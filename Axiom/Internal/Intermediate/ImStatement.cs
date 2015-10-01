using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Axiom.Internal.Intermediate
{
    internal abstract class ImStatement : ImCode
    {
        public abstract ImSequence Linear();
    }
}
