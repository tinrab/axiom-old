namespace Axiom.Internal.Frames
{
    internal class Temp
    {
        private static int count;

        public Temp(string name)
        {
            Name = name;
        }

        public override bool Equals(object obj)
        {
            return Name == ((Temp)obj).Name;
        }

        public static bool operator ==(Temp lhs, Temp rhs)
        {
            return lhs.Equals(rhs);
        }

        public static bool operator !=(Temp lhs, Temp rhs)
        {
            return !lhs.Equals(rhs);
        }

        public override int GetHashCode()
        {
            return Name.GetHashCode();
        }

        public string Name { get; private set; }

        public static Temp Create()
        {
            return new Temp("T" + (count++).ToString());
        }

        public static Temp Create(string name)
        {
            return new Temp("_" + name);
        }
    }
}
