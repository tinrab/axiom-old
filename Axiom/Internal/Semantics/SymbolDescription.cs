using Axiom.Internal.Ast;
using System.Collections.Generic;

namespace Axiom.Internal.Semantics
{
    internal static class SymbolDescription
    {
        public static IDictionary<AstNode, int?> Scopes { get; set; }
        public static IDictionary<AstNode, AssignmentExpression> Initializations { get; set; }

        static SymbolDescription()
        {
            Scopes = new Dictionary<AstNode, int?>();
            Initializations = new Dictionary<AstNode, AssignmentExpression>();
        }
    }
}
