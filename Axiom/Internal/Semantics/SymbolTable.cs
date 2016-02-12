using Axiom.Internal.Ast;
using System.Collections.Generic;

namespace Axiom.Internal.Semantics
{
    internal static class SymbolTable
    {
        private static IDictionary<Identifier, LinkedList<AssignmentExpression>> mapping;
        private static int scope;

        static SymbolTable()
        {
            mapping = new Dictionary<Identifier, LinkedList<AssignmentExpression>>();
            scope = 0;
        }

        public static void NewScope()
        {
            scope++;
        }

        public static void OldScope()
        {
            var names = new List<Identifier>();

            names.AddAll(mapping.Keys);

            foreach (var name in names) {
                Delete(name);
            }

            scope--;
        }

        public static void Insert(Identifier name, AssignmentExpression initialization)
        {
            LinkedList<AssignmentExpression> init = null;

            if (!mapping.ContainsKey(name)) {
                init = new LinkedList<AssignmentExpression>();

                init.AddFirst(initialization);
                SymbolDescription.Scopes[initialization] = scope;
                mapping.Add(name, init);

                return;
            }

            init = mapping[name];

            if (init.Count == 0 || SymbolDescription.Scopes[init.First.Value] == null) {
                Error.Report();

                return;
            }

            if (SymbolDescription.Scopes[init.First.Value] == scope) {
                throw new IllegalInsertException();
            }

            init.AddFirst(initialization);
            SymbolDescription.Scopes[initialization] = scope;
        }

        public static void Delete(Identifier name)
        {
            var init = mapping[name];

            if (init == null) {
                throw new IllegalDeleteException();
            }

            if (init.Count == 0 || SymbolDescription.Scopes[init.First.Value] == null) {
                Error.Report();
            }

            if (SymbolDescription.Scopes[init.First.Value] < scope) {
                throw new IllegalDeleteException();
            }

            init.RemoveFirst();

            if (init.Count == 0) {
                mapping.Remove(name);
            }
        }

        public static AssignmentExpression Find(Identifier name)
        {
            if (mapping.ContainsKey(name)) {
                var init = mapping[name];

                if (init.Count == 0) {
                    return null;
                } else {
                    return init.First.Value;
                }
            } else {
                return null;
            }
        }
    }
}
