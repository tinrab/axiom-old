package com.moybl.axiom.frame;

public class Label {

	private String name;

	public Label(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		return name.equals(((Label) obj).name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	private static int counter;

	public static Label create() {
		counter++;
		return new Label("L" + counter);
	}

}
