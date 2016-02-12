﻿using Axiom.Internal.Ast;

namespace Axiom.Internal
{
    internal interface IVisitor
    {
        void Visit(BinaryExpression acceptor);
        void Visit(UnaryExpression acceptor);
        void Visit(SequenceExpression acceptor);
        void Visit(BlockStatement acceptor);
        void Visit(ConditionalExpression acceptor);
        void Visit(Literal acceptor);
        void Visit(WhileStatement acceptor);
        void Visit(CallExpression acceptor);
        void Visit(FunctionExpression acceptor);
        void Visit(ReferenceExpression acceptor);
        void Visit(IfStatement acceptor);
        void Visit(ForStatement acceptor);
        void Visit(Identifier acceptor);
        void Visit(MemberExpression acceptor);
        void Visit(AssignmentExpression acceptor);
    }
}
