package gameEngine.towers;

import gameEngine.Referee;
import gameEngine.zombie.Zombie;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import cs195n.Vec2f;
import cs195n.Vec2i;


public class LaserTower extends AbstractTower {
	
	private boolean _animate = false;
	
	public LaserTower(Vec2f vec, Referee ref, BufferedImage sprite) {
		super(30, 100, 2000000000, vec, ref, sprite);
	}

	@Override
	public void draw(Graphics2D g, Vec2i coords) {
		g.setColor(java.awt.Color.ORANGE);
		g.fill(new Rectangle2D.Float(coords.x, coords.y, 10, 10));
	}
	
	@Override
	public void drawSimple(Graphics2D g) {
		super.drawSimple(g, java.awt.Color.ORANGE);
		
//		//This is just for fun, the drawn oval is not accurate at allgh
//		if (_animate) {
//			g.setColor(java.awt.Color.ORANGE);
//			g.fillOval((int) coords.x - 50, (int) coords.y - 50, 100, 100);
//			_animate = false;
//		}
	}
	
	
	
	@Override
	public boolean action() {
		Zombie z = _ref.getFarthest(_vec, _radius);
		if (z != null) {
			Vec2f t = z.getCoords();
			float run = t.x - super._vec.x;
			float rise = t.y - super._vec.y;
			float slope = rise/run;
			float yfar = slope*(10000 - t.x) + t.y;
			Line2D laser = new Line2D.Float(super._vec.x, super._vec.y, 10000, yfar);
			//TODO make into a projectile or something?
			
			
			
			
			return true;
		}

		return false;
	}
	
	private double sqdist(Vec2f v1, Vec2f v2) {
		float x = v1.x - v2.x;
		float y = v1.y - v2.y;
		return x*x + y*y;
	}


}