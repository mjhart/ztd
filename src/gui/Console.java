package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;

import cs195n.Vec2i;

public class Console {
	
	private float width;
	private float height;
	
	private HashMap<Rectangle2D, String> cbs; //A list of control buttons. Need to check for mouse clicks
	
	public Console(float x, float y, float w, float h, Graphics2D g) {
		width = w;
		height = h;
		cbs = new HashMap<Rectangle2D, String>();
		this.onDraw(x,y,w,h,g);
	}

	public void onDraw(float x, float y, float w, float h, Graphics2D g) {
		java.awt.Color colorholder = g.getColor();
		g.setColor(Color.RED);
		g.fill(new Rectangle2D.Float(x,y,w,h));
		g.setColor(Color.BLUE);
		FontMetrics fm = g.getFontMetrics();
		int c = fm.getHeight();
		new Text("Zombie", h/7, g);
		new Text("Tower Defense", h/7+c,g);
		new ControlButton("Main Menu", 5*h/7, g);
		new ControlButton("Restart", 5*h/7 + c + 10, g);
		new ControlButton("Quit", 5*h/7 + 2*c + 20, g);
		g.setColor(colorholder);
	}
	
	private float centerX(String name, Graphics2D g) {
		FontMetrics fm = g.getFontMetrics();
		int c = fm.stringWidth(name);
		float x = (float) (.5*width - .5*c);
		return x;
	}
	
	private class ControlButton {
		public ControlButton(String name, float y, Graphics2D g) {
			float x = centerX(name, g) - 6;
			FontMetrics fm = g.getFontMetrics();
			Rectangle2D bb = fm.getStringBounds(name, g);
			Rectangle2D r = new Rectangle2D.Float(x,y,(float) (bb.getWidth()+10), (float) (bb.getHeight()+5));
			cbs.put(r, name);
			g.draw(r);
			g.drawString(name, x+5,(int) (y+bb.getHeight()+1));
		}
	}
	
	
	
	
	
	
	
	private class Text {
		
		public Text(String name, float y, Graphics2D g) {
			float x = centerX(name, g);
			g.drawString(name, x, y);
		}
		
	}
	
	
	
	
	//Work in terms of vectors or x y?
	private void contains(int x, int y) {
		for (Rectangle2D r: cbs.keySet()) {
			if (r.contains(x, y)) {
				System.out.println(cbs.get(r) + " pressed");
				//TODO Send a message somewhere to do something
			}
		}
	}
	
	
	
	
}


//TODO Refactor to use vectors???

