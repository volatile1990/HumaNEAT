package de.core.neat.genes.node;

import java.util.ArrayList;

import de.core.global.components.Node;

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

		super(new NodeGeneEngager());

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
}
