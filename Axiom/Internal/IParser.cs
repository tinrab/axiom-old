using Axiom.Internal.Ast;
using System;

namespace Axiom.Internal
{
    internal interface IParser : IDisposable
    {
        AstNode Parse();
    }
}
