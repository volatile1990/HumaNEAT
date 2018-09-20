package de.core.neat.genes.node;

import java.util.ArrayList;

import de.core.global.Visitor;
import de.core.global.components.node.Node;

/**
 * @author muellermak
 *
 */
public class NodeGene extends Node {

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
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
