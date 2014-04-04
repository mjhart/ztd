package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Console extends Rectangle2D.Float {
	
	public Console(float x, float y, float w, float h, Graphics2D g) {
		super(x,y,w,h);
		this.onDraw(g);
	}

	public void onDraw(Graphics2D g) {
		java.awt.Color colorholder = g.getColor();
		g.setColor(java.awt.Color.BLUE);
		g.fill(new Rectangle2D.Float(10,10,10,10));
		g.drawString("Hello world!", 20, 20);
		g.setColor(colorholder);
	}
	
	
	
	
	private class Title {
		
		public Title() {
			
		}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
}


