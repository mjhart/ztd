package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import gameEngine.Referee;


public class Screen {
	
	private int _w; //Width of the whole frame
	private int _h; //Height of the whole frame
	private Graphics2D g;
	private Referee _ref;
	
	private List<HugeButton> _cbs; //A list of control buttons. Needed to check for mouse clicks
	private boolean _first;
	private String _type;
	private Text _t;
	private Color _background = Color.ORANGE;
	
	public Screen(String type, int w, int h, Referee ref) {
		_w = w;
		_h = h;
		_ref = ref;
		_cbs = new ArrayList<HugeButton>();
		this.g = null;
		_first = true;
		_type = type;
	}
	
	public void draw(Graphics2D g) {
		java.awt.Color colorholder = g.getColor();
		g.setColor(new Color(_background.getRed(), _background.getGreen(), _background.getBlue(), 150));
		g.fill(new Rectangle2D.Float(0,0,_w,_h));
		this.g = g;
		int c = 30;
		g.setColor(Color.BLACK);
		if (_type.equals("Pause")) {
			if (_first) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("Helvetica", Font.PLAIN, 15));
				_cbs.add(new HugeButton("Continue", _w, _h/2, g));
				_first = false;
			}
			g.setFont(new Font("Helvetica", Font.PLAIN, 70));
			_t = new Text("PAUSE", _w, _h/5);
		}
		else if (_type.equals("Loading")) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Helvetica", Font.PLAIN, 30));
			new Text("LOADING...", centerX("PAUSE", _w), _h/10);
		}
		else if (_type.equals("Game Over")) {
			if (_first) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("Helvetica", Font.PLAIN, 15));
				_cbs.add(new HugeButton("Restart", _w, _h/2, g));
				_cbs.add(new HugeButton("Main Menu", _w, _h/2 + 60, g));
				_first = false;
			}
			g.setFont(new Font("Helvetica", Font.PLAIN, 70));
			_t = new Text("Game Over", _w, _h/5);
		}
		else if (_type.equals("Stats")) {
			if (_first) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("Helvetica", Font.PLAIN, 15));
				_cbs.add(new HugeButton("Continue", _w, _h*2/3, g));
				_first = false;
			}
			g.setFont(new Font("Helvetica", Font.PLAIN, 70));
			_t = new Text("Stats", _w, _h/5);
			int d = 30;
			g.setFont(new Font("Helvetica", Font.PLAIN, 20));
			g.drawString("Zombies Killed: " + _ref.getZombiesKilled(), _w, _h/5 + d);
			g.drawString("Interest Earned: " + _ref.getInterestEarned(), _w, _h/5 + 2*d);
		}
		else if (_type.equals("Bad Connection")) {
			if (_first) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("Helvetica", Font.PLAIN, 15));
				_cbs.add(new HugeButton("OK", _w, _h*2/3, g));
				_first = false;
			}
			g.setFont(new Font("Helvetica", Font.PLAIN, 70));
			_t = new Text("Bad Connection", _w, _h/5);
			g.setFont(new Font("Helvetica", Font.PLAIN, 40));
			int d = 30;
			String msg1 = "There is a problem with your connection. Please fix the error";
			String msg2 = "or use one of our pre-loaded locations.";
			float x1 = centerX(msg1, _w);
			float x2 = centerX(msg2, _w);
			g.drawString(msg1, x1, _h/5 + 100 + d);
			g.drawString(msg2, x2, _h/5 + 100 + 2*d);
			

		}
		

		
		for (HugeButton cb: _cbs) {
			cb.draw(g, Color.GRAY);
		}
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Helvetica", Font.PLAIN, 70));
		_t.draw();
		
		g.setColor(colorholder);
		_first = false;
	}
	
	private float centerX(String name, float rightline) {
		FontMetrics fm = g.getFontMetrics();
		int c = fm.stringWidth(name);
		int x = (int) (.5*rightline - .5*c);
		return x;
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
	}
	
	public String contains(int x, int y, boolean click) {
		for (HugeButton cb: _cbs) {
			if (cb.getRoundRect().contains(x, y)) {
				cb.highlight();
				return cb.getName();
			}
			else {
				cb.unhighlight();
			}
		}
		return null;
	}

}