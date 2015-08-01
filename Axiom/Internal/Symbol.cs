namespace Axiom.Internal
{
    internal class Symbol
    {
        public Symbol() { }

        public Symbol(Token token, Position position, string lexeme)
        {
            Token = token;
            Position = position;
            Lexeme = lexeme;
        }

        public Token Token { get; set; }
        public Position Position { get; set; }
        public string Lexeme { get; set; }

        public override string ToString()
        {
            return string.Format("Symbol({0}, {1}, {2})", Token, Position, Lexeme);
        }
    }
}
