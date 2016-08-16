import com.moybl.axiom.ast.*;
import com.moybl.axiom.semantics.SymbolMap;

import java.util.ArrayList;
import java.util.List;

public class DebugAstVisitor implements Visitor {

	private int ident;
	private SymbolMap symbolMap;

	public DebugAstVisitor(SymbolMap symbolMap) {
		this.symbolMap = symbolMap;
	}

	public void visit(BinaryExpression acceptor) {
		print("BinaryExpression(%s)", acceptor.getOperator());
		ident++;
		acceptor.getLeft().accept(this);
		acceptor.getRight().accept(this);
		ident--;
	}

	public void visit(UnaryExpression acceptor) {
		print("UnaryExpression(%s) %s", acceptor.getOperator(), acceptor.getKind());
		ident++;
		acceptor.getExpression().accept(this);
		ident--;
	}

	public void visit(SequenceExpression acceptor) {
		print("SequenceExpression");
		ident++;
		for (int i = 0; i < acceptor.getExpressions().size(); i++) {
			acceptor.getExpressions().get(i).accept(this);
		}
		ident--;
	}

	public void visit(BlockStatement acceptor) {
		print("Block");
		ident++;
		for (int i = 0; i < acceptor.getStatements().size(); i++) {
			acceptor.getStatements().get(i).accept(this);
		}
		ident--;
	}

	public void visit(ConditionalExpression acceptor) {
		print("ConditionalExpression");

		print("test");
		ident++;
		acceptor.getTest().accept(this);
		ident--;

		print("consequent");
		ident++;
		acceptor.getConsequent().accept(this);
		ident--;

		print("alternate");
		ident++;
		acceptor.getAlternate().accept(this);
		ident--;
	}

	public void visit(Literal acceptor) {
		print("%s(%s)", acceptor.getKind(), acceptor.getValue());
	}

	public void visit(WhileStatement acceptor) {
		print("While");

		ident++;
		acceptor.getCondition().accept(this);
		ident--;

		print("do");
		ident++;
		acceptor.getBody().accept(this);
		ident--;
	}

	public void visit(CallExpression acceptor) {
		print("Call");

		print("arguments");
		ident++;
		for (int i = 0; i < acceptor.getArguments().size(); i++) {
			acceptor.getArguments().get(i).accept(this);
		}
		ident--;

		print("callee");
		ident++;
		acceptor.getCallee().accept(this);
		ident--;
	}

	public void visit(FunctionExpression acceptor) {
		List<String> params = new ArrayList<String>();

		for (int i = 0; i < acceptor.getParameters().size(); i++) {
			params.add(acceptor.getParameters().get(i).getName());
		}

		print("FunctionExpression(%s)", String.join(", ", params));

		ident++;
		acceptor.getBody().accept(this);
		ident--;
	}

	public void visit(ReferenceExpression acceptor) {
		print("ReferenceExpression(%s)", acceptor.getKind());
	}

	public void visit(IfStatement acceptor) {
		print("If");
		ident++;
		acceptor.getCondition().accept(this);
		ident--;

		print("then");
		ident++;
		acceptor.getBody().accept(this);
		ident--;

		if (acceptor.getElseBody() != null) {
			print("else");
			ident++;
			acceptor.getElseBody().accept(this);
			ident--;
		}
	}

	public void visit(ForStatement acceptor) {
		print("For");
		ident++;
		acceptor.getCondition().accept(this);
		ident--;

		if (acceptor.getInit() != null) {
			print("init");
			ident++;
			acceptor.getInit().accept(this);
			ident--;
		}

		if (acceptor.getCondition() != null) {
			print("condition");
			ident++;
			acceptor.getCondition().accept(this);
			ident--;
		}

		if (acceptor.getLoop() != null) {
			print("loop");
			ident++;
			acceptor.getLoop().accept(this);
			ident--;
		}

		if (acceptor.getBody() != null) {
			print("body");
			ident++;
			acceptor.getBody().accept(this);
			ident--;
		}
	}

	public void visit(Identifier acceptor) {
		if (symbolMap.isInitialized(acceptor)) {
			AssignmentExpression init = symbolMap.getInitialization(acceptor);

			print("Identifier(%s) #initialized at %s", acceptor.getName(), init.getPosition());
		} else {
			print("Identifier(%s)", acceptor.getName());
		}
	}

	public void visit(MemberExpression acceptor) {
		print("MemberExpression(%s)", acceptor.getKind());

		print("object");
		ident++;
		acceptor.getObject().accept(this);
		ident--;

		print("member");
		ident++;
		acceptor.getMember().accept(this);
		ident--;
	}

	public void visit(AssignmentExpression acceptor) {
		print("AssignmentExpression(%s)", acceptor.getOperator());

		print("source");
		ident++;
		acceptor.getSource().accept(this);
		ident--;

		print("destination");
		ident++;
		acceptor.getDestination().accept(this);
		ident--;
	}

	private void print(String format, Object... args) {
		for (int i = 0; i < ident * 3; i++) {
			System.out.print(" ");
		}

		System.out.printf(format + "\n", args);
	}

}
