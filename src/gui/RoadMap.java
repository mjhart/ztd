package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;


import mapbuilder.MapNode;
import mapbuilder.MapWay;

import java.lang.Double;

/**
 * This class extends JPanel and displays the map.
 * @author mmkaplan
 *
 */
@SuppressWarnings("serial")
public class RoadMap extends JPanel {
	
	public final int _width;
	public final int _height;

	
	private boolean clear;
	
	private List<Line2D> _lines;
	private List<Line2D> _paths;
	
	private MaxGuiFrame _gf;

	private List<MapWay> _ways;
	private HashMap<String, MapNode> _nodes;
	
	public RoadMap(MaxGuiFrame gf) {
		super();
		_width = gf._width - gf._consoleh;
		_height =  gf._height;
		_gf = gf;
		
		//_lines stores the streets, while _paths stores the shortest path
		_lines = new ArrayList<Line2D>(0);
		_paths = new ArrayList<Line2D>(0);
		
		clear = false;
		
		setPreferredSize(new Dimension(_width, _height));
		setVisible(true);
		setBackground(java.awt.Color.WHITE);
		
		//The MouseListener is for clicking on the map to choose start and end. The MouseMotionListener is for dragging
		//in order to pan.
		this.addMouseListener(new MyMouseListener());
		
	}
	
	
	/**
	 * This method is called whenever repaint() is called. It draws _lines.
	 */
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
	    Graphics2D g2 = (java.awt.Graphics2D) g;
	    

	    //Draw the roads
//		g2.setStroke(new BasicStroke(3));
		g2.setColor(Color.BLACK);
		for (Line2D line: _lines) {
			g2.draw(line);
		}
		
		
		if (clear == false) {
			
			
		}
		clear = false;

	}

	
	//Clears the map of shortest paths and points
	public void clear() {
		clear = true;
		this.repaint();
	}
	
	/**
	 * This method retrieves the nodes and ways stored in the Box and packages them into Line2D's, which can be drawn.
	 * It also updates the corners of the data box.
	 * @param b A Box containing information on nodes and ways
	 */
	public void drawWays(List<MapWay> ways) {
		 _ways = ways;
		 //For each way, get its endpoints, then make a Line2D out of them and add to _lines
		 for (MapWay way: ways) {
			 List<MapNode> waynodes = way.getNodes();
			 for (int i = 0; i < waynodes.size() - 1; i++) {
				 MapNode s = waynodes.get(i);
				 MapNode t = waynodes.get(i+1);
				 Point2D start = s.getPoint();
				 Point2D end = t.getPoint();
				 _lines.add(new Line2D.Double(start, end));
			 }

		 }
		 System.out.println("Lines " + _lines.size());
		 this.repaint();
	}

	
	

	
	
	/**
	 * Handles clicking to get nearest node as well as dragging in order to zoom
	 * @author mmkaplan
	 *
	 */
	private class MyMouseListener implements MouseListener {

		//Handles clicking to get start or end point
		@Override
		public void mouseClicked(java.awt.event.MouseEvent e) {
			//Get where on map mouse was clicked
			double x = (double) e.getX();
			double y = (double) e.getY();
			//Send out to GuiFrame, which hands to BackEnd
			
			
			
		}

		@Override
		public void mouseEntered(java.awt.event.MouseEvent e) {}
		@Override
		public void mouseExited(java.awt.event.MouseEvent e) {}
		@Override
		public void mouseReleased(java.awt.event.MouseEvent e) {}
		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {}
		
	}
	

	
	
}
