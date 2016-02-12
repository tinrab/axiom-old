namespace Axiom.Internal.Frames
{
    internal class Access
    {
        public Access(string id, Frame frame)
        {
            Id = id;
            Frame = frame;
        }

        public string Id { get; private set; }
        public Frame Frame { get; private set; }
    }
}
