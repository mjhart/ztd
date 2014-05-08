package gui;

import gameEngine.Constants;
import gameEngine.Referee;
import gameEngine.towers.AbstractTower;
import gameEngine.towers.TowerFactory;
import gameEngine.zombie.Zombie;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import mapbuilder.Building;
import mapbuilder.Relation;
import mapbuilder.Map;
import mapbuilder.MapNode;
import mapbuilder.MapWay;
import cs195n.SwingFrontEnd;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class TestFrontEnd extends SwingFrontEnd {
	
	static final Vec2i DEFAULT_WINDOW_SIZE = new Vec2i(980, 800);
	
	private List<Line2D> _highline2D;
	private List<Line2D> _zombieline2D;
	private MainMenu _mm;
	private Console2 _c;
	private boolean _hasMain;
	private boolean _showMain;
	private boolean _hasMap;
	private boolean _showMap;
	private AbstractTower _candidate;
	private boolean _validPlace;
	private AbstractTower _placedTower;
	private Vec2i _size;
	private Map _m;
	private Referee _ref;
	private String _command;
	private TowerFactory _tf;
	private Screen _screen;
	private boolean _hasScreen;
	private boolean _wasRunning;
	private float _mapSize;
	private float _consoleWidth;
	private Rectangle _border;
	private boolean _hasDataError = false;
	private AtomicBoolean _loading;
	private LoadingScreen _lScreen;
	private boolean _doreset = false;
	
	public TestFrontEnd(String title, boolean fullscreen) {
		super(title, fullscreen);
		super.startup();
	}
	
	public TestFrontEnd(String title, boolean fullscreen, Vec2i size) {
		super(title, fullscreen, size);
		super.setDebugMode(false);
		
		_tf = new TowerFactory();

		_mm = new MainMenu(size.x, size.y, "", "", 0);
		_hasMain = true;
		_showMain = true;
		_hasMap = false;
		_showMap = false;
		_hasScreen = false;
		_wasRunning = false;
		
		_highline2D = new ArrayList<>();
		_zombieline2D = new ArrayList<>();

		_validPlace = false;
		_candidate = null;
		_loading = new AtomicBoolean(false);
		
		super.startup();
	}

	@Override
	protected void onTick(long nanosSincePreviousTick) {
		if(!_loading.get()) {
			if(_ref != null) {
				_ref.tick(nanosSincePreviousTick);
			}
		}
		else {
			_lScreen.tick(nanosSincePreviousTick);
		}
	}

	@Override
	protected void onDraw(Graphics2D g) {
		if(_loading.get()) {
			_lScreen.draw(g);
		}
		else {
			if (_showMain) {
				_mm.draw(g);
			}
			else if (_showMap) {
				g.translate(_consoleWidth, 0);
				g.scale((float) _mapSize / 10000, (float) _mapSize / 10000);
				float defaultstroke = 10000 / DEFAULT_WINDOW_SIZE.x;
				g.setStroke(new BasicStroke(defaultstroke));

				this.drawMap(g, defaultstroke);

				for(AbstractTower t : _ref.towers()) {
					t.draw2(g);
				}

				g.setColor(java.awt.Color.RED);
				for(Zombie z : _ref.getZombies()) {
					//g.drawOval(lonToX(z.getCoords().x), latToY(z.getCoords().y), 3, 3);
					//z.draw(g, new Vec2i(lonToX(z.getCoords().x), latToY(z.getCoords().y)));
					z.draw(g);
				}

				this.drawCandidate(g);

				g.setStroke(new BasicStroke());
				g.setTransform(new AffineTransform());
				_c.draw(g);

				if(_border != null) {
					g.setColor(java.awt.Color.WHITE);
					g.fill(_border);
				}

				this.checkGameOver();
			}
			if (_hasScreen) {
				_screen.draw(g);
			}
		}

	}
	
	public void drawMap(Graphics2D g, float defaultstroke) {
		//Landuse
		g.setColor(new Color(144,238,144));
		if (_m.getLanduse() != null) {
			for (Building b: _m.getLanduse()) {
				g.fill(b.getPolygon());
			}
		}
		
		//Waterways ways
		if (_m.getWaterways() != null) {
			g.setColor(new Color(173,216,230));
			for (Building b: _m.getWaterways()) {
				g.fill(b.getPolygon());
			}
		}
		
		//Streams
		g.setStroke(new BasicStroke(8*defaultstroke));
		if (_m.getStreams() != null) {
			for(MapWay h : _m.getStreams()) {
				List<MapNode> nList = h.getNodes();
				for(int i=1; i<nList.size(); i++) {
					g.draw(new Line2D.Float(nList.get(i-1).getX(), nList.get(i-1).getY(), nList.get(i).getX(), nList.get(i).getY()));
				}
			}
		}
		
//		//Waterways relations
//		if (_m.getWaterrels() != null) {
//			g.setColor(new Color(173,216,230));
//			for (Relation r: _m.getWaterrels()) {
//				g.fill(r.getPolygon());
//			}
//		}
		
		//All highways, draw thin
		g.setColor(new Color(255,222,173).darker());
		g.setStroke(new BasicStroke(defaultstroke));
		for (Line2D l: _highline2D) {
			g.draw(l);
		}
		

		
		//Footways
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
		
		//Residential
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
		
		//Secondary
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
		
		//Tertiary
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
		
		//Buildings
		g.setColor(Color.GRAY.brighter());
		for (Building b: _m.getBuildings()) {
			g.fill(b.getPolygon());
		}

		//Zombie highways
		g.setColor(java.awt.Color.BLUE);
		g.setStroke(new BasicStroke(6*defaultstroke));
		for (Line2D l: _zombieline2D) {
			g.draw(l);
		}
		
		//Building names
		g.setColor(Color.BLACK);
		g.setFont(new Font("Helvetica", Font.BOLD, 110));
		for (Building b: _m.getBuildings()) {
			Rectangle2D r = b.getPolygon().getBounds();
			if (b.getName() != null) {
				String[] namearr = b.getName().split("\\s+");
				FontMetrics fm = g.getFontMetrics();
				boolean draw = true;
				for (int i = 0; i < namearr.length; i++) {
					if (fm.stringWidth(namearr[i]) > r.getWidth()) {
						draw = false;
						break;
					}
				}
				if ((namearr.length * fm.getHeight() < r.getHeight()) && (draw)) {
					for (int i = 0; i < namearr.length; i++) {
						g.drawString(namearr[i], (int) r.getX() + 100, (int) r.getCenterY() - 50 + 110*i);
					}
				}
			}
		}
		
		/*
		//Source nodes
		g.setColor(java.awt.Color.ORANGE);
		for(MapNode n : _m.getSourceList()) {
			g.fillOval((int) (n.getX()-35), (int) (n.getY()-35), 70, 70);
		}
		*/
	}
	
	public void drawCandidate(Graphics2D g) {
		if (_candidate != null) {
			_candidate.draw2(g);
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
		
		if (_placedTower != null) {
			Color holder = g.getColor();
			g.setColor(new Color(0f, 1f, 0f, .5f));
			Ellipse2D e = new Ellipse2D.Float(
					_placedTower.getCoords().x - (float) Math.sqrt(_placedTower.getRadius()),
					_placedTower.getCoords().y - (float) Math.sqrt(_placedTower.getRadius()),
					(float) Math.sqrt(_placedTower.getRadius()) * 2,
					(float) Math.sqrt(_placedTower.getRadius()) * 2);
			g.fill(e);
			g.setColor(holder);
		}
		
	}
	
	public void checkGameOver() {
		if (!_hasScreen) {
			if (_ref.getGameOver()) {
				_screen = new Screen("Game Over", _size.x, _size.y, _ref);
				_hasMap = false;
				_showMap = true;
				_hasScreen = true;
			}
		}
	}
	
	public void dataError(int i) {
		if (i == 1) {
			_screen = new Screen("Bad Connection", _size.x, _size.y, _ref);
		}
		else if (i == 2) {
			_screen = new Screen("No Location", _size.x, _size.y, _ref);
		}
		else if (i == 3) {
			_screen = new Screen("Bad Location", _size.x, _size.y, _ref);
		}
		_hasScreen = true;
		_hasDataError = true;
		_hasMain = false;
	}
	
	public void makeMap(String add) {
		_ref = new Referee(_m, this);
		_m = new Map(add, _ref, this);
		if (!_hasDataError) {
			if (_m.getBaseNode() == null) {
				dataError(2);
			}
			else {
				_ref.setMap(_m);
				if (_m.calculatePath().isEmpty()) {
					dataError(3);
				}
				else {
					_c = new Console2(0,0,_consoleWidth,_size.y, _tf, _ref);
					_hasMap = true;
					_showMap = true;
					_hasMain = false;
					_showMain = false;
					_hasScreen = false;


					for(MapWay h : _m.getHighways()) {
						List<MapNode> nList = h.getNodes();
						for(int i=1; i<nList.size(); i++) {
							_highline2D.add(new Line2D.Float(nList.get(i-1).getX(), nList.get(i-1).getY(), nList.get(i).getX(), nList.get(i).getY()));
						}
					}

					for(MapNode n : _m.getSourceList()) {
						MapNode cur = n;
						MapNode next = cur.getNext();
						while(next!=null) {
							Line2D l = new Line2D.Float(cur.getX(), cur.getY(), next.getX(), next.getY());
							_zombieline2D.add(l);
							cur = next;
							next = next.getNext();
						}
					}
				}
			}
		}
		
	}


	@Override
	protected void onKeyTyped(KeyEvent e) {}

	@Override
	protected void onKeyPressed(KeyEvent e) {
		if(!_loading.get()) {
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
		}
	}

	@Override
	protected void onKeyReleased(KeyEvent e) {}

	@Override
	protected void onMouseClicked(MouseEvent e) {
		if(!_loading.get()) {
			if (_hasMain) {
				String add = _mm.contains(e.getX(), e.getY(), true);
				if (add != null) {
					_loading.set(true);
					_lScreen = new LoadingScreen(_size.x, _size.y, _ref);
					Thread t = new MapImportThread(add, this);
					t.start();
				}
			}
			else if (_hasMap) {
				String command = _c.contains(e.getX(), e.getY(), true);
				if (command != null) {
					_c.noUpgrades();
					String[] fw = command.split("\\s+");
					_command = fw[0];

				}



				if (command != null) {
					if (parseConsoleControlButton()) {
						if ((!_command.equals("Halve")) && (!_command.equals("Double"))) {
							_placedTower = null;
							if (_c != null) {
								_c.noUpgrades();
							}
						}
						else {
							if (_c != null) {
								_c.showUpgrades(_placedTower);
							}
						}
						_command = null;
					}
					else {
						_placedTower = null;
						_c.noUpgrades();
					}
				}
				else {
					if ((e.getX() > _consoleWidth) && (_command != null)) {
						if (_validPlace) {
							if (_candidate != null) {
								_ref.addTower(_candidate);
								_c.unhighlightTb();
								_command = null;
								_candidate = null;
								_c.noUpgrades();
							}
						}
					}
					else if ((e.getX() > _consoleWidth) && (_command == null)) {
						_c.noUpgrades();
						_placedTower = null;
						for (AbstractTower t: _ref.towers()) {
							if (t.contains(xToLon(e.getX()), yToLat(e.getY()))) {
								_c.unhighlightTb();
								_placedTower = t;
								_c.showUpgrades(t);
								break;
							}
						}
					}
					else {
						_c.unhighlightTb();
						_c.noUpgrades();
						_command = null;
						_candidate = null;
						_placedTower = null;
					}
				}
			}
			else if(_hasScreen) {

				String command = _screen.contains(e.getX(), e.getY(), true);
				if (command != null) {
					String[] fw = command.split("\\s+");
					_command = fw[0];
					parseConsoleControlButton();
				}
				_command = null;
			}
		}

	}
	
	private boolean parseConsoleControlButton() {
		if (_command.equals("Halve")) {
			if ((_ref.getResources() - _placedTower.getUpgradeCost(1) >= 0)
					&& (!_placedTower.isUpgraded(1))) {
				_ref.upgradeTower(_placedTower, 1);
			}
			return true;
		}
		else if (_command.equals("Double")) {
			if ((_ref.getResources() - _placedTower.getUpgradeCost(2) >= 0)
					&& (!_placedTower.isUpgraded(2))) {
				_ref.upgradeTower(_placedTower, 2);
			}
			return true;
		}
		else if (_command.equals("Start")) {
			_ref.startRound();
			_c.unhighlightTb();
			_c.noUpgrades();
			return true;
		}
		else if (_command.equals("Main")) {
			_mm.clear();
			_hasMain = true;
			_showMain = true;
			_hasMap = false;
			_showMap = false;
			_hasScreen = false;
			_screen = null;
			_highline2D.clear();
			_zombieline2D.clear();
			_c = null;
			_m = null;
			return true;
		}
		else if (_command.equals("Restart")) {
			_hasScreen = false;
			_hasMap = true;
			_screen = null;
			_ref.restart();
			_c.unhighlightTb();
			_c.noUpgrades();
			_placedTower = null;
			_candidate = null;
			return true;
		}
		else if (_command.equals("Pause")) {
			_screen = new Screen("Pause", _size.x, _size.y, _ref);
			_hasMap = false;
			_showMap = true;
			_hasScreen = true;
			_wasRunning = _ref.pause();
			return true;
		}
		else if (_command.equals("Continue")) {
			_screen = null;
			_hasMap = true;
			_showMap = true;
			_hasScreen = false;
			_c.unhighlightTb();
			if (_wasRunning) {
				_ref.unpause();
			}
			if (_doreset) {
				_ref.resetRoundMoneySpent();
				_doreset = false;
			}
			return true;
		}
		else if (_command.equals("OK")) {
			_screen = null;
			_hasScreen = false;
			_hasMain = true;
			_showMain = true;
			_hasDataError = false;
		}
		else if (_command.equals("Quit")) {
			System.exit(0);
		}
		return false;
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
		else if (_command.equals("Goo")) {
			return _tf.makeGoo(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref);
		}
		else if (_command.equals("Laser")) {
			return _tf.makeLaser(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref);
		}
		else if (_command.equals("Poison")) {
			return _tf.makePoison(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref);
		}
		else if (_command.equals("Stun")) {
			return _tf.makeStun(new Vec2f(xToLon(e.getX()), yToLat(e.getY())), _ref);
		}
		else {
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
		if(!_loading.get()) {
			if (_hasMain) {
				_mm.contains(e.getX(), e.getY(), false);
			}

			else if (_hasMap) {
				_c.contains(e.getX(), e.getY(), false);
				if ((e.getX() > _consoleWidth) && (_command != null)) {
					_candidate = parseConsoleTowerButton(e);
					//if (_candidate != null) {
					BufferedImage sprite = _candidate.getSprite();
					int w = sprite.getWidth();
					int h = sprite.getHeight();
					Rectangle2D r = new Rectangle2D.Double(xToLon(e.getX() + 3) - w/2, yToLat(e.getY() + 2) - h/2, w - 8, h - 10);
					for (Line2D l: _zombieline2D) {
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
					//}
				}
				else {
					_candidate = null;
				}
			}
			else if (_hasScreen) {
				_screen.contains(e.getX(), e.getY(), false);
			}
		}
	}

	@Override
	protected void onMouseWheelMoved(MouseWheelEvent e) {}

	@Override
	protected void onResize(Vec2i newSize) {
		_size = newSize;
		_candidate = null;
		_command = null;
		_placedTower = null;
		if(newSize.x - Constants.MIN_CONSOLE_WIDTH > newSize.y) {
			_mapSize = newSize.y;
			_consoleWidth = newSize.x - newSize.y;
			_c = new Console2(0,0,_consoleWidth,_size.y, _tf, _ref);
			_border = null;
		}
		else {
			_mapSize = newSize.x - Constants.MIN_CONSOLE_WIDTH;
			_consoleWidth = Constants.MIN_CONSOLE_WIDTH;
			_c = new Console2(0,0,_consoleWidth,_size.y, _tf, _ref);
			_border = new Rectangle((int) _consoleWidth, (int) _mapSize, (int) _mapSize, (int) (newSize.x - _consoleWidth));
		}
		if(_loading.get()) {
			_lScreen = new LoadingScreen(_size.x, _size.y, _ref);
		}
		if(_hasScreen) {
			_screen = new Screen(_screen.getType(), _size.x, _size.y, _ref);
		}
		
		String line1 = _mm.getLine1();
		String line2 = _mm.getLine2();
		int toggle = _mm.getToggle();
		_mm = new MainMenu(_size.x, _size.y, line1, line2, toggle);
	}
	
	public static void main(String[] args) {
		new TestFrontEnd("ZTD", false, new Vec2i(DEFAULT_WINDOW_SIZE.x, DEFAULT_WINDOW_SIZE.y));
	}
	
	
	public int latToY(double lat) {
		return (int) (lat / 10000 * _size.y);
	}
	
	public int lonToX(double lon) {
		return (int) (lon / 10000 * (float) _size.x + Constants.MIN_CONSOLE_WIDTH);
	}
	
	private float yToLat(double y) {
		return (float) (y / _size.y * 10000);
	}
	private float xToLon(double x) {
		return (float) ((x - _consoleWidth) / (float) (_size.x - _consoleWidth) * 10000f);
	}
	
	public void roundEnded() {
		_hasScreen = true;
		_hasMap = false;
		_wasRunning = false;
		_screen = new Screen("Stats", _size.x, _size.y, _ref);
		_doreset = true;
	}
	

	private class MapImportThread extends Thread {
		
		private TestFrontEnd _fe;
		private String _add;
		public MapImportThread(String add, TestFrontEnd fe) {
			_fe = fe;
			_add = add;
		}
		
		@Override
		public void run() {
			_fe.makeMap(_add);
			_loading.set(false);
		}
	}
	
	
}
