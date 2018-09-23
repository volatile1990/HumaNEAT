package de.humaneat.core.neat.genes.node;

import java.util.ArrayList;
import java.util.List;

import de.humaneat.core.global.components.node.Node;
import de.humaneat.core.global.components.node.NodeVisitor;
import de.humaneat.core.neat.genes.connection.ConnectionGene;

/**
 * @author muellermak
 *
 */
public class NodeGene extends Node {

	public List<ConnectionGene> outputConnections;

	/**
	 * @param type
	 * @param id
	 */
	public NodeGene(NodeGeneType type, int innovationNumber) {

		this.type = type;
		this.innovationNumber = innovationNumber;

		inputSum = 0;
		outputValue = 0;

		outputConnections = new ArrayList<>();
	}

	/**
	 *
	 */
	public NodeGene copy() {
		return new NodeGene(type, innovationNumber);
	}

	/**
	 * @param visitor
	 */
	public void accept(NodeVisitor visitor) {
		visitor.visit(this);
	}
}
