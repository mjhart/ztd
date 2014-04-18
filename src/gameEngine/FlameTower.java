package gameEngine;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import cs195n.Vec2f;
import cs195n.Vec2i;


public class FlameTower extends AbstractTower {
	
	
	public FlameTower(Vec2f vec, Referee ref) {
		super(5, 5, 500000000, vec, ref);
	}

	@Override
	public void draw(Graphics2D g, Vec2i coords) {
		g.setColor(java.awt.Color.ORANGE);
		g.fill(new Rectangle2D.Float(coords.x, coords.y, 10, 10));
	}
	
	
	@Override
	public void drawSimple(Graphics2D g, Vec2i coords) {
		g.setColor(java.awt.Color.ORANGE);
		g.fill(new Rectangle2D.Float(coords.x, coords.y, 10, 10));
	}
	
	
	@Override
	public void action() {
		List<Zombie> zombies = _ref.getZombiesInR(_vec, _radius);
		if (!zombies.isEmpty()) {
			for (Zombie z: zombies) {
			_ref.dealDamage(z, _damage);
			}
		}
		
	}


}