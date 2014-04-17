package ztdpac;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import cs195n.Vec2f;


public class BasicTower extends AbstractTower {

	public BasicTower(Vec2f vec, Referee ref) {
		super(10, 10, 1, vec, ref);
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(java.awt.Color.BLUE);
		g.fill(new Rectangle2D.Float(_vec.x, _vec.y, 10, 10));
	}

	@Override
	public void drawSimple(Graphics2D g) {
		g.setColor(java.awt.Color.BLUE);
		g.fill(new Rectangle2D.Float(_vec.x, _vec.y, 10, 10));
	}

	@Override
	public void action() {
		Zombie z = _ref.getFarthest(_vec, _radius);
		if (z != null) {
			z.takeDamage(_damage);
		}
	}
	
}