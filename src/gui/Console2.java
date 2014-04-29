
package gui;

import gameEngine.Referee;
import gameEngine.towers.AbstractTower;
import gameEngine.towers.BasicTower;
import gameEngine.towers.TowerFactory;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import cs195n.Vec2f;


public class Console2 {

	
	private float _cw; //Width of console
	private float _h; //Height of the whole frame
	private float _x;
	private float _y;
	private int _textoffset;
	private int _cboffset;
	private Text _titleline1;
	private Text _titleline2;
	private Text _round;
	private Text _basehealth;
	private Text _resources;
	private TowerInfo _info;
	private Graphics2D g;
	private boolean _first;
	private TowerFactory _tf;
	private Referee _ref;
	private Color _background = Color.RED;
	private float _tiheight;
	private UpgradeInfo _ui;

	
	private List<ControlButton> _cbs; //A list of control buttons. Needed to check for mouse clicks
	private List<TowerButton> _tbs; //A list of tower buttons. Needed to check for mouse clicks

	
	public Console2(float x, float y, float w, float h, TowerFactory tf, Referee ref) {
		System.out.println("making new console");
		_cw = w;
		_h = h;
		_x = x;
		_y = y;
		_tf = tf;
		_ref = ref;
		_tiheight = 3*h/7;
		_cbs = new ArrayList<ControlButton>();
		_tbs = new ArrayList<TowerButton>();
		_first = true;
		this.g = null;
	}

	public void draw(Graphics2D g) {
		this.g = g;
		getRound();
		getResources();
		getBaseHealth();
		if (_first) {
			_textoffset = 20;
			_cboffset = 30;
			g.setColor(Color.WHITE);
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			_titleline1 = new Text("Zombie", _cw, _h/8);
			_titleline2 = new Text("Tower Defense", _cw, _h/8+_textoffset);

			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			_cbs.add(new ControlButton("Start Round!", _cw, 5*_h/7, g));
			_cbs.add(new ControlButton("Pause", _cw, 5*_h/7 + _cboffset, g));
			_cbs.add(new ControlButton("Main Menu", _cw, 5*_h/7 + 2*_cboffset, g));
			_cbs.add(new ControlButton("Restart", _cw,  5*_h/7 + 3*_cboffset, g));
			_cbs.add(new ControlButton("Quit", _cw,  5*_h/7 + 4*_cboffset, g));
			
			Vec2f zero = new Vec2f(0,0);
			_tbs.add(new TowerButton("Basic Tower", _cw/4, 2*_h/7, _tf.makeBasic(zero, _ref)));
			_tbs.add(new TowerButton("Flame Tower", _cw*3/4, 2*_h/7, _tf.makeFlame(zero, _ref)));
			_tbs.add(new TowerButton("Cannon Tower", _cw*5/4, 2*_h/7, _tf.makeCannon(zero, _ref)));
			_tbs.add(new TowerButton("Electric Tower", _cw*7/4, 2*_h/7, _tf.makeElectric(zero, _ref)));

			_first = false;
		}
		
		java.awt.Color colorholder = g.getColor();
		
		g.setColor(_background);
		g.fill(new Rectangle2D.Float(_x,_y,_cw,_h));
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Helvetica", Font.BOLD, 15));
		_titleline1.draw();
		_titleline2.draw();
		g.setColor(java.awt.Color.ORANGE);
		_round.draw();
		_basehealth.draw();
		_resources.draw();
		
		if (_info != null) {
			_info.draw();
		}
		
		if (_ui != null) {
			_ui.draw();
		}
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Helvetica", Font.BOLD, 15));
		for (ControlButton cb: _cbs) {
			cb.draw(g, _background);
		}
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Helvetica", Font.BOLD, 15));
		for (TowerButton tb: _tbs) {
			tb.draw();
		}

		g.setColor(colorholder);
	}
	
	private float centerX(String name, float rightline) {
		FontMetrics fm = g.getFontMetrics();
		int c = fm.stringWidth(name);
		float x = (float) (.5*rightline - .5*c);
		return x;
	}
	
