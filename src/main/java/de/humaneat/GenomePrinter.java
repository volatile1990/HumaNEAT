package de.humaneat;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import de.humaneat.core.neat.genes.connection.ConnectionGene;
import de.humaneat.core.neat.genes.node.NodeGene;
import de.humaneat.core.neat.genes.node.NodeGeneType;
import de.humaneat.core.neat.genome.Genome;

public class GenomePrinter {

	public static void printGenome(Genome genome, String path) {
		Random r = new Random();
		HashMap<Integer, Point> nodeGenePositions = new HashMap<>();
		int nodeSize = 20;
		int connectionSizeBulb = 6;
		int imageSize = 512;

		BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);

		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, imageSize, imageSize);

		g.setColor(Color.BLUE);
		for (NodeGene gene : genome.nodes.values()) {
			if (gene.type == NodeGeneType.INPUT) {
				float x = gene.innovationNumber / (countNodesByType(genome, NodeGeneType.INPUT) + 1f) * imageSize;
				float y = imageSize - nodeSize / 2;
				g.setColor(new Color(255, 0, 0));
				g.fillOval((int) (x - nodeSize / 2), (int) (y - nodeSize / 2), nodeSize, nodeSize);
				nodeGenePositions.put(gene.innovationNumber, new Point((int) x, (int) y));
			} else if (gene.type == NodeGeneType.HIDDEN) {
				int x = r.nextInt(imageSize - nodeSize * 2) + nodeSize;
				int y = r.nextInt(imageSize - nodeSize * 3) + (int) (nodeSize * 1.5f);
				g.setColor(new Color(0, 255, 0));
				g.fillOval(x - nodeSize / 2, y - nodeSize / 2, nodeSize, nodeSize);
				nodeGenePositions.put(gene.innovationNumber, new Point(x, y));
			} else if (gene.type == NodeGeneType.OUTPUT) {
				int x = r.nextInt(imageSize - nodeSize * 2) + nodeSize;
				int y = nodeSize / 2;
				g.setColor(new Color(0, 0, 255));
				g.fillOval(x - nodeSize / 2, y - nodeSize / 2, nodeSize, nodeSize);
				nodeGenePositions.put(gene.innovationNumber, new Point(x, y));
			} else if (gene.type == NodeGeneType.BIAS) {
				float x = gene.innovationNumber / (countNodesByType(genome, NodeGeneType.INPUT) + 1f) * imageSize;
				float y = imageSize - nodeSize / 2;
				g.setColor(new Color(150, 150, 150));
				g.fillOval((int) (x - nodeSize / 2), (int) (y - nodeSize / 2), nodeSize, nodeSize);
				nodeGenePositions.put(gene.innovationNumber, new Point((int) x, (int) y));
			}
		}

		g.setColor(Color.BLACK);
		for (ConnectionGene gene : genome.connections.values()) {

			if (gene.from == null || gene.to == null) {
				continue;
			}

			if (!gene.enabled) {
				continue;
			}
			Point inNode = nodeGenePositions.get(gene.from.innovationNumber);
			Point outNode = nodeGenePositions.get(gene.to.innovationNumber);

			if (inNode == null || outNode == null) {
				continue;
			}

			Point lineVector = new Point((int) ((outNode.x - inNode.x) * 0.95f), (int) ((outNode.y - inNode.y) * 0.95f));

			g.drawLine(inNode.x, inNode.y, inNode.x + lineVector.x, inNode.y + lineVector.y);
			g.fillRect(inNode.x + lineVector.x - connectionSizeBulb / 2, inNode.y + lineVector.y - connectionSizeBulb / 2, connectionSizeBulb, connectionSizeBulb);
			g.drawString("" + gene.weight, (int) (inNode.x + lineVector.x * 0.25f + 5), (int) (inNode.y + lineVector.y * 0.25f));
		}

		g.setColor(Color.WHITE);
		for (NodeGene nodeGene : genome.nodes.values()) {
			Point p = nodeGenePositions.get(nodeGene.innovationNumber);
			g.drawString("" + nodeGene.innovationNumber, p.x, p.y);
		}

		try {
			ImageIO.write(image, "PNG", new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static int countNodesByType(Genome genome, NodeGeneType type) {
		int c = 0;
		for (NodeGene node : genome.nodes.values()) {
			if (node.type == type) {
				c++;
			}
		}
		return c;
	}

}
