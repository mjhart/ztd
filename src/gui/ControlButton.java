package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;


public class ControlButton {
	private String _name;
	private RoundRectangle2D _r;
	private Rectangle2D _bb;
	private float x;
	private float y;
	private boolean _highlight;
	public ControlButton(String name, float rightline, float y, Graphics2D g) {
		_name = name;
		g.setFont(new Font("Helvetica", Font.BOLD, 15));
		float x = centerX(name, rightline, g);
		this.x = x;
		this.y = y;
		_highlight = false;
	}
	public void draw(Graphics2D g, Color background) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Helvetica", Font.BOLD, 15));
		FontMetrics fm = g.getFontMetrics();
		_bb = fm.getStringBounds(_name, g);
		_r = new RoundRectangle2D.Float(x,y,(float) (_bb.getWidth()+10), (float) (_bb.getHeight()+5), 10, 10);
		if (_highlight) {
			g.setColor(background.brighter());
			//g.setColor(Color.BLACK);
			g.fill(_r);
		}
		g.setColor(Color.BLACK);
		g.draw(_r);
		g.drawString(_name, x+5,(int) (y+_bb.getHeight()+1));
	}
	public RoundRectangle2D getRoundRect() {
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
	
	private float centerX(String name, float rightline, Graphics2D g) {
		FontMetrics fm = g.getFontMetrics();
		int c = fm.stringWidth(name);
		int x = (int) (.5*rightline - .5*c);
		return x;
	}

}