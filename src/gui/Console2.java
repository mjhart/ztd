
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
import java.awt.geom.AffineTransform;
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
	private Color _background = Color.GRAY;
	private float _tiheight;
	private UpgradeInfo _ui;
	private final float _tbwidth;

	
	private List<ControlButton> _cbs; //A list of control buttons. Needed to check for mouse clicks
	private List<TowerButton> _tbs; //A list of tower buttons. Needed to check for mouse clicks

	
	public Console2(float x, float y, float w, float h, TowerFactory tf, Referee ref) {
		_cw = w;
		_tbwidth = 36;
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
			int towerspace = 5;
			float leftstart = centerRect(_tbwidth*4 + towerspace*3, _cw);
			_tbs.add(new TowerButton("Basic Tower", leftstart, 2*_h/7, _tf.makeBasic(zero, _ref)));
			_tbs.add(new TowerButton("Flame Tower", leftstart+_tbwidth+towerspace, 2*_h/7, _tf.makeFlame(zero, _ref)));
			_tbs.add(new TowerButton("Cannon Tower", leftstart+2*_tbwidth+2*towerspace, 2*_h/7, _tf.makeCannon(zero, _ref)));
			_tbs.add(new TowerButton("Goo Tower", leftstart+3*_tbwidth+3*towerspace, 2*_h/7, _tf.makeGoo(zero, _ref)));

			
			_tbs.add(new TowerButton("Poison Tower", leftstart, 2*_h/7 + _tbwidth + 5, _tf.makePoison(zero, _ref)));
			_tbs.add(new TowerButton("Electric Tower", leftstart+_tbwidth+towerspace, 2*_h/7 + _tbwidth + 5, _tf.makeElectric(zero, _ref)));
			_tbs.add(new TowerButton("Laser Tower", leftstart+2*_tbwidth+2*towerspace, 2*_h/7 + _tbwidth + 5, _tf.makeLaser(zero, _ref)));
			//_tbs.add(new TowerButton("Stun Tower", _cw*7/4, 2*_h/7 + _tbwidth + 5, _tf.makeStun(zero, _ref)));
			_first = false;
		}
		
		java.awt.Color colorholder = g.getColor();
		
		//g.setColor(_background);
		g.setColor(Color.WHITE);
		g.fill(new Rectangle2D.Float(_x,_y,_cw,_h));
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Helvetica", Font.BOLD, 15));
		_titleline1.draw();
		_titleline2.draw();
		g.setColor(java.awt.Color.BLACK);
		_round.draw();
		_basehealth.draw();
		_resources.draw();
		
		if (_info != null) {
			_info.draw();
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
		
		if (_ui != null) {
			_ui.draw();
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
		private float x;
		private float y;
		private boolean _highlight;
		private BufferedImage _sprite;
		private AbstractTower _t;
		public TowerButton(String name, float x, float y, AbstractTower t) {
			_t = t;
			_sprite = t.getSprite();
			_name = name;
			//this.x = centerRect(_tbwidth, rightline);
			this.x = x;
			this.y = y;
			_r = new RoundRectangle2D.Float(x,y,_tbwidth,_tbwidth, 5, 5);
		}
		public void draw() {
			g.setColor(Color.BLACK);
			//TODO this won't work right now
			//g.drawImage(_sprite, new AffineTransform(), null);
			g.draw(_r);
			String first = _name.substring(0, 1);
			first.toUpperCase();
			g.setFont(new Font("Helvetica", Font.BOLD, 20));
			g.drawString(first, x+_tbwidth/4 +2, y + 25);
			g.setColor(Color.RED);
			g.setStroke(new BasicStroke(3));
			if (exOutTower(_t)) {
				g.drawLine((int) x + 7, (int) y + 7, (int) (x + _tbwidth - 5), (int) (y + _tbwidth - 5));
				g.drawLine((int) x + 7, (int) (y + _tbwidth - 5), (int) (x + _tbwidth - 5), (int) y + 7);
			}
			g.setStroke(new BasicStroke(1));
			if (_highlight) {
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.MAGENTA);
				g.draw(_r);
				g.setStroke(new BasicStroke(1));
			}
			g.setColor(Color.BLACK);
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
		private final float _width = 160;
		private float x;
		private float y;
		private RoundRectangle2D _back;
		private AbstractTower _t;
		public float _rightline;
		public TowerInfo(String name, AbstractTower t, float rightline, float y) {
			_name = name;
			_t = t;
			this.x = centerRect(_width, rightline);
			this.y = y;
			_back = new RoundRectangle2D.Float(x,y,_width,_width + 35, 5, 5);
			_rightline = rightline;
		}

		public void draw() {
			g.setColor(Color.ORANGE);
			g.draw(_back);
			g.setColor(Color.BLACK);
			int c = 20;
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			g.drawString(_name, centerX(_name, _rightline), y + c);
			g.drawString("Price: " + _t.getPrice(), x + 2, y + 2*c);
			g.drawString("Damage: " + _t.getDamage(), x + 2, y + 3*c);
			g.drawString("Radius: " + (int) Math.sqrt(_t.getRadius()/10000), x + 2, y + 4*c);
			g.drawString("Delay: " + _t.getDelay() + " secs", x + 2, y + 5*c);
			String blurb = _t.getBlurb();
			FontMetrics fm = g.getFontMetrics();
			int d = fm.stringWidth(blurb);
			if (d < _width) {
				g.drawString(blurb, x + 2, y + 5*c);
			}
			else {
				String[] strarr = blurb.split("\\s+");
				String line1 = strarr[0];
				int i = 1;
				while (fm.stringWidth(line1) < _width) {
					if (fm.stringWidth(line1 + " " + strarr[i]) < _width) {
						line1 = line1 + " " + strarr[i];
						i++;
					}
					else {
						break;
					}
				}
				String line2 = strarr[i];
				i++;
				while ((fm.stringWidth(line2) < _width) && (i < strarr.length)) {
					if (fm.stringWidth(line2 + " " + strarr[i]) < _width) {
						line2 = line2 + " " + strarr[i];
						i++;
					}
					else {
						break;
					}
				}
				String line3 = "";
				if (i < strarr.length) {
					line3 = strarr[i];
					for (int j = i+1; j < strarr.length; j++) {
						line3 = line3 + " " + strarr[j];
					}
				}
				g.drawString(line1,  x + 2, y + 7*c);
				g.drawString(line2,  x + 2, y + 8*c);
				g.drawString(line3,  x + 2, y + 9*c);

			}

		}
	}
	
	
	private class UpgradeInfo {
		private final float _width = 160;
		private float x;
		private float y;
		private RoundRectangle2D _back;
		private ControlButton _halfdelay;
		private ControlButton _doubledamage;
		private Text _text1;
		private Text _text2;
		private AbstractTower _t;
		public UpgradeInfo(float rightline, float y, AbstractTower t) {
			_t = t;
			this.x = centerRect(_width, rightline);
			this.y = y;
			_back = new RoundRectangle2D.Float(x,y,_width,_width + 35, 5, 5);
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			_text1 = new Text("Upgrades cost 200", rightline, y + 50);
			_text2 = new Text("resources each", rightline, y + 70);
			_halfdelay = new ControlButton("Halve Delay", rightline,  y + 100, g);
			_doubledamage = new ControlButton("Double Damage", rightline,  y + 130, g);
			_cbs.add(_halfdelay);
			_cbs.add(_doubledamage);

		}
		public void draw() {
			g.setColor(Color.ORANGE);
			g.draw(_back);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			_text1.draw();
			_text2.draw();
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			_halfdelay.draw(g, _background);
			_doubledamage.draw(g, _background);
			g.setColor(new Color(1f, 0f, 0f, .5f));
			if (colorOutUpgrade(1,_t)) {
				RoundRectangle2D r = _halfdelay.getRoundRect();
				g.fill(r);
			}
			if (colorOutUpgrade(2,_t)) {
				RoundRectangle2D r = _doubledamage.getRoundRect();
				g.fill(r);
			}
			g.setColor(Color.RED);
			g.setStroke(new BasicStroke(3));
			if (exOutUpgrade(1,_t)) {
				RoundRectangle2D r = _halfdelay.getRoundRect();
				g.drawLine((int) r.getX() + 5, (int) r.getY() + 5, (int) (r.getX() + r.getWidth() - 5), (int) (r.getY() + r.getHeight() - 5));
				g.drawLine((int) r.getX() + 5, (int) (r.getY() + r.getHeight() - 5), (int) (r.getX() + r.getWidth() - 5),(int) r.getY() + 5);
			}
			if (exOutUpgrade(2,_t)) {
				RoundRectangle2D r = _doubledamage.getRoundRect();
				g.drawLine((int) r.getX() + 5, (int) r.getY() + 5, (int) (r.getX() + r.getWidth() - 5), (int) (r.getY() + r.getHeight() - 5));
				g.drawLine((int) r.getX() + 5, (int) (r.getY() + r.getHeight() - 5), (int) (r.getX() + r.getWidth() - 5),(int) r.getY() + 5);
			}

			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(1));
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
	
	

	public String contains(int x, int y, boolean click) {
		for (ControlButton cb: _cbs) {
			if (cb.getRoundRect().contains(x, y)) {
				if (!click) {
					cb.highlight();
				}
				return cb.getName();
			}
			else {
				cb.unhighlight();
			}
		}
		if (click) {
			for (TowerButton tb: _tbs) {
				if (tb.getRect().contains(x, y)) {
					this.unhighlightTb();
					tb.highlight();
					return tb.getName();
				}
			}
		}
		return null;
	}
	
	public void unhighlightTb() {
		for (TowerButton tb: _tbs) {
			tb.unhighlight();
		}
	}
	
	public void showUpgrades(AbstractTower t) {
		_ui = new UpgradeInfo(_cw, _tiheight, t);	}
	
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
	
	private boolean exOutTower(AbstractTower t) {
		return (_ref.getResources() - t.getPrice() < 0);
	}
	
	private boolean exOutUpgrade(int i, AbstractTower t) {
		int z = t.getUpgradeCost(i); //1 indicates HD, 2 is DD
		return _ref.getResources() - z < 0;
	}
	
	private boolean colorOutUpgrade(int i, AbstractTower t) {
		return t.isUpgraded(i);
	}
	
	
}
