using System.IO;

namespace Axiom.Internal.Intermediate
{
    internal class ImBinaryOperation : ImExpression
    {
        internal enum BinaryOperator
        {
            Add, Subtract, Multiply, Divide, Modulo
        }

        public ImBinaryOperation(BinaryOperator oper, ImExpression left, ImExpression right)
        {
            Operator = oper;
            Left = left;
            Right = right;
        }

        public override void Dump(TextWriter tw, int ident)
        {
            Report.Dump(tw, ident, "BinaryOperation(" + Operator + ")");
            Left.Dump(tw, ident + 1);
            Right.Dump(tw, ident + 1);

        }

        public override ImExpressionSequence Linear()
        {
            var left = Left.Linear();
            var right = Right.Linear();
            var stmt = new ImSequence();

            stmt.Statements.AddAll(((ImSequence)left.Statement).Statements);
            stmt.Statements.AddAll(((ImSequence)right.Statement).Statements);

            return new ImExpressionSequence(stmt, new ImBinaryOperation(Operator, left.Expression, right.Expression));
        }

        public BinaryOperator Operator { get; private set; }
        public ImExpression Left { get; private set; }
        public ImExpression Right { get; private set; }

        internal static BinaryOperator OperatorFromToken(Token token)
        {
            switch (token) {
            case Token.Add:
                return BinaryOperator.Add;
            case Token.Subtract:
                return BinaryOperator.Subtract;
            case Token.Multiply:
                return BinaryOperator.Multiply;
            case Token.Divide:
                return BinaryOperator.Divide;
            case Token.Modulo:
                return BinaryOperator.Modulo;
            }

            Error.Report();

            return 0;
        }
    }
}
