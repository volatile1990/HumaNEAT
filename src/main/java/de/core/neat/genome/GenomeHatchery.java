package de.core.neat.genome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.core.global.Random;
import de.core.neat.Property;
import de.core.neat.genes.connection.ConnectionGene;
import de.core.neat.genes.node.NodeGene;
import de.core.neat.genes.node.NodeGeneType;

/**
 * @author MannoR
 *
 *         FSK 18: Produces new little genome babies
 */
public class GenomeHatchery {

	/**
	 * This method is producing a crossover from two genomes to generate a new one.
	 *
	 * @param fitParent   more fit parent
	 * @param unfitParent less fit parent
	 * @return
	 */
	public static Genome crossover(Genome fitParent, Genome unfitParent) {

		if (fitParent.fitness < unfitParent.fitness) {
			throw new IllegalArgumentException("The first given parent for a crossover must have a higher fitness than the second one");
		}

		Genome childGenome = new Genome(fitParent.anzInputs, fitParent.anzOutputs);

		childGenome.nodes.clear();
		childGenome.connections.clear();

		crossoverNodes(fitParent, childGenome);

		// Copy matching connection genes
		Map<Integer, Boolean> childConnectionEnabled = crossoverConnections(fitParent, unfitParent, childGenome);

		// Clone all inherited connections to connect the childs new nodes
		List<ConnectionGene> newConnetions = cloneConnections(childGenome, childConnectionEnabled);

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
	private static void addNewConnections(Genome childGenome, List<ConnectionGene> newConnetions) {

		for (ConnectionGene newConnection : newConnetions) {

			NodeGene from = newConnection.from;
			NodeGene to = newConnection.to;

			if (childGenome.getValidator().connectionExists(from, to)) {
				continue;
			}

			childGenome.addConnectionGene(newConnection);
		}
	}

	/**
	 * Clones all previously added connections
	 *
	 * @param childGenome
	 * @param childConnectionEnabled
	 * @return
	 */
	private static List<ConnectionGene> cloneConnections(Genome childGenome, Map<Integer, Boolean> childConnectionEnabled) {

		List<ConnectionGene> newConnetions = new ArrayList<>();
		for (ConnectionGene connection : childGenome.connections.values()) {

			NodeGene from = childGenome.nodes.get(connection.from.innovationNumber);
			NodeGene to = childGenome.nodes.get(connection.to.innovationNumber);
			Boolean enabled = childConnectionEnabled.get(connection.innvoationNumber);

			ConnectionGene newConnection = new ConnectionGene(from, to, connection.weight, enabled, connection.innvoationNumber);
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
	private static Map<Integer, Boolean> crossoverConnections(Genome fitParent, Genome unfitParent, Genome childGenome) {

		Map<Integer, Boolean> childConnectionEnabled = new HashMap<>();
		for (ConnectionGene fitParentConnection : fitParent.connections.values()) {

			// Check if the both parents have the connection with same from & to node innovationNumbers
			boolean matchingGene = false;
			ConnectionGene unfitConnection = unfitParent.connections.get(fitParentConnection.innvoationNumber);
			if (unfitConnection != null && fitParent.containsConnection(unfitConnection.from.innovationNumber, unfitConnection.to.innovationNumber)) {
				matchingGene = true;
			}

			// Check if the connection gene is present in both genomes
			if (matchingGene) {

				// Randomly take connection from first or second parent
				ConnectionGene sourceConnection = null;
				ConnectionGene unfitParentConnection = unfitConnection;
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
				childGenome.addConnectionGene(sourceConnection);

			} else {

				// Disjoint/Excess ConnectionGene (Not present in unfit parent) will always be
				// taken from the more fit parent
				childConnectionEnabled.put(fitParentConnection.innvoationNumber, fitParentConnection.enabled);
				childGenome.addConnectionGene(fitParentConnection);
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
	private static void crossoverNodes(Genome fitParent, Genome childGenome) {

		// Copy nodes from the most fit parent
		for (NodeGene node : fitParent.nodes.values()) {

			NodeGene copy = node.copy();
			childGenome.addNodeGene(copy);

			// Set bias
			if (node.type == NodeGeneType.BIAS) {
				childGenome.biasNode = copy;
			}
		}
	}
}
