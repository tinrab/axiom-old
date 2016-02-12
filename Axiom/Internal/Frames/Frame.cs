using Axiom.Internal.Ast;
using System.Collections.Generic;

namespace Axiom.Internal.Frames
{
    internal class Frame
    {
        public Frame(Label label, FunctionExpression functionExpression, int level)
        {
            Function = functionExpression;

            Level = level;
            Label = label;
            Variables = new List<Access>();
            FramePointer = Temp.Create();
            ReturnValue = Temp.Create();
        }

        public override string ToString()
        {
            return string.Format("Frame({0})", Label.Name, Level, Label.Name, Size);
        }

        public int Size { get; set; }

        public FunctionExpression Function { get; private set; }
        public int Level { get; private set; }
        public Label Label { get; private set; }
        public IList<Access> Variables { get; private set; }
        public Temp FramePointer { get; private set; }
        public Temp ReturnValue { get; private set; }
    }
}
