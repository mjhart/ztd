package gameEngine;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import cs195n.Vec2f;


public class CannonTower extends AbstractTower {

	public CannonTower(Vec2f vec, Referee ref) {
		super(30, 30, 5000000000l, vec, ref);
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(java.awt.Color.BLACK);
		g.fill(new Rectangle2D.Float(_vec.x, _vec.y, 10, 10));
	}

	@Override
	public void drawSimple(Graphics2D g) {
		g.setColor(java.awt.Color.BLACK);
		g.fill(new Rectangle2D.Float(_vec.x, _vec.y, 10, 10));
	}

	@Override
	public void action() {
		Zombie z = _ref.getFarthest(_vec, _radius);
		if (z != null) {
			List<Zombie> splash = _ref.getZombiesInR(z.getCoords(), 10);
			for (Zombie nb: splash) {
				_ref.dealDamage(nb, _damage/2);
			}
			_ref.dealDamage(z, _damage);
		}
	}
	
}