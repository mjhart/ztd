package gameEngine;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import cs195n.Vec2f;
import cs195n.Vec2i;


public class BasicTower extends AbstractTower {

	public BasicTower(Vec2f vec, Referee ref) {
		super(10, 100, 1000000000, vec, ref);
		
	}
	
	@Override
	public void draw(Graphics2D g, Vec2i coords) {
		g.setColor(java.awt.Color.BLUE);
		g.fill(new Rectangle2D.Float(coords.x, coords.y, 10, 10));
	}

	@Override
	public void drawSimple(Graphics2D g, Vec2i coords) {
		g.setColor(java.awt.Color.BLUE);
		g.fill(new Rectangle2D.Float(coords.x, coords.y, 10, 10));
	}

	@Override
	public void action() {
		Zombie z = _ref.getFarthest(_vec, _radius);
		if (z != null) {
			_ref.dealDamage(z, _damage);
			System.out.println("Basic Tower Firing");
		}
	}
	
}