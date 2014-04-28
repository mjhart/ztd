package gui;

import gameEngine.Referee;
import gameEngine.towers.AbstractTower;
import gameEngine.towers.BasicTower;
import gameEngine.towers.CannonTower;
import gameEngine.towers.FlameTower;
import gameEngine.towers.TowerFactory;
import gameEngine.zombie.Zombie;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mapbuilder.Map;
import mapbuilder.MapNode;
import mapbuilder.MapWay;
import cs195n.SwingFrontEnd;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class TestFrontEnd extends SwingFrontEnd {
	
	private final float CONSOLE_WIDTH = 150;
	static final Vec2i DEFAULT_WINDOW_SIZE = new Vec2i(960, 810);

	private List<MapNode> nodes;
	private List<MapWay> ways;
	private List<MapWay> highs;
	private List<MapNode> srcs;
	private List<Line2D> _highline2D;
	private AffineTransform _at;
	
	private MainMenu _mm;
	private Console2 _c;
	private boolean _hasMain;
	private boolean _hasMap;
	private List<AbstractTower> _towers;
	private AbstractTower _candidate;
	private boolean _validPlace;
	
	private MapNode base;
	
	private Vec2i _size;
	private double[] wMin = {0,0};
	private double[] wMax = {0,0};
	private Map _m;
	private Referee _ref;
	private String _command;
	private TowerFactory _tf;
	
	public TestFrontEnd(String title, boolean fullscreen) {
		super(title, fullscreen);
		super.startup();
	}
	
	public TestFrontEnd(String title, boolean fullscreen, Vec2i size) {
		super(title, fullscreen, size);
		super.setDebugMode(true);
		
		
		
		_tf = new TowerFactory();

		_mm = new MainMenu(size.x, size.y);
		_hasMain = true;
		_hasMap = false;
		
		_highline2D = new ArrayList<>();
		_validPlace = false;
		_candidate = null;
		_at = new AffineTransform(1, 0, 0, 1, 0, 0);
		
		super.startup();
	}

	@Override
	protected void onTick(long nanosSincePreviousTick) {
		if(_ref != null) {
			_ref.tick(nanosSincePreviousTick);
		}
	}

	///*
	@Override
	protected void onDraw(Graphics2D g) {

		//g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//AffineTransform af = new AffineTransform(_size.x*(wMax[0]-wMin[0]), 0, 0, -1*_size.y*(wMax[1]-wMin[1]), -1*_size.x*wMin[0]/(wMax[0]-wMin[0]), _size.y*wMax[1]/(wMax[1]-wMin[1]));
		//g.setTransform(_at);



		if (_hasMain) {
			_mm.draw(g);
		}
		else if (_hasMap) {
			
			

			// draw console
			_c.draw(g);
			
			
			
			
			g.translate(CONSOLE_WIDTH, 0);
			g.scale((_size.x - CONSOLE_WIDTH) / 10000, (float) _size.y / 10000);
			g.setStroke(new BasicStroke(10000 / DEFAULT_WINDOW_SIZE.x));
			
			// draw new tower
			if (_candidate != null) {
				_candidate.drawSimple(g);
				//TODO This radius is not accurate. Also need to translate to correct coord system
				Color holder = g.getColor();
				if (_validPlace) {
					g.setColor(new Color(0f, 1f, 0f, .5f));
				}
				else {
					g.setColor(new Color(1f, 0f, 0f, .5f));
				}
				Ellipse2D e = new Ellipse2D.Float(_candidate.getCoords().x - (float) Math.sqrt(_candidate.getRadius()), _candidate.getCoords().y - (float) Math.sqrt(_candidate.getRadius()), (float) Math.sqrt(_candidate.getRadius()) * 2, (float) Math.sqrt(_candidate.getRadius()) * 2);
				g.fill(e);
				g.setColor(holder);
			}
			
			for(MapWay w : _m.getWays()) {
				List<MapNode> nList = w.getNodes();
				for(int i=1; i<nList.size(); i++) {
					//System.out.println(nList.get(i)._coords);
					//g.drawLine(lonToX(nList.get(i-1).getX()), latToY(nList.get(i-1).getY()), lonToX(nList.get(i).getX()), latToY(nList.get(i).getY()));
					//g.drawLine((int) nList.get(i-1).getX(), (int) nList.get(i-1).getY(), (int) nList.get(i).getX(), (int) nList.get(i).getY());
					g.draw(new Line2D.Float(nList.get(i-1).getX(), nList.get(i-1).getY(), nList.get(i).getX(), nList.get(i).getY()));
				}
			}

			g.setColor(java.awt.Color.GREEN);
			g.setStroke(new BasicStroke(10000 / DEFAULT_WINDOW_SIZE.x * 3));

			
			for (Line2D l: _highline2D) {
				g.draw(l);
			}

			g.setColor(java.awt.Color.BLUE);
			for(MapNode n : _m.getSourceList()) {
				MapNode cur = n;
				MapNode next = cur.getNext();
				while(next!=null) {
					//g.drawLine(lonToX(cur.getX()), latToY(cur.getY()), lonToX(next.getX()), latToY(next.getY()));
					g.draw(new Line2D.Float(cur.getX(), cur.getY(), next.getX(), next.getY()));
					cur = next;
					next = next.getNext();
				}
				//System.out.println("new path\n");
			}
			

			g.setColor(java.awt.Color.ORANGE);
			for(MapNode n : _m.getSourceList()) {
				g.fillOval((int) (n.getX()-35), (int) (n.getY()-35), 70, 70);
			}

			
			g.setColor(java.awt.Color.RED);
			for(Zombie z : _ref.getZombies()) {
				//g.drawOval(lonToX(z.getCoords().x), latToY(z.getCoords().y), 3, 3);
				//z.draw(g, new Vec2i(lonToX(z.getCoords().x), latToY(z.getCoords().y)));
				z.draw(g);
			}

			for(AbstractTower t : _ref.towers()) {

				//t.drawSimple(g);
				t.draw2(g);
			}

		}

	}
	
	public void makeMap(String add) {
		
		_ref = new Referee(_m);
		try {
			_m = new Map(add, _ref);
		} catch (Exception e) {
			e.printStackTrace();
		}
		_ref.setMap(_m);
		wMax = _m.getwMax();
		wMin = _m.getwMin();
		srcs = _m.getSources();

		_c = new Console2(0,0,CONSOLE_WIDTH,_size.y, _tf, _ref);

		_hasMap = true;
		_hasMain = false;
		_mm.clear();
		
		for(MapWay h : _m.getHighways()) {
			List<MapNode> nList = h.getNodes();
			for(int i=1; i<nList.size(); i++) {
				//_highline2D.add(new Line2D.Float(lonToX(nList.get(i-1).getX()), latToY(nList.get(i-1).getY()), lonToX(nList.get(i).getX()), latToY(nList.get(i).getY())));
				_highline2D.add(new Line2D.Float(nList.get(i-1).getX(), nList.get(i-1).getY(), nList.get(i).getX(), nList.get(i).getY()));
				//g.drawLine((int)nList.get(i-1).lon,(int) nList.get(i-1).lat,(int) nList.get(i).lon, (int)nList.get(i).lat);
			}
		}
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
			}
			else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				s = "enter";
			}
			else if (e.getKeyCode() == KeyEvent.VK_TAB) {
				s = "tab";
			}
			else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				s = ""; //Don't print shift
			}
			else if (e.getKeyCode() == KeyEvent.VK_UP) {
				s = ""; //Don't print up
			}
			else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				s = ""; //Don't print left
			}
			else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				s = ""; //Don't print down
			}
			else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				s = ""; //Don't print right
			}
			_mm.keyTyped(s);
		}
		


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


	}

	@Override
	protected void onMouseClicked(MouseEvent e) {
		if (_hasMain) {
			String add = _mm.contains(e.getX(), e.getY(), true);
			System.out.println("MM returned: " + add);
			if (add != null) {
				this.makeMap(add);
			}
		}
		else if (_hasMap) {
			String command = _c.contains(e.getX(), e.getY());
			if (command != null) {
				String[] fw = command.split("\\s+");
				_command = fw[0];
				if (_command.equals("Start")) {
					_ref.startRound();
					_c.unhighlight();
				}
				else if (_command.equals("Main")) {
					_hasMain = true;
					_hasMap = false;
					_c = null;
					_m = null; //TODO reset map (ie zombies)
				}
				else if (_command.equals("Restart")) {
					_ref.restart();
					_c.unhighlight();
					_command = null;
				}
				else if (_command.equals("Pause")) {
					//TODO Pause screen
					_ref.pause();
				}
				else if (_command.equals("Quit")) {
					System.exit(0);
				}
			}
			else if ((e.getX() > CONSOLE_WIDTH) && (_command != null)) {
				//TODO if intersects with a tower, select and have upgrade option
				Rectangle2D r = new Rectangle2D.Double(e.getX() - 5, e.getY() - 5, 10, 10);
				if (_validPlace) {
					if (_command.equals("Basic")) {
						_ref.addTower(_tf.makeBasic(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref));
					}
					else if (_command.equals("Cannon")) {
						_ref.addTower(_tf.makeCannon(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref));
					}
					else if (_command.equals("Electric")) {
						_ref.addTower(_tf.makeElectric(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref));
					}
					else if (_command.equals("Flame")) {
						_ref.addTower(_tf.makeFlame(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref));
					}
					_c.unhighlight();
					_command = null;
					_candidate = null;
				}
			}
		}

	}

	@Override
	protected void onMousePressed(MouseEvent e) {}

	@Override
	protected void onMouseReleased(MouseEvent e) {}

	@Override
	protected void onMouseDragged(MouseEvent e) {}

	@Override
	protected void onMouseMoved(MouseEvent e) {
		if (_hasMain) {
			_mm.contains(e.getX(), e.getY(), false);
		}
		
		
		
		else if (_hasMap) {
			if ((e.getX() > CONSOLE_WIDTH) && (_command != null)) {
				Rectangle2D r = new Rectangle2D.Double(xToLon(e.getX()) - 50, yToLat(e.getY()) - 50, 100, 100);
				for (Line2D l: _highline2D) {
					if (l.intersects(r)) {
						_validPlace = false;
						break;
					}
					else {
						_validPlace = true;
					}
				}
				//TODO Change this to sprites instead of towers?
				if (_command.equals("Basic")) {
					_candidate = _tf.makeBasic(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref);
				}
				else if (_command.equals("Cannon")) {
					_candidate = _tf.makeCannon(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref);
				}
				else if (_command.equals("Electric")) {
					_candidate = _tf.makeElectric(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref);
				}
				else if (_command.equals("Flame")) {
					_candidate = _tf.makeFlame(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref);
				}
				
				if (_ref.getResources() - _candidate.getPrice() < 0) {
					_candidate = null;
				}

			}
			else {
				_candidate = null;
			}
		}

	}

	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {}

	@Override
	protected void onResize(Vec2i newSize) {
		// TODO we should mess with this
		/*
		_at = new AffineTransform();
		_at.scale(newSize.x/100, newSize.y/100);
		
		if (_size != null) {
			_at.scale(newSize.x - _size.x + 1, newSize.y - _size.y + 1);
		}
		*/
		_size = newSize;
		
		
		if (_hasMap) {
			_m.setSize(newSize);
		}
		
	}
	
	public static void main(String[] args) {
		new TestFrontEnd("ZTD", false, new Vec2i(DEFAULT_WINDOW_SIZE.x, DEFAULT_WINDOW_SIZE.y));
	}
	
	
	public int latToY(double lat) {
		return (int) (lat / 10000 * _size.y);
	}
	
	public int lonToX(double lon) {
		return (int) (lon / 10000 * (float) _size.x + CONSOLE_WIDTH);
	}
	
	private float yToLat(double y) {
		return (float) (y / _size.y * 10000);
	}
	private float xToLon(double x) {
		return (float) ((x - CONSOLE_WIDTH) / (float) (_size.x - CONSOLE_WIDTH) * 10000f);
	}
	

	

	
}
