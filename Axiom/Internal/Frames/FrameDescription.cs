using Axiom.Internal.Ast;
using System.Collections.Generic;

namespace Axiom.Internal.Frames
{
    internal static class FrameDescription
    {
        public static IDictionary<AstNode, Frame> Frames { get; set; }
        public static IDictionary<AstNode, Access> Accesses { get; set; }

        static FrameDescription()
        {
            Frames = new Dictionary<AstNode, Frame>();
            Accesses = new Dictionary<AstNode, Access>();
        }
    }
}
