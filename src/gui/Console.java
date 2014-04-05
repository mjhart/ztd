package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import cs195n.Vec2i;

public class Console {
	
	private float width;
	private float height;
	
	public Console(float x, float y, float w, float h, Graphics2D g) {
		width = w;
		height = h;
		this.onDraw(x,y,w,h,g);
	}

	public void onDraw(float x, float y, float w, float h, Graphics2D g) {
		g.setColor(Color.RED);
		g.fill(new Rectangle2D.Float(x,y,w,h));
		java.awt.Color colorholder = g.getColor();
		new Title("ZTD", g);
		g.setColor(colorholder);
	}
	
	
//	
//	private class ControlButton {
//		
//		public ControlButton(String name, float x, float y, float w, float h, Graphics2D g) {
//			int c = fm.stringWidth(title);
//			float x = (float) (.5*width - .5*c);
//			g.drawString("ZTD", x, height/10);
//		}
//		
//	}
//	
	
	
	
	
	
	
	private class Title {
		
		public Title(String title, Graphics2D g) {
			FontMetrics fm = g.getFontMetrics();
			int c = fm.stringWidth(title);
			float x = (float) (.5*width - .5*c);
			g.drawString("ZTD", x, height/10);
		}
		
	}
	
	
	
	
	
	
	
	
	
	
}


