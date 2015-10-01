namespace Axiom.Internal.Frames
{
    internal class Label
    {
        public Label(string name)
        {
            Name = name;
        }

        public override bool Equals(object obj)
        {
            return Name == ((Label)obj).Name;
        }

        public static bool operator ==(Label left, Label right)
        {
            if (((object)left) == null || ((object)right) == null) {
                return false;
            }

            return left.Equals(right);
        }

        public static bool operator !=(Label left, Label right)
        {
            return !(left == right);
        }

        public override int GetHashCode()
        {
            return Name.GetHashCode();
        }

        public string Name { get; private set; }

        private static int count;

        public static Label Create()
        {
            return new Label("L" + (count++));
        }

        public static Label Create(string name)
        {
            return new Label("_" + name);
        }
    }
}
