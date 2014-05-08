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
			g.setFont(new Font("Helvetica", Font.PLAIN, 70));
			_t = new Text("LOADING...", _w, _h/5);
		}
		else if (_type.equals("Game Over")) {
			if (_first) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("Helvetica", Font.PLAIN, 15));
				int d = 30;
				int f = 70;
				g.setFont(new Font("Helvetica", Font.PLAIN, 20));
				g.drawString("Zombies Killed This Round: " + _ref.getRoundZombiesKilled(), _w/3, _h/5 + f + d);
				g.drawString("Total Zombies Killed: " + _ref.getTotalZombiesKilled(), _w/3, _h/5 + f + 2*d);
				g.drawString("Interest Earned This Round: " + _ref.getRoundInterestEarned(), _w/3, _h/5 + f + 3*d);
				g.drawString("Total Interest Earned: " + _ref.getTotalInterestEarned(), _w/3, _h/5 + f + 4*d);
				g.drawString("Money Earned This Round: " + _ref.getRoundMoneyEarned(), _w/3, _h/5 + f + 5*d);
				g.drawString("Total Money Earned: " + _ref.getTotalMoneyEarned(), _w/3, _h/5 + f + 6*d);
				g.drawString("Money Spent This Round: " + _ref.getRoundMoneySpent(), _w/3, _h/5 + f + 7*d);
				g.drawString("Total Money Spent: " + _ref.getTotalMoneySpent(), _w/3, _h/5 + f + 8*d);
				_cbs.add(new HugeButton("Restart", _w, _h*2/3, g));
				_cbs.add(new HugeButton("Main Menu", _w, _h*2/3 + 60, g));
				_first = false;
			}
			g.setFont(new Font("Helvetica", Font.PLAIN, 70));
			_t = new Text("Game Over", _w, _h/5);
		}
		else if (_type.equals("Stats")) {
			g.setColor(Color.BLACK);
			if (_first) {
				g.setFont(new Font("Helvetica", Font.PLAIN, 15));
				_cbs.add(new HugeButton("Continue", _w, _h*2/3, g));
				_first = false;
			}
			g.setFont(new Font("Helvetica", Font.PLAIN, 70));
			_t = new Text("Stats", _w, _h/5);
			int d = 30;
			int f = 70;
			g.setFont(new Font("Helvetica", Font.PLAIN, 20));
			g.drawString("Zombies Killed This Round: " + _ref.getRoundZombiesKilled(), _w/3, _h/5 + f + d);
			g.drawString("Total Zombies Killed: " + _ref.getTotalZombiesKilled(), _w/3, _h/5 + f + 2*d);
			g.drawString("Interest Earned This Round: " + _ref.getRoundInterestEarned(), _w/3, _h/5 + f + 3*d);
			g.drawString("Total Interest Earned: " + _ref.getTotalInterestEarned(), _w/3, _h/5 + f + 4*d);
			g.drawString("Money Earned This Round: " + _ref.getRoundMoneyEarned(), _w/3, _h/5 + f + 5*d);
			g.drawString("Total Money Earned: " + _ref.getTotalMoneyEarned(), _w/3, _h/5 + f + 6*d);
			g.drawString("Money Spent This Round: " + _ref.getRoundMoneySpent(), _w/3, _h/5 + f + 7*d);
			g.drawString("Total Money Spent: " + _ref.getTotalMoneySpent(), _w/3, _h/5 + f + 8*d);

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
			g.setFont(new Font("Helvetica", Font.PLAIN, 30));
			int d = 30;
			String msg1 = "There is a problem with your connection. Please fix the error";
			String msg2 = "or use one of our pre-loaded locations.";
			float x1 = centerX(msg1, _w);
			float x2 = centerX(msg2, _w);
			g.drawString(msg1, x1, _h/5 + 100 + d);
			g.drawString(msg2, x2, _h/5 + 100 + 2*d);
		}
		else if (_type.equals("No Location")) {
			if (_first) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("Helvetica", Font.PLAIN, 15));
				_cbs.add(new HugeButton("OK", _w, _h*2/3, g));
				_first = false;
			}
			g.setFont(new Font("Helvetica", Font.PLAIN, 70));
			_t = new Text("Unsupported Location", _w, _h/5);
			g.setFont(new Font("Helvetica", Font.PLAIN, 30));
			int d = 30;
			String msg1 = "The selected location is unsupported.";
			String msg2 = "Please select a new location,";
			String msg3 = "or use one of our pre-loaded locations.";
			float x1 = centerX(msg1, _w);
			float x2 = centerX(msg2, _w);
			float x3 = centerX(msg3, _w);
			g.drawString(msg1, x1, _h/5 + 100 + d);
			g.drawString(msg2, x2, _h/5 + 100 + 2*d);
			g.drawString(msg3, x3, _h/5 + 100 + 3*d);
		}
		else if (_type.equals("Bad Location")) {
			if (_first) {
				g.setColor(Color.BLACK);
				g.setFont(new Font("Helvetica", Font.PLAIN, 15));
				_cbs.add(new HugeButton("OK", _w, _h*2/3, g));
				_first = false;
			}
			g.setFont(new Font("Helvetica", Font.PLAIN, 70));
			_t = new Text("Bad Location", _w, _h/5);
			g.setFont(new Font("Helvetica", Font.PLAIN, 30));
			int d = 30;
			String msg1 = "The selected location does not have";
			String msg2 = "enough information to create a map.";
			String msg3 = "Please select a new location,";
			String msg4 = "or use one of our pre-loaded locations.";
			float x1 = centerX(msg1, _w);
			float x2 = centerX(msg2, _w);
			float x3 = centerX(msg3, _w);
			float x4 = centerX(msg4, _w);
			g.drawString(msg1, x1, _h/5 + 100 + d);
			g.drawString(msg2, x2, _h/5 + 100 + 2*d);
			g.drawString(msg3, x3, _h/5 + 100 + 3*d);
			g.drawString(msg4, x4, _h/5 + 100 + 4*d);
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
			//g.drawString(name, x, y);
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
	
	public String getType() {
		return _type;
	}

}