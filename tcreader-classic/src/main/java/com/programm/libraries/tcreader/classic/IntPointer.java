package com.programm.libraries.tcreader.classic;

public class IntPointer {

	private int index;

	public IntPointer() {

	}

	public IntPointer(int index) {
		this.index = index;
	}

	public void set(int i) {
		this.index = i;
	}

	public void add(int i) {
		this.index += i;
	}

	public int get() {
		return index;
	}

	@Override
	public String toString() {
		return Integer.toString(index);
	}

}
