using Axiom.Internal.Ast;

namespace Axiom.Internal.Frames
{
    internal class Access
    {
        public Access(Identifier id, Frame frame)
        {
            Id = id;
            Frame = frame;
        }

        public Identifier Id { get; private set; }
        public Frame Frame { get; private set; }
    }
}
