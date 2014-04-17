package gui;

import gameEngine.AbstractTower;
import gameEngine.Referee;
import gameEngine.Zombie;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Collection;
import java.util.List;

import mapbuilder.Map;
import mapbuilder.MapNode;
import mapbuilder.MapWay;
import cs195n.SwingFrontEnd;
import cs195n.Vec2i;

public class TestFrontEnd extends SwingFrontEnd {

	private List<MapNode> nodes;
	private List<MapWay> ways;
	private List<MapWay> highs;
	private List<MapNode> srcs;
	
	private MainMenu _mm;
	private Console2 _c;
	private boolean _hasMain;
	private boolean _hasConsole;
	private List<AbstractTower> _towers;
	
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
		_mm = new MainMenu(size.x, size.y);
		_hasMain = true;
//		//_m = new Map("69 Brown Street, Providence, RI", _ref);
//		_ref.setMap(_m);
//		
//		srcs = _m.getSources();
//		///*
//		_ref.startRound();
//		//*/
		super.startup();
	}

	@Override
	protected void onTick(long nanosSincePreviousTick) {
		_ref.tick(nanosSincePreviousTick);
	}

	@Override
	protected void onDraw(Graphics2D g) {
		
		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//AffineTransform af = new AffineTransform(_size.x*(wMax[0]-wMin[0]), 0, 0, -1*_size.y*(wMax[1]-wMin[1]), -1*_size.x*wMin[0]/(wMax[0]-wMin[0]), _size.y*wMax[1]/(wMax[1]-wMin[1]));
		//g.setTransform(af);
		
		
		if (_hasMain) {
			_mm.draw(g);
		}
		else if (_hasConsole) {
			_c.draw(g);
			_m.draw(g);
			System.out.println("Drew Map");
		}
		else {
			//_p.draw();
		}
		
		
	}
	
	public void makeMap(String add) {
		_m = new Map(add, _ref);
		System.out.println("Made Map");
		_c = new Console2(0,0,_size.x,_size.y);
		_hasConsole = true;
		_hasMain = false;
	}

	@Override
	protected void onKeyTyped(KeyEvent e) {

	}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		String s = Character.toString(e.getKeyChar());
		if (_hasMain) {

			if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				s = "backspace";
				System.out.println("back");
			}
			else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				s = "enter";
			}
			else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				s = ""; //Don't print shift
			}
			_mm.keyTyped(s);
		}
		
		
		
		if (s.equals("p")) {
			_c.setResources(87);
		}

		// TODO Auto-generated method stub
//		_m.keyPressed(e);
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
		if (_hasMain) {
			String add = _mm.contains(e.getX(), e.getY());
			System.out.println("MM returned: " + add);
			if (add != null) {
				this.makeMap(add);
			}
		}
		else if (_hasConsole) {
			_c.contains(e.getX(), e.getY());
		}

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
		if (_hasConsole) {
			_m.setSize(newSize);
		}
		
	}
	
	public static void main(String[] args) {
		new TestFrontEnd("ZTD", false, new Vec2i(600, 500));
	}
	
	public int latToY(double lat) {
		return (int) ((wMax[1]-lat)/(wMax[1]-wMin[1]) * _size.y);
	}
	
	public int lonToX(double lon) {
		return (int) ((lon - wMin[0])/(wMax[0]-wMin[0]) * _size.x);
	}
	
	public Collection<Zombie> getZombie() {
		return _ref.getZombies();
	}
	

}
