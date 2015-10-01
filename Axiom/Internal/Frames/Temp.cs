using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Axiom.Internal.Frames
{
    internal class Temp
    {
        private static int count;

        public Temp()
        {
            Name = "T" + (count++).ToString();
        }

        public override bool Equals(object obj)
        {
            return Name == ((Temp)obj).Name;
        }

        public static bool operator ==(Temp left, Temp right)
        {
            return left.Equals(right);
        }

        public static bool operator !=(Temp left, Temp right)
        {
            return !left.Equals(right);
        }

        public override int GetHashCode()
        {
            return Name.GetHashCode();
        }

        public string Name { get; private set; }
    }
}
