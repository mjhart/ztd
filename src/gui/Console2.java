
package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;


public class Console2 {

	
	private float _w; //Width of the whole frame
	private float _cw; //Width of console
	private float _h; //Height of the whole frame
	private float _x;
	private float _y;
	private int _textoffset;
	private Text _titleline1;
	private Text _titleline2;
	private Text _round;
	private Text _basehealth;
	private Text _resources;
	private Graphics2D g;
	private boolean _first;

	
	private List<ControlButton> _cbs; //A list of control buttons. Needed to check for mouse clicks
	private List<TowerButton> _tbs; //A list of tower buttons. Needed to check for mouse clicks

	
	public Console2(float x, float y, float w, float h) {
		System.out.println("making new console");
		_cw = w;
		_h = h;
		_x = x;
		_y = y;
		_cbs = new ArrayList<ControlButton>();
		_tbs = new ArrayList<TowerButton>();
		_first = true;
		this.g = null;
	}

	public void draw(Graphics2D g) {
		this.g = g;
		if (_first) {
			_textoffset = 30;
			g.setColor(Color.WHITE);
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			_titleline1 = new Text("Zombie", _cw, _h/7);
			_titleline2 = new Text("Tower Defense", _cw, _h/7+_textoffset);
			g.setColor(Color.ORANGE);
			_round = new Text("Round: 1", _cw, _h/7 + 2*_textoffset);
			_basehealth = new Text("Base Health: 100", _cw, _h/7 + 3*_textoffset);
			_resources = new Text("Resources: 500", _cw, _h/7 + 4*_textoffset);

			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			_cbs.add(new ControlButton("Start Round!", _cw, 5*_h/7));
			_cbs.add(new ControlButton("Main Menu", _cw, 5*_h/7 + _textoffset));
			_cbs.add(new ControlButton("Restart", _cw,  5*_h/7 + 2*_textoffset));
			_cbs.add(new ControlButton("Quit", _cw,  5*_h/7 + 3*_textoffset));
			
			_tbs.add(new TowerButton("Basic Tower", _cw,  3*_h/7 + _textoffset));
			_tbs.add(new TowerButton("Flame Tower", _cw,  3*_h/7 + 2*_textoffset));
			_tbs.add(new TowerButton("Cannon Tower", _cw,  3*_h/7 + 3*_textoffset));

			_first = false;
		}
		
		java.awt.Color colorholder = g.getColor();
		
		g.setColor(Color.RED);
		g.fill(new Rectangle2D.Float(_x,_y,_cw,_h));
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Helvetica", Font.BOLD, 15));
		_titleline1.draw();
		_titleline2.draw();
		g.setColor(java.awt.Color.ORANGE);
		_round.draw();
		_basehealth.draw();
		_resources.draw();
		

		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Helvetica", Font.BOLD, 15));
		for (ControlButton cb: _cbs) {
			cb.draw();
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
	
	private class ControlButton {
		private String _name;
		private RoundRectangle2D _r;
		private Rectangle2D _bb;
		private float x;
		private float y;
		public ControlButton(String name, float rightline, float y) {
			_name = name;
			float x = centerX(name, rightline);
			this.x = x;
			this.y = y;
			g.setColor(Color.BLACK);
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			FontMetrics fm = g.getFontMetrics();
			_bb = fm.getStringBounds(name, g);
			_r = new RoundRectangle2D.Float(x,y,(float) (_bb.getWidth()+10), (float) (_bb.getHeight()+5), 5, 5);
		}
		public void draw() {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			g.draw(_r);
			g.drawString(_name, x+5,(int) (y+_bb.getHeight()+1));
		}
		public RoundRectangle2D getRect() {
			return _r;
		}
		public String getName() {
			return _name;
		}
	}
	
	private class TowerButton {
		private String _name;
		private RoundRectangle2D _r;
		private Rectangle2D _bb;
		private float x;
		private float y;
		private boolean _highlight;
		public TowerButton(String name, float rightline, float y) {
			_name = name;
			float x = centerX(name, rightline);
			this.x = x;
			this.y = y;
			g.setColor(Color.BLACK);
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			FontMetrics fm = g.getFontMetrics();
			_bb = fm.getStringBounds(name, g);
			_r = new RoundRectangle2D.Float(x,y,(float) (_bb.getWidth()+10), (float) (_bb.getHeight()+5), 5, 5);
		}
		public void draw() {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Helvetica", Font.BOLD, 15));
			g.draw(_r);
			g.drawString(_name, x+5,(int) (y+_bb.getHeight()+1));
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
		}
		public void unhighlight() {
			_highlight = false;
		}
	}
	
	
	
	private class Text {
		private String _name;
		private float x;
		private float y;
		public Text(String name, float rightline, float y) {
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
	
	
	
	
	//Work in terms of vectors or x y?
	public String contains(int x, int y) {
		for (ControlButton cb: _cbs) {
			if (cb.getRect().contains(x, y)) {
				System.out.println(cb.getName());
				return cb.getName();
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
	
	public void setRound(int i) {
		_round = new Text("Round: " + Integer.toString(i), _cw, _h/7 + 2*_textoffset);
	}
	
	public void setBaseHealth(int i) {
		_basehealth = new Text("Base Health: " + Integer.toString(i), _cw, _h/7 + 3*_textoffset);
	}
	
	public void setResources(int i) {
		_resources = new Text("Resources: " + Integer.toString(i), _cw, _h/7 + 4*_textoffset);
	}
	
	
}
