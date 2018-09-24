package de.humaneat.core.global.components.node;

import de.humaneat.core.lstm.genes.node.LstmNodeGene;
import de.humaneat.core.neat.genes.node.NodeGene;

/**
 * @author MannoR
 *
 */
public interface NodeVisitor {

	void visit(NodeGene node);

	void visit(LstmNodeGene node);

}
