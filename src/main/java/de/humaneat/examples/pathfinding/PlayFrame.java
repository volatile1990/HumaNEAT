package de.humaneat.examples.pathfinding;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.humaneat.core.neat.ArtificialIntelligence;

public class PlayFrame extends JFrame {

	public static final int CANVAS_WIDTH = 550;
	public static final int CANVAS_HEIGHT = 550;
	public DrawCanvas canvas;

	private static final long serialVersionUID = 1L;

	public PlayFrame() {
		canvas = new DrawCanvas(); // Construct the drawing canvas
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

		// Set the Drawing JPanel as the JFrame's content-pane
		Container cp = getContentPane();
		cp.add(canvas);
		// or "setContentPane(canvas);"

		setDefaultCloseOperation(EXIT_ON_CLOSE); // Handle the CLOSE button
		pack(); // Either pack() the components; or setSize()
		setTitle("......"); // "super" JFrame sets the title
		setVisible(true); // "super" JFrame show
	}

	public class DrawCanvas extends JPanel {

		private static final long serialVersionUID = 1L;

		public List<PathfindingAI> players;

		public DrawCanvas() {
			players = new ArrayList<>();
		}

		// Override paintComponent to perform your own painting
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g); // paint parent's background
			PlayFrame.this.setBackground(Color.BLACK); // set background color for this JPanel

			g.setColor(Color.BLUE);
			for (PathfindingAI ai : players) {
				g.fillOval(ai.currentX * 10, ai.currentY * 10, 10, 10);
			}

			// Target location
			g.setColor(Color.GREEN);
			g.fillOval(450, 490, 15, 15);
		}

		public void drawPlayers(List<ArtificialIntelligence> players) {

			List<PathfindingAI> ais = new ArrayList<>();
			for (ArtificialIntelligence player : players) {
				PathfindingAI ai = (PathfindingAI) player;
				ais.add(ai);
			}

			this.players = ais;
			this.repaint();
		}

		public void update(PathfindingAI pathfindingAI) {
			this.repaint();
		}
	}

}