	private float centerRect(float width, float rightline) {
		float x = (float) (.5*rightline - .5*width);
		return x;
	}
	

	
	private class TowerButton {
		private String _name;
		private RoundRectangle2D _r;
		private final float _width = _cw/5;
		private float x;
		private float y;
		private boolean _highlight;
		private BufferedImage _sprite;
		private AbstractTower _t;
		public TowerButton(String name, float rightline, float y, AbstractTower t) {
			_t = t;
			_sprite = t.getSprite();
			_name = name;
			this.x = centerRect(_width, rightline);
			this.y = y;
			_r = new RoundRectangle2D.Float(x,y,_width,_width, 5, 5);
		}
		public void draw() {
			g.setColor(Color.BLACK);
			//TODO this won't work right now
			//g.drawImage(_sprite, g.getTransform(), null);
			g.draw(_r);
			if (_highlight) {
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.MAGENTA);
				g.draw(_r);
				g.setStroke(new BasicStroke(1));
			}
		}
		public RoundRectangle2D getRect() {
			return _r;
		}
		public String getName() {
			return _name;
		}
		public void highlight() {
			_highlight = true;
			_info = new TowerInfo(_name, _t, _cw, _tiheight);
		}
		public void unhighlight() {
			_highlight = false;
			_info = null;
		}
	}
	



	private class TowerInfo {
		private String _name;
		private final float _width = _cw - 20;
		private float x;
		private float y;
		private RoundRectangle2D _back;
		private AbstractTower _t;
		public TowerInfo(String name, AbstractTower t, float rightline, float y) {
			_name = name;
			_t = t;
			this.x = centerRect(_width, rightline);
			this.y = y;
			_back = new RoundRectangle2D.Float(x,y,_width,_width + 30, 5, 5);
		}

		public void draw() {
			g.setColor(Color.ORANGE);
			g.draw(_back);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			int c = 30;
			g.drawString(_name, x, y + c);
			g.drawString("Price: " + _t.getPrice(), x, y + 2*c);
			g.drawString("Damage: " + _t.getDamage(), x, y + 3*c);
			g.drawString("Radius: " + _t.getRadius(), x, y + 4*c);
			g.drawString("Delay: " + _t.getDelay() + " secs", x, y + 5*c);
		}
	}
	
	
	private class UpgradeInfo {
		private final float _width = _cw - 20;
		private float x;
		private float y;
		private RoundRectangle2D _back;
		private ControlButton _halfdelay;
		private ControlButton _doubledamage;
		public UpgradeInfo(float rightline, float y) {
			this.x = centerRect(_width, rightline);
			this.y = y;
			_back = new RoundRectangle2D.Float(x,y,_width,_width + 30, 5, 5);
			_halfdelay = new ControlButton("Halve Delay", rightline,  y + 30, g);
			_doubledamage = new ControlButton("Double Damage", rightline,  y + 60, g);
			_cbs.add(_halfdelay);
			_cbs.add(_doubledamage);

		}

		public void draw() {
			g.setColor(Color.ORANGE);
			g.draw(_back);
			_halfdelay.draw(g, _background);
			_doubledamage.draw(g, _background);
		}
		public void removeButtons() {
			_cbs.remove(_halfdelay);
			_cbs.remove(_doubledamage);
			_halfdelay = null;
			_doubledamage = null;
		}
	}
	
	
	private class Text {
		private String _name;
		private float x;
		private float y;
		public Text(String name, float rightline, float y) {
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			this.x = centerX(name, rightline);
			this.y = y;
			_name = name;
			g.drawString(name, x, y);
		}
		public void draw() {
			g.drawString(_name, x, y);
		}
		public void setName(String name) {
			_name = name;
		}
	}
	
	

	public String contains(int x, int y) {
		for (ControlButton cb: _cbs) {
			if (cb.getRoundRect().contains(x, y)) {
				cb.highlight();
				return cb.getName();
			}
			else {
				cb.unhighlight();
			}
		}
		for (TowerButton tb: _tbs) {
			if (tb.getRect().contains(x, y)) {
				System.out.println(tb.getName());
				this.unhighlight();
				tb.highlight();
				return tb.getName();
			}
		}
		return null;
	}
	
	public void unhighlight() {
		for (TowerButton tb: _tbs) {
			tb.unhighlight();
		}
	}
	
	public void showUpgrades() {
		_ui = new UpgradeInfo(_cw, _tiheight);	}
	
	public void noUpgrades() {
		if (_ui != null) {
			_ui.removeButtons();
			_ui = null;
		}
	}
	
	private void getRound() {
		int i = _ref.getRound();
		_round = new Text("Round: " + i, _cw, _h/7 + 2*_textoffset);
	}
	
	private void getBaseHealth() {
		int i = _ref.getBaseHealth();
		_basehealth = new Text("Base Health: " + i, _cw, _h/7 + 3*_textoffset);
	}
	
	private void getResources() {
		int i = _ref.getResources();
		_resources = new Text("Resources: " + i, _cw, _h/7 + 4*_textoffset);
	}
	
	
}
