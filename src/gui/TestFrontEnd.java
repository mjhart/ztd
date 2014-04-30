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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mapbuilder.Building;
import mapbuilder.Map;
import mapbuilder.MapNode;
import mapbuilder.MapWay;
import cs195n.SwingFrontEnd;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class TestFrontEnd extends SwingFrontEnd {
	
	private final float CONSOLE_WIDTH = 170;
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
	private boolean _showMap;
	private List<AbstractTower> _towers;
	private AbstractTower _candidate;
	private boolean _validPlace;
	private AbstractTower _placedTower;
	
	private MapNode base;
	
	private Vec2i _size;
	private double[] wMin = {0,0};
	private double[] wMax = {0,0};
	private Map _m;
	private Referee _ref;
	private String _command;
	private TowerFactory _tf;
	private Screen _screen;
	private boolean _hasScreen;
	private boolean _wasRunning;
	
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
		_showMap = false;
		_hasScreen = false;
		_wasRunning = false;
		
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
		else if (_showMap) {
			
			

			
			
			
			float defaultstroke = 10000 / DEFAULT_WINDOW_SIZE.x;
			
			g.translate(CONSOLE_WIDTH, 0);
			g.scale((_size.x - CONSOLE_WIDTH) / 10000, (float) _size.y / 10000);
			//g.setStroke(new BasicStroke(10000 / DEFAULT_WINDOW_SIZE.x));
			g.setStroke(new BasicStroke(defaultstroke));

			

			
//			for(MapWay w : _m.getWays()) {
//				List<MapNode> nList = w.getNodes();
//				for(int i=1; i<nList.size(); i++) {
//					//System.out.println(nList.get(i)._coords);
//					//g.drawLine(lonToX(nList.get(i-1).getX()), latToY(nList.get(i-1).getY()), lonToX(nList.get(i).getX()), latToY(nList.get(i).getY()));
//					//g.drawLine((int) nList.get(i-1).getX(), (int) nList.get(i-1).getY(), (int) nList.get(i).getX(), (int) nList.get(i).getY());
//					g.draw(new Line2D.Float(nList.get(i-1).getX(), nList.get(i-1).getY(), nList.get(i).getX(), nList.get(i).getY()));
//				}
//			}



			g.setColor(new Color(144,238,144));
			if (_m.getLanduse() != null) {
				for (Building b: _m.getLanduse()) {
					g.fill(b.getPolygon());
				}
			}
			
			if (_m.getWaterways() != null) {
				g.setColor(new Color(173,216,230));
				for (Building b: _m.getWaterways()) {
					g.fill(b.getPolygon());
				}
			}
			

			g.setColor(new Color(255,222,173));
			g.setStroke(new BasicStroke(defaultstroke * 3));
			for (Line2D l: _highline2D) {
				g.draw(l);
			}
			
			g.setColor(new Color(178,34,34));
			g.setStroke(new BasicStroke(defaultstroke));
			if (_m.getFootways() != null) {
				for(MapWay h : _m.getFootways()) {
					List<MapNode> nList = h.getNodes();
					for(int i=1; i<nList.size(); i++) {
						g.draw(new Line2D.Float(nList.get(i-1).getX(), nList.get(i-1).getY(), nList.get(i).getX(), nList.get(i).getY()));
					}
				}
			}
			
			g.setColor(new Color(255,222,173));
			g.setStroke(new BasicStroke(6*defaultstroke));
			if (_m.getResidential() != null) {
				for(MapWay h : _m.getResidential()) {
					List<MapNode> nList = h.getNodes();
					for(int i=1; i<nList.size(); i++) {
						g.draw(new Line2D.Float(nList.get(i-1).getX(), nList.get(i-1).getY(), nList.get(i).getX(), nList.get(i).getY()));
					}
				}
			}
			
			g.setColor(new Color(255,215,0));
			g.setStroke(new BasicStroke(6*defaultstroke));
			if (_m.getSecondary() != null) {
				for(MapWay h : _m.getSecondary()) {
					List<MapNode> nList = h.getNodes();
					for(int i=1; i<nList.size(); i++) {
						g.draw(new Line2D.Float(nList.get(i-1).getX(), nList.get(i-1).getY(), nList.get(i).getX(), nList.get(i).getY()));
					}
				}
			}
			
			g.setColor(new Color(250,128,114));
			g.setStroke(new BasicStroke(6*defaultstroke));
			if (_m.getTertiary() != null) {
				for(MapWay h : _m.getTertiary()) {
					List<MapNode> nList = h.getNodes();
					for(int i=1; i<nList.size(); i++) {
						g.draw(new Line2D.Float(nList.get(i-1).getX(), nList.get(i-1).getY(), nList.get(i).getX(), nList.get(i).getY()));
					}
				}
			}

			
			
			
			
			
			
			
			
			
			
			
			
			g.setColor(Color.GRAY.brighter());
			for (Building b: _m.getBuildings()) {
				g.fill(b.getPolygon());
			}
			
			//DONT DELETE THIS
			//Getting rid of overlap
//			g.setColor(Color.BLACK);
//			g.setFont(new Font("Helvetica", Font.BOLD, 110));
//			for (Building b: _m.getBuildings()) {
//				Rectangle2D r = b.getPolygon().getBounds();
//				if (b.getName() != null) {
//					String[] namearr = b.getName().split("\\s+");
//					FontMetrics fm = g.getFontMetrics();
//					boolean draw = true;
//					for (int i = 0; i < namearr.length; i++) {
//						if (fm.stringWidth(namearr[i]) > r.getWidth()) {
//							draw = false;
//							break;
//						}
//					}
//					if ((namearr.length * fm.getHeight() < r.getHeight()) && (draw)) {
//						for (int i = 0; i < namearr.length; i++) {
//							g.drawString(namearr[i], (int) r.getX() + 100, (int) r.getCenterY() - 50 + 110*i);
//						}
//					}
//				}
//			}
			//DONT DELETE THIS

			
			//DONT DELETE THIS
			//All names
			g.setColor(Color.BLACK);
			g.setFont(new Font("Helvetica", Font.BOLD, 110));
			for (Building b: _m.getBuildings()) {
				Rectangle2D r = b.getPolygon().getBounds();
				if (b.getName() != null) {
					String[] namearr = b.getName().split("\\s+");
					FontMetrics fm = g.getFontMetrics();
					for (int i = 0; i < namearr.length; i++) {
						g.drawString(namearr[i], (int) r.getX() + 100, (int) r.getCenterY() - 50 + 110*i);
					}
				}
			}
			//DONT DELETE THIS
			
			


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
			
			// draw new tower
			if (_candidate != null) {
				_candidate.draw2(g);
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
			
			g.setColor(java.awt.Color.CYAN);
			
			g.setStroke(new BasicStroke());
			g.setTransform(new AffineTransform());
			// draw console
			_c.draw(g);
			
			

		}
		if (_hasScreen) {
			_screen.draw(g);
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
		_showMap = true;
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
			if (add != null) {
				this.makeMap(add);
			}
		}
		else if (_hasMap) {
			String command = _c.contains(e.getX(), e.getY());
			if (command != null) {
				_c.noUpgrades();
				System.out.println(command);
				String[] fw = command.split("\\s+");
				_command = fw[0];
				parseConsoleControlButton();
			}
			else if ((e.getX() > CONSOLE_WIDTH) && (_command != null)) {
				Rectangle2D r = new Rectangle2D.Double(e.getX() - 5, e.getY() - 5, 10, 10);
				if (_validPlace) {
					_ref.addTower(parseConsoleTowerButton(e));
					_c.unhighlight();
					_command = null;
					_candidate = null;
				}
			}
			else if (command == null) {
				_command = null;
				_c.unhighlight();
				_c.noUpgrades();
				for (AbstractTower t: _ref.towers()) {
					if (t.contains(xToLon(e.getX()), yToLat(e.getY()))) {
						_placedTower = t;
						_c.showUpgrades();
						break;
					}
				}
			}
		}
		else {
			String command = _screen.contains(e.getX(), e.getY());
			if (command != null) {
				String[] fw = command.split("\\s+");
				_command = fw[0];
				parseConsoleControlButton();
			}
			_command = null;
		}

	}
	
	private void parseConsoleControlButton() {
		if (_command.equals("Start")) {
			_ref.startRound();
			_c.unhighlight();
			_c.noUpgrades();
		}
		else if (_command.equals("Main")) {
			_hasMain = true;
			_hasMap = false;
			_showMap = false;
			_highline2D.clear();
			_c = null;
			_m = null;
		}
		else if (_command.equals("Restart")) {
			_ref.restart();
			_c.unhighlight();
			_c.noUpgrades();
			_command = null;
		}
		else if (_command.equals("Pause")) {
			_screen = new Screen("Pause", _size.x, _size.y);
			_hasMap = false;
			_showMap = true;
			_hasScreen = true;
			_wasRunning = _ref.pause();
		}
		else if (_command.equals("Continue")) {
			_screen = null;
			_hasMap = true;
			_showMap = true;
			_hasScreen = false;
			if (_wasRunning) {
				_ref.unpause();
			}
		}
		else if (_command.equals("Quit")) {
			System.exit(0);
		}
	}
	
	private AbstractTower parseConsoleTowerButton(MouseEvent e) {
		if (_command.equals("Basic")) {
			return _tf.makeBasic(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref);
		}
		else if (_command.equals("Cannon")) {
			return _tf.makeCannon(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref);
		}
		else if (_command.equals("Electric")) {
			return _tf.makeElectric(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref);
		}
		else if (_command.equals("Flame")) {
			return _tf.makeFlame(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref);
		}
		else {
			System.out.println("Bad tower button command. This should never happen");
			return null;
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
				_candidate = parseConsoleTowerButton(e);
				BufferedImage sprite = _candidate.getSprite();
				int w = sprite.getWidth();
				int h = sprite.getHeight();
				Rectangle2D r = new Rectangle2D.Double(xToLon(e.getX()) - w/2, yToLat(e.getY()) - h/2, w, h);
				for (Line2D l: _highline2D) {
					if (l.intersects(r)) {
						_validPlace = false;
						break;
					}
					else {
						_validPlace = true;
					}
				}
				for (AbstractTower t: _ref.towers()) {
					if (t.intersectRect(r)) {
						_validPlace = false;
						break;
					}
				}

				

				
				if (_candidate != null) {
					if (_ref.getResources() - _candidate.getPrice() < 0) {
						_candidate = null;
					}
				}
				
				
				

			}
			else {
				_candidate = null;
			}
		}
		else if (_hasScreen) {
			_screen.contains(e.getX(), e.getY(), false);
		}

	}

	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {}

	@Override
	protected void onResize(Vec2i newSize) {
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
