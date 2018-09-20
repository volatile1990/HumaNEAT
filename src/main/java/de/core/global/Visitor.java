package de.core.global;

import de.core.lstm.genes.LSTMNodeGene;
import de.core.neat.genes.node.NodeGene;

/**
 * @author MannoR
 *
 */
public interface Visitor {

	void visit(NodeGene node);

	void visit(LSTMNodeGene node);
}
