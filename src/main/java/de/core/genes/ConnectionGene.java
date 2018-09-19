package de.core.genes;

/**
 * @author muellermak
 *
 */
public class ConnectionGene {

	private NodeGene from;
	private NodeGene out;
	private float weight;
	private boolean enabled;
	private int innvoationNumber;

	public float payload;
	public boolean activated;

	/**
	 * @param in
	 * @param out
	 * @param weight
	 * @param enabled
	 * @param innovationNumber
	 */
	public ConnectionGene(NodeGene in, NodeGene out, float weight, boolean enabled, int innovationNumber) {
		this.from = in;
		this.out = out;
		this.weight = weight;
		this.enabled = enabled;
		this.innvoationNumber = innovationNumber;
		this.payload = 0;
		this.activated = false;
	}

	/**
	 * @param to
	 * @param from
	 * @return
	 */
	public ConnectionGene copy(NodeGene from, NodeGene to) {
		return new ConnectionGene(from, to, this.weight, this.enabled, this.innvoationNumber);
	}

	/**
	 *
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the in
	 */
	public NodeGene getFrom() {
		return this.from;
	}

	/**
	 * @return the out
	 */
	public NodeGene getTo() {
		return this.out;
	}

	/**
	 * @return the weight
	 */
	public float getWeight() {
		return this.weight;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * @return the innvoationNumber
	 */
	public int getInnvoationNumber() {
		return this.innvoationNumber;
	}

	/**
	 * @param in the in to set
	 */
	public void setIn(NodeGene in) {
		this.from = in;
	}

	/**
	 * @param out the out to set
	 */
	public void setOut(NodeGene out) {
		this.out = out;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * @param innvoationNumber the innvoationNumber to set
	 */
	public void setInnvoationNumber(int innvoationNumber) {
		this.innvoationNumber = innvoationNumber;
	}

}
