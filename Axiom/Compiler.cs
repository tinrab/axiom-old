using System.IO;
using Axiom.Internal;

namespace Axiom
{
    public class Compiler
    {
        public void Compile(string source)
        {
            Compile(new StringReader(source));
        }

        public void Compile(TextReader textReader)
        {
            var lexer = new Lexer(textReader);
            var parser = new Parser(lexer);

            try {
                parser.Parse();
            } catch (Error e) {
                parser.Dispose();

                throw e;
            }
        }
    }
}
