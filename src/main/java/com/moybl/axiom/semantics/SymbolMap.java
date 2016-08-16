package com.moybl.axiom.semantics;

import com.moybl.axiom.ast.*;

import java.util.*;

public class SymbolMap {

	private Map<Node, List<AssignmentExpression>> mapping;
	private int scope;

	private Map<Node, Integer> scopes;
	private Map<Node, AssignmentExpression> initializations;

	public SymbolMap() {
		mapping = new HashMap<>();
		scope = 0;
		scopes = new HashMap<>();
		initializations = new HashMap<>();
	}

	public void enterScope() {
		scope++;
	}

	public void exitScope() {
		List<Node> names = new ArrayList<>();
		names.addAll(mapping.keySet());

		for (int i = 0; i < names.size(); i++) {
			remove(names.get(i));
		}

		scope--;
	}

	public void insert(Node name, AssignmentExpression initialization) {
		List<AssignmentExpression> init = null;
		init = mapping.get(name);

		if (init == null) {
			init = new ArrayList<>();

			init.add(initialization);
			scopes.put(initialization, scope);
			mapping.put(name, init);

			return;
		}

		if (init.size() == 0 || !scopes.containsKey(init.get(0))) {
			throw SemanticException.internal();
		}

		if (scopes.get(init.get(0)) == scope) {
			throw SemanticException.illegalInsert();
		}

		init.add(initialization);
		scopes.put(initialization, scope);
	}

	public void remove(Node name) {
		List<AssignmentExpression> init = mapping.get(name);

		if (init == null) {
			throw SemanticException.illegalRemove();
		}

		if (init.size() == 0 || !scopes.containsKey(init.get(0))) {
			throw SemanticException.internal();
		}

		if (scopes.get(init.get(0)) < scope) {
			throw SemanticException.illegalRemove();
		}

		init.remove(0);

		if (init.size() == 0) {
			mapping.remove(name);
		}
	}

	public AssignmentExpression find(Node name) {
		List<AssignmentExpression> init = mapping.get(name);

		if (init != null) {
			if (init.size() == 0) {
				return null;
			} else {
				return init.get(0);
			}
		}

		return null;
	}

	public void setInitialization(Identifier name, AssignmentExpression assignmentExpression) {
		initializations.put(name, assignmentExpression);
	}

	public Integer getScope(Node node) {
		return scopes.get(node);
	}

	public AssignmentExpression getInitialization(Node node) {
		return initializations.get(node);
	}

	public boolean isInitialized(Node node) {
		return initializations.containsKey(node);
	}

}
