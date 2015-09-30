using Axiom.Internal.Ast;
using System.Collections.Generic;

namespace Axiom.Internal.Semantics
{
    internal static class SymbolTable
    {
        private static IDictionary<string, LinkedList<AssignmentExpression>> mapping;
        private static int scope;

        static SymbolTable()
        {
            mapping = new Dictionary<string, LinkedList<AssignmentExpression>>();
            scope = 0;
        }

        public static void NewScope()
        {
            scope++;
        }

        public static void OldScope()
        {
            var names = new LinkedList<string>();

            foreach (var name in mapping.Keys) {
                names.AddLast(name);

                Delete(name);
            }

            scope--;
        }

        public static void Insert(string name, AssignmentExpression definition)
        {
            LinkedList<AssignmentExpression> init = null;

            if (!mapping.ContainsKey(name)) {
                init = new LinkedList<AssignmentExpression>();

                init.AddFirst(definition);
                SymbolDescription.Scopes[definition] = scope;
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

            init.AddFirst(definition);
            SymbolDescription.Scopes[definition] = scope;
        }

        public static void Delete(string name)
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

        public static AssignmentExpression Find(string name)
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
