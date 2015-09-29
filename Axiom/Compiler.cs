using System.IO;
using Axiom.Internal;

namespace Axiom
{
    public class Compiler
    {
        public Program Compile(string source)
        {
            return Compile(new StringReader(source));
        }

        public Program Compile(TextReader textReader)
        {
            var lexer = new Lexer(textReader);
            var parser = new Parser(lexer);

            try {
                parser.Parse();
            } catch (Error e) {
                parser.Dispose();

                throw e;
            }

            return new Program();
        }
    }
}
