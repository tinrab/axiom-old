import com.moybl.axiom.im.*;

public class DebugImVisitor implements ImVisitor {

	private int ident;

	@Override
	public void visit(ImBinaryOperation acceptor) {
		print("BinaryOperation(%s)", acceptor.getOperator());
		ident++;
		acceptor.getLeft().accept(this);
		acceptor.getRight().accept(this);
		ident--;
	}

	@Override
	public void visit(ImConstant acceptor) {
		print("%s(%s)", acceptor.getKind(), acceptor.getValue());
	}

	@Override
	public void visit(ImExpressionSequence acceptor) {
		print("ExpressionSequence");
		ident++;
		acceptor.getExpression().accept(this);
		acceptor.getStatement().accept(this);
		ident--;
	}

	@Override
	public void visit(ImExpressionStatement acceptor) {
		print("ExpressionStatement");
		ident++;
		acceptor.getExpression().accept(this);
		ident--;
	}

	@Override
	public void visit(ImLabel acceptor) {
		print("Label(%s)", acceptor.getLabel().getName());
	}

	@Override
	public void visit(ImMemoryRead acceptor) {
		print("MemoryRead");
		ident++;
		acceptor.getExpression().accept(this);
		ident--;
	}

	@Override
	public void visit(ImMemoryWrite acceptor) {
		print("MemoryWrite");
		ident++;
		print("destination");
		acceptor.getDestination().accept(this);
		print("source");
		acceptor.getSource().accept(this);
		ident--;
	}

	@Override
	public void visit(ImSequence acceptor) {
		print("Sequence");
		ident++;
		for (int i = 0; i < acceptor.getStatements().size(); i++) {
			acceptor.getStatements().get(i).accept(this);
		}
		ident--;
	}

	@Override
	public void visit(ImTemp acceptor) {
		print("Temp(%s)", acceptor.getTemp().getName());
	}

	private void print(String format, Object... args) {
		for (int i = 0; i < ident * 3; i++) {
			System.out.print(" ");
		}

		System.out.printf(format + "\n", args);
	}

}
