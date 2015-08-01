using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Axiom.Internal
{
    internal class Parser
    {
        private Lexer _lexer;

        public Parser(Lexer lexer)
        {
            _lexer = lexer;
        }

        public void Parse()
        {
            Symbol symbol;

            while ((symbol = _lexer.Next()).Token != Token.Eof) {
                Console.WriteLine(symbol);
            }
        }
    }
}
