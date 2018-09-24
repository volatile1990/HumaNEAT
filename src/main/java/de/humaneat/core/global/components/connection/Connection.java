package de.humaneat.core.global.components.connection;

/**
 * @author MannoR
 *
 */
public abstract class Connection {

	public boolean enabled;
	public int innvoationNumber;

	public double payload;
	public boolean activated;

	/**
	 * @param from
	 * @param to
	 * @param weight
	 * @param enabled
	 * @param innovationNumber
	 * @param circular2
	 */
	public Connection(boolean enabled, int innovationNumber) {

		this.enabled = enabled;
		innvoationNumber = innovationNumber;
		payload = 0;
		activated = false;
	}

	/**
	 * @return
	 */
	public abstract Connection copy();
}
