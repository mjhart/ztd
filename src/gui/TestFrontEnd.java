package gui;

import gameEngine.BasicZombie;
import gameEngine.Referee;
import gameEngine.SprintZombie;
import gameEngine.Zombie;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.Collection;
import java.util.List;

import mapbuilder.Map;

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
	
	private Vec2i _size;
	private double[] wMin = {0,0};
	private double[] wMax = {0,0};
	private Map _m;
	private Referee _ref;
	
	public TestFrontEnd(String title, boolean fullscreen) {
		super(title, fullscreen);
		super.startup();
	}
	
	public TestFrontEnd(String title, boolean fullscreen, Vec2i size) {
		super(title, fullscreen, size);
		super.setDebugMode(true);
		
		_ref = new Referee(_m);
		try {
			_m = new Map("69 Brown Street, Providence, RI", _ref);
		} catch (Exception e) {
			e.printStackTrace();
		}
		_ref.setMap(_m);
		
		srcs = _m.getSources();
		///*
		_ref.startRound();
		//*/
		super.startup();
	}

	@Override
	protected void onTick(long nanosSincePreviousTick) {
		// TODO Auto-generated method stub
		_ref.tick(nanosSincePreviousTick);
	}

	@Override
	protected void onDraw(Graphics2D g) {
		
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//AffineTransform af = new AffineTransform(_size.x*(wMax[0]-wMin[0]), 0, 0, -1*_size.y*(wMax[1]-wMin[1]), -1*_size.x*wMin[0]/(wMax[0]-wMin[0]), _size.y*wMax[1]/(wMax[1]-wMin[1]));
		//g.setTransform(af);
		_m.draw(g);
		
		g.setColor(java.awt.Color.RED);
		new Console(0,0,(int) _size.x/5,_size.y,g);
		
		/*
		g.setStroke(new BasicStroke(3));
		for(MapNode n : srcs) {
			MapNode cur = n;
			MapNode next = cur.getNext();
			while(next!=null) {
				g.drawLine(lonToX(cur.lon), latToY(cur.lat), lonToX(next.lon), latToY(next.lat));
				cur = next;
				next = next.getNext();
			}
			//System.out.println("new path\n");
		}
		//System.exit(0);
		
		g.setColor(java.awt.Color.ORANGE);
		for(MapNode n : srcs) {
			g.drawOval(lonToX(n.lon), latToY(n.lat), 3, 3);
		}
		
		g.setColor(java.awt.Color.RED);
		for(Zombie z : _ref.getZombies()) {
			g.drawOval(lonToX(z.getCoords().x), latToY(z.getCoords().y), 3, 3);
		}
		*/
	}

	@Override
	protected void onKeyTyped(KeyEvent e) {
		

	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		_m.keyPressed(e);
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
		_size = newSize;
		_m.setSize(newSize);
	}
	
	public static void main(String[] args) {
		new TestFrontEnd("ZTD", false, new Vec2i(600, 500));
	}
	
	private int latToY(double lat) {
		return (int) ((wMax[1]-lat)/(wMax[1]-wMin[1]) * _size.y);
	}
	
	private int lonToX(double lon) {
		return (int) ((lon - wMin[0])/(wMax[0]-wMin[0]) * _size.x);
	}
	
	public Collection<Zombie> getZombie() {
		return _ref.getZombies();
	}
	

}
