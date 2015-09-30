using Axiom.Internal.Ast;

namespace Axiom.Internal
{
    internal interface IVisitor
    {
        void Visit(Program acceptor);
        void Visit(BinaryExpression acceptor);
        void Visit(UnaryExpression acceptor);
        void Visit(SequenceExpression acceptor);
        void Visit(CompoundStatement acceptor);
        void Visit(ConditionalExpression acceptor);
        void Visit(Literal acceptor);
        void Visit(ReferenceExpression acceptor);
        void Visit(Identifier acceptor);
        void Visit(MemberExpression acceptor);
        void Visit(AssignmentExpression acceptor);
    }
}
