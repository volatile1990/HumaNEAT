package de.core.global.components.node;

import de.core.lstm.genes.LSTMNodeGene;
import de.core.neat.genes.node.NodeGene;

/**
 * @author MannoR
 *
 */
public interface NodeVisitor {

	void visit(NodeGene node);

	void visit(LSTMNodeGene node);
}
