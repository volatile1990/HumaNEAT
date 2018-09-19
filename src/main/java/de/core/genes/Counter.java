package de.core.genes;

/**
 * @author muellermak
 *
 */
public class Counter {

	private int current = 0;

	/**
	 * @return
	 */
	public int getNext() {
		return this.current++;
	}

	/**
	 * @param current
	 */
	public void setCurrent(int current) {
		this.current = current;
	}

	/**
	 * @return
	 */
	public int getCurrent() {
		return this.current;
	}
}
