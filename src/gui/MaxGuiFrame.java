//package gui;
//
//import java.awt.BorderLayout;
//import java.awt.Dimension;
//import java.util.List;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
//import mapbuilder.MapWay;
//
//
//
///**
// * The GuiFrame class extends JFrame and is the wrapper/main class for the gui. It is the only gui element that can
// * connect to the backend. It has some of its own logic, but mostly passes things off to the backend.
// * @author mmkaplan
// *
// */
//@SuppressWarnings("serial")
//public class MaxGuiFrame extends JFrame {
//
//	public final int _width = 900;
//	public final int _height = 500;
//	public final int _consoleh = 400;
//	private RoadMap _roadmap;
//
//
//	public MaxGuiFrame() {
//		super("ZTD");
//
//		setSize(_width, _height);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setResizable(true);
//
//		this.setLayout(new BorderLayout());
//		_roadmap = new RoadMap(this);		
//		this.add(_roadmap, BorderLayout.WEST);
//		
////		JPanel test = new JPanel();
////		test.setBackground(java.awt.Color.BLACK);
////		test.setPreferredSize(new Dimension(_height, _width - 300));
////		this.add(test);
//		
//		setVisible(true);
//
//	}
//	
//	public void drawWays(List<MapWay> ways) {
//		_roadmap.drawWays(ways);
//	}
//
//
//}