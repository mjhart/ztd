package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;


public class Screen {
	
	private int _w; //Width of the whole frame
	private int _h; //Height of the whole frame
	private Graphics2D g;
	
	private List<ControlButton> _cbs; //A list of control buttons. Needed to check for mouse clicks
	private ControlButton _go;
	private boolean _first;
	private String _type;
	
	public Screen(String type, int w, int h) {
		_w = w;
		_h = h;
		_cbs = new ArrayList<ControlButton>();
		this.g = null;
		_first = true;
		_type = type;
	}
	
	public void draw(Graphics2D g) {
		this.g = g;
		int c = 30;
		if (_type.equals("Pause")) {
			if (_first) {
				_cbs.add(new ControlButton("Continue", 3*_w/2, _h/5 + 2*c));
				_cbs.add(new ControlButton("Restart", 3*_w/2, _h/5 + 3*c));
				_cbs.add(new ControlButton("Main Menu", 3*_w/2, _h/5 + 4*c));
				_first = false;
			}
			g.setColor(Color.WHITE);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
			new Text("PAUSE", centerX("PAUSE", _w), _h/10);
		}
		else if (_type.equals("Loading")) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
			new Text("LOADING...", centerX("PAUSE", _w), _h/10);
		}
		else if (_type.equals("Game Over")) {
			if (_first) {
				_cbs.add(new ControlButton("Restart", 3*_w/2, _h/5 + 3*c));
				_cbs.add(new ControlButton("Main Menu", 3*_w/2, _h/5 + 4*c));
				_first = false;
			}
			g.setColor(Color.WHITE);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
			new Text("GAME OVER", centerX("PAUSE", _w), _h/10);
		}
		
		java.awt.Color colorholder = g.getColor();
		g.setColor(Color.GRAY);
		g.fill(new Rectangle2D.Float(0,0,_w,_h));
		

				
		g.setColor(Color.BLACK);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
		
		for (ControlButton cb: _cbs) {
			cb.draw();
		}
		
		g.setColor(colorholder);
	}
	
	private float centerX(String name, float rightline) {
		FontMetrics fm = g.getFontMetrics();
		int c = fm.stringWidth(name);
		int x = (int) (.5*rightline - .5*c);
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
			g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
			float x = centerX(name, rightline);
			this.x = x;
			this.y = y;
		}
		public void draw() {
			g.setColor(Color.BLACK);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 15));
			FontMetrics fm = g.getFontMetrics();
			_bb = fm.getStringBounds(_name, g);
			_r = new RoundRectangle2D.Float(x,y,(float) (_bb.getWidth()+10), (float) (_bb.getHeight()+5), 5, 5);
			g.draw(_r);
			g.drawString(_name, x+5,(int) (y+_bb.getHeight()+1));
		}
		public RoundRectangle2D getRoundRect() {
			return _r;
		}
		public String getName() {
			return _name;
		}
	}
	
	//Work in terms of vectors or x y?
	public String contains(int x, int y) {
		for (ControlButton cb: _cbs) {
			if (cb.getRoundRect().contains(x, y)) {
				return cb.getName();
			}
		}
		return null;
	}
	
	private class Text {
		public Text(String name, float x, float y) {
			g.drawString(name, x, y);
		}
	}

}