using Axiom.Internal.Ast;
using Axiom.Internal.Semantics;
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
            Variables = new LinkedList<Access>();
            FP = new Temp();
            RV = new Temp();
        }

        public override string ToString()
        {
            return string.Format("FRAME({0})", Label.Name, Level, Label.Name, Size);
        }

        public int Size { get; set; }

        public FunctionExpression Function { get; private set; }
        public int Level { get; private set; }
        public Label Label { get; private set; }
        public LinkedList<Access> Variables { get; private set; }
        public Temp FP { get; private set; }
        public Temp RV { get; private set; }
    }
}
