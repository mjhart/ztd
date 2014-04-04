package gui;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mapbuilder.DistConverter;
import mapbuilder.MapNode;
import mapbuilder.MapWay;
import mapbuilder.PathFinder;
import mapbuilder.Retriever;
import mapbuilder.XmlParser;

import cs195n.CS195NFrontEnd;
import cs195n.SwingFrontEnd;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class TestFrontEnd extends SwingFrontEnd {

	private List<MapNode> nodes;
	private List<MapWay> ways;
	private List<MapWay> highs;
	private List<MapNode> srcs;
	
	private MapNode base;
	
	private Vec2i size;
	private double[] wMin = {0,0};
	private double[] wMax = {0,0};
	
	public TestFrontEnd(String title, boolean fullscreen) {
		super(title, fullscreen);
		super.startup();
	}
	
	public TestFrontEnd(String title, boolean fullscreen, Vec2i size) {
		super(title, fullscreen, size);
		super.setDebugMode(true);
		
//		File sta = Retriever.getFromAddress("228 East Meade Street, Philadelphia, PA");
		File sta = Retriever.getFromAddress("69 Brown Street, Providence, RI");
//		File sta = Retriever.getFromAddress("Av. Andres Bello 2800, Las Condes, Santiago, Chile");
//		File sta = Retriever.getFromAddress("8 East 86th Street, New York, United States");
//		File sta = Retriever.getFromAddress("Eiffel Tower");

			

		XmlParser x = new XmlParser();
		MapNode cent = x.parseAddress(sta);
		DistConverter dc = new DistConverter(cent.lat, cent.lon);
		System.out.println(dc.getLeft(cent.lon));
		System.out.println(dc.getBott(cent.lat));
		System.out.println(dc.getRight(cent.lon));
		System.out.println(dc.getTop(cent.lat));
		wMin[0] = dc.getLeft(cent.lon);
		wMin[1] = dc.getBott(cent.lat);
		wMax[0] = dc.getRight(cent.lon);
		wMax[1] = dc.getTop(cent.lat);

		
		File box = Retriever.getBox(dc.getLeft(cent.lon), dc.getBott(cent.lat), dc.getRight(cent.lon), dc.getTop(cent.lat));
		
//		XmlParser x = new XmlParser();
//		File box = Retriever.getBox(-71.40794, 41.82544, -71.40086, 41.82944);
		x.parseBox(box);
		ways = x.getWays();
		nodes = x.getNodes();
		highs = x.getHighs();
		
//		PathFinder pf = new PathFinder();
//		base = x.getNodesHash().get("5980360728");
//		srcs = pf.findSrcs(x.getNodes(), new Vec2f(-71.40086f, 41.82944f), new Vec2f(-71.40794f, 41.82544f));
//		srcs = pf.findPaths(srcs, base, x.getNodes(), x.getWays());
		super.startup();
	}

	@Override
	protected void onTick(long nanosSincePreviousTick) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onDraw(Graphics2D g) {
		for(MapNode n : nodes) {
			g.drawOval(lonToX(n.lon), latToY(n.lat), 1, 1);
		}
		for(MapWay w : ways) {
			List<MapNode> nList = w.getNodes();
			for(int i=1; i<nList.size(); i++) {
				g.drawLine(lonToX(nList.get(i-1).lon), latToY(nList.get(i-1).lat), lonToX(nList.get(i).lon), latToY(nList.get(i).lat));
			}
		}
		
		g.setColor(java.awt.Color.GREEN);
		for(MapWay h : highs) {
			List<MapNode> nList = h.getNodes();
			for(int i=1; i<nList.size(); i++) {
				g.drawLine(lonToX(nList.get(i-1).lon), latToY(nList.get(i-1).lat), lonToX(nList.get(i).lon), latToY(nList.get(i).lat));
			}
		}
		
		g.setColor(java.awt.Color.RED);
		g.draw(new Console(0,0,50,50,g));
		

		g.setStroke(new BasicStroke(3));
//		for(MapNode n : srcs) {
//			MapNode cur = n;
//			MapNode next = cur.getNext();
//			while(next!=null) {
//				g.drawLine(lonToX(cur.lon), latToY(cur.lat), lonToX(next.lon), latToY(next.lat));
//				//System.out.println(String.format("Drawing line from : %d %d to %d %d", lonToX(cur.lon), latToY(cur.lat), lonToX(cur.lon), latToY(cur.lat)));
//				cur = next;
//				next = next.getNext();
//			}
//			//System.out.println("new path\n");
//		}
		//System.exit(0);
		
		g.setColor(java.awt.Color.ORANGE);
//		for(MapNode n : srcs) {
//			g.drawOval(lonToX(n.lon), latToY(n.lat), 3, 3);
//		}
		
		g.setColor(java.awt.Color.GREEN);
//		g.drawOval(lonToX(base.lon), latToY(base.lat), 3, 3);
		
	}

	@Override
	protected void onKeyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onKeyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResize(Vec2i newSize) {
		// TODO Auto-generated method stub
		size = newSize;
	}
	
	public static void main(String[] args) {
		//new TestFrontEnd("ZTD", true);
		new TestFrontEnd("ZTD", false, new Vec2i(600, 500));
	}
	
	private int latToY(double lat) {
		return (int) ((wMax[1]-lat)/(wMax[1]-wMin[1]) * size.y);
	}
	
	private int lonToX(double lon) {
		return (int) ((lon - wMin[0])/(wMax[0]-wMin[0]) * size.x);
	}
}
