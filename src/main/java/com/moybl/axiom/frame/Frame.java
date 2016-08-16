package com.moybl.axiom.frame;

import com.moybl.axiom.ast.FunctionExpression;

import java.util.ArrayList;
import java.util.List;

public class Frame {

	private Label label;
	private FunctionExpression functionExpression;
	private int level;
	private int size;
	private List<Access> variables;
	private Temp framePointer;
	private Temp returnValue;

	public Frame(Label label, FunctionExpression functionExpression, int level) {
		this.label = label;
		this.functionExpression = functionExpression;
		this.level = level;

		variables = new ArrayList<>();
		framePointer = Temp.create();
		returnValue = Temp.create();
	}

	public Label getLabel() {
		return label;
	}

	public FunctionExpression getFunctionExpression() {
		return functionExpression;
	}

	public int getLevel() {
		return level;
	}

	public int getSize() {
		return size;
	}

	public List<Access> getVariables() {
		return variables;
	}

	public Temp getFramePointer() {
		return framePointer;
	}

	public Temp getReturnValue() {
		return returnValue;
	}

}
