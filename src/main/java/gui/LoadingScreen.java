package gui;

import gameEngine.Referee;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class LoadingScreen {

	private int _w; //Width of the whole frame
	private int _h; //Height of the whole frame
	private Graphics2D g;
	private Referee _ref;
	
	private boolean _first;
	private Text _t;
	private Text _m;
	private int _l;
	private Color _background = Color.ORANGE;
	private long _nanosSinceInc;
	
	public LoadingScreen(int w, int h, Referee ref) {
		_w = w;
		_h = h;
		_ref = ref;
		_l = 0;
		this.g = null;
		_first = true;
	}

	public void draw(Graphics2D g) {
		java.awt.Color colorholder = g.getColor();
		g.setColor(new Color(119, 136, 153));
		g.fill(new Rectangle2D.Float(0,0,_w,_h));
		this.g = g;
		if(_first) {
			g.setFont(new Font("Helvetica", Font.PLAIN, 70));
			_t = new Text("LOADING", _w, _h/10);
			g.setFont(new Font("Helvetica", Font.PLAIN, 20));
			_m = new Text("Looking up address", _w, _h/5);
		}

		g.setColor(Color.BLACK);
		g.setFont(new Font("Helvetica", Font.PLAIN, 70));
		_t.draw();
		g.setFont(new Font("Helvetica", Font.PLAIN, 20));
		_m.draw();
		
		for(int i=1; i <= _l; i ++) {
			g.fillOval(_w / 2 + (i - 3) * 50, _h / 5 * 4, 10, 10);
		}

		g.setColor(colorholder);
		_first = false;
	}
	
	public void tick(long nanosSinceTick) {
		_nanosSinceInc+=nanosSinceTick;
		if(_nanosSinceInc > 500000000) {
			_nanosSinceInc = 0;
			_l = (_l + 1) % 6;
		}
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
}
