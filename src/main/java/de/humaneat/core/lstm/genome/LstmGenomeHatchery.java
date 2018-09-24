package de.humaneat.core.lstm.genome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.humaneat.core.global.Random;
import de.humaneat.core.global.components.node.NodeGeneType;
import de.humaneat.core.lstm.genes.connection.LstmConnectionGene;
import de.humaneat.core.lstm.genes.node.LstmNodeGene;
import de.humaneat.core.neat.Property;

public class LstmGenomeHatchery {

	private LstmGenome genome;

	/**
	 * @param genome
	 */
	public LstmGenomeHatchery(LstmGenome genome) {
		this.genome = genome;
	}

	/**
	 * @param unfitParent
	 * @return
	 */
	public LstmGenome crossover(LstmGenome unfitParent) {

		if (genome.fitness < unfitParent.fitness) {
			throw new IllegalArgumentException("The first given parent for a crossover must have a higher fitness than the second one");
		}

		LstmGenome childGenome = new LstmGenome(genome.anzInputs, genome.anzOutputs);

		childGenome.nodes.clear();
		childGenome.connections.clear();

		crossoverNodes(genome, childGenome);

		// Copy matching connection genes
		Map<Integer, Boolean> childConnectionEnabled = crossoverConnections(genome, unfitParent, childGenome);

		// Clone all inherited connections to connect the childs new nodes
		List<LstmConnectionGene> newConnetions = cloneConnections(childGenome, childConnectionEnabled);

		// Remove parents template connections after its own were created
		childGenome.connections.clear();

		// Add the new connections
		addNewConnections(childGenome, newConnetions);

		childGenome.getLinker().connectNodes();
		childGenome.getValidator().validateIntegrity();

		return childGenome;
	}

	/**
	 * Adds all new connections to the child genome
	 *
	 * @param childGenome
	 * @param newConnetions
	 */
	private void addNewConnections(LstmGenome childGenome, List<LstmConnectionGene> newConnetions) {

		for (LstmConnectionGene newConnection : newConnetions) {

			LstmNodeGene from = newConnection.from;
			LstmNodeGene to = newConnection.to;

			if (childGenome.getValidator().connectionExists(from, to)) {
				continue;
			}

			childGenome.addLstmConnectionGene(newConnection);
		}
	}

	/**
	 * Clones all previously added connections
	 *
	 * @param childGenome
	 * @param childConnectionEnabled
	 * @return
	 */
	private List<LstmConnectionGene> cloneConnections(LstmGenome childGenome, Map<Integer, Boolean> childConnectionEnabled) {

		List<LstmConnectionGene> newConnetions = new ArrayList<>();
		for (LstmConnectionGene connection : childGenome.connections.values()) {

			LstmNodeGene from = childGenome.nodes.get(connection.from.innovationNumber);
			LstmNodeGene to = childGenome.nodes.get(connection.to.innovationNumber);
			Boolean enabled = childConnectionEnabled.get(connection.innvoationNumber);

			LstmConnectionGene newConnection = new LstmConnectionGene(from, to, enabled, connection.innvoationNumber);
			newConnetions.add(newConnection);
		}

		return newConnetions;
	}

	/**
	 * Compares all connection genes of both given genomes
	 * Randomly takes a connection from fit or unfit parent if present in both
	 * Takes disjoint or excess connection genes from the fit parent
	 *
	 * @param fitParent
	 * @param unfitParent
	 * @param childGenome
	 * @return
	 */
	private Map<Integer, Boolean> crossoverConnections(LstmGenome fitParent, LstmGenome unfitParent, LstmGenome childGenome) {

		Map<Integer, Boolean> childConnectionEnabled = new HashMap<>();
		for (LstmConnectionGene fitParentConnection : fitParent.connections.values()) {

			// Check if the both parents have the connection with same from & to node innovationNumbers
			boolean matchingGene = false;
			LstmConnectionGene unfitConnection = unfitParent.connections.get(fitParentConnection.innvoationNumber);
			if (unfitConnection != null && fitParent.containsConnection(unfitConnection.from.innovationNumber, unfitConnection.to.innovationNumber)) {
				matchingGene = true;
			}

			// Check if the connection gene is present in both genomes
			if (matchingGene) {

				// Randomly take connection from first or second parent
				LstmConnectionGene sourceConnection = null;
				LstmConnectionGene unfitParentConnection = unfitConnection;
				if (Random.random.nextBoolean()) {
					sourceConnection = fitParentConnection;
				} else {
					sourceConnection = unfitParentConnection;
				}

				// If either of the parents connection is disabled there is a chance to disable it for the child
				boolean enabled = true;
				if (!fitParentConnection.enabled || !unfitParentConnection.enabled) {
					if (Random.success(Property.DISABLE_CONNECTION_ON_CHILD_RATE.getValue())) {
						enabled = false;
					}
				}

				// Temporary add connection
				childConnectionEnabled.put(sourceConnection.innvoationNumber, enabled);
				childGenome.addLstmConnectionGene(sourceConnection);

			} else {

				// Disjoint/Excess LstmConnectionGene (Not present in unfit parent) will always be
				// taken from the more fit parent
				childConnectionEnabled.put(fitParentConnection.innvoationNumber, fitParentConnection.enabled);
				childGenome.addLstmConnectionGene(fitParentConnection);
			}
		}

		return childConnectionEnabled;
	}

	/**
	 * Copies all nodes from the more fit parent
	 *
	 * @param fitParent
	 * @param childGenome
	 */
	private void crossoverNodes(LstmGenome fitParent, LstmGenome childGenome) {

		// Copy nodes from the most fit parent
		for (LstmNodeGene node : fitParent.nodes.values()) {

			LstmNodeGene copy = node.copy();
			childGenome.addLstmNodeGene(copy);

			// Set bias
			if (node.type == NodeGeneType.BIAS) {
				childGenome.biasNode = copy;
			}
		}
	}
}
