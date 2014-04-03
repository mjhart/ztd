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

import mapbuilder.DebugPathFinder;
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
	private List<MapNode> srcs;
	private List<MapNode> spawns;
	
	private MapNode base;
	private DebugPathFinder pf;
	private XmlParser x;
	
	private Vec2i size;
	private double[] wMin = {-71.40794f, 41.82544f};
	private double[] wMax = {-71.40086f, 41.82944f};
	
	public TestFrontEnd(String title, boolean fullscreen) {
		super(title, fullscreen);
		super.startup();
	}
	
	public TestFrontEnd(String title, boolean fullscreen, Vec2i size) {
		super(title, fullscreen, size);
		super.setDebugMode(true);
		
		
		x = new XmlParser();
		File box = Retriever.getBox(-71.40794, 41.82544, -71.40086, 41.82944);
		ways = x.parseBox(box);
		nodes = x.getNodes();
		
		
		base = x.getNodesHash().get("1955930561");
		pf = new DebugPathFinder(base, x.getNodes());
		spawns = pf.findSrcs(x.getNodes(), new Vec2f(-71.40086f, 41.82944f), new Vec2f(-71.40794f, 41.82544f));
		srcs = pf.findPaths(spawns, base, x.getNodes(), x.getWays());
		super.startup();
	}

	@Override
	protected void onTick(long nanosSincePreviousTick) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onDraw(Graphics2D g) {
		// TODO Auto-generated method stub
		for(MapNode n : nodes) {
			g.drawOval(lonToX(n.lon), latToY(n.lat), 1, 1);
		}
		for(MapWay w : ways) {
			List<MapNode> nList = w.getNodes();
			for(int i=1; i<nList.size(); i++) {
				g.drawLine(lonToX(nList.get(i-1).lon), latToY(nList.get(i-1).lat), lonToX(nList.get(i).lon), latToY(nList.get(i).lat));
			}
		}
		
		
		g.setColor(java.awt.Color.RED);
		g.setStroke(new BasicStroke(3));
		///*
		for(MapNode n : srcs) {
			MapNode cur = n;
			MapNode next = cur.getNext();
			while(next!=null) {
				
				g.drawLine(lonToX(cur.lon), latToY(cur.lat), lonToX(next.lon), latToY(next.lat));
				cur = next;
				next = cur.getNext();
			}
			//System.out.println("new path\n");
		}
		//*/
		/*
		for(MapNode n : nodes) {
			
			if(n.getNext()!=null) {
				MapNode cur = n;
				MapNode next = cur.getNext();
				
				g.drawLine(lonToX(cur.lon), latToY(cur.lat), lonToX(next.lon), latToY(next.lat));
				cur = next;
				next = cur.getNext();
			}
			//System.out.println("new path\n");
		}
		*/
		g.setColor(java.awt.Color.BLUE);
		/*
		for(MapNode n : nodes) {

			if(n.getNext()!=null) {
				g.drawString(Double.toString(pf.dist.get(n)),lonToX(n.lon), latToY(n.lat));
			}
			//System.out.println("new path\n");
		}
		*/
		
		for(MapNode n : pf.visited) {
			g.drawOval(lonToX(n.lon), latToY(n.lat), 1, 1);
		}
		
		g.setColor(java.awt.Color.ORANGE);
		for(MapNode n : srcs) {
			g.drawOval(lonToX(n.lon), latToY(n.lat), 3, 3);
		}
		
		g.setColor(java.awt.Color.GREEN);
		g.drawOval(lonToX(base.lon), latToY(base.lat), 3, 3);
		
	}

	@Override
	protected void onKeyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		//System.out.println(e.getKeyCode());
		if(e.getKeyCode()==39) {
			wMax[0]+=0.0005;
			wMin[0]+=0.0005;
		}
		if(e.getKeyCode()==37) {
			wMax[0]-=0.0005;
			wMin[0]-=0.0005;
		}
		if(e.getKeyCode()==38) {
			wMax[1]+=0.0005;
			wMin[1]+=0.0005;
		}
		if(e.getKeyCode()==40) {
			wMax[1]-=0.0005;
			wMin[1]-=0.0005;
		}
		if(e.getKeyCode()==81) {
			wMax[0]-=0.0005;
			wMin[0]+=0.0005;
			wMax[1]-=0.0005;
			wMin[1]+=0.0005;			
		}
		if(e.getKeyCode()==65) {
			wMax[0]+=0.0005;
			wMin[0]-=0.0005;
			wMax[1]+=0.0005;
			wMin[1]-=0.0005;			
		}
		
		if(e.getKeyCode()==82) {
			pf.run = true;
			srcs = pf.findPaths(spawns, base, x.getNodes(), x.getWays());
		}

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
