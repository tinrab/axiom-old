using System.IO;
using Axiom.Internal;

namespace Axiom
{
    public class Engine
    {
        public Program Compile(string source)
        {
            return Compile(new StringReader(source));
        }

        public Program Compile(TextReader textReader)
        {
            using (var lexer = new Lexer(textReader)) {
                var parser = new Parser(lexer);

                parser.Parse();
            }

            return new Program();
        }
    }
}
