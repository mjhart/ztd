package gameEngine.towers;

import gameEngine.Referee;
import gameEngine.zombie.Zombie;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import cs195n.Vec2f;
import cs195n.Vec2i;


public class CannonTower extends AbstractTower {

	public CannonTower(Vec2f vec, Referee ref) {
		super(30, 100, 5000000000l, vec, ref);
		
	}
	
	@Override
	public void draw(Graphics2D g, Vec2i coords) {
		g.setColor(java.awt.Color.BLACK);
		g.fill(new Rectangle2D.Float(coords.x, coords.y, 10, 10));
	}

	@Override
	public void drawSimple(Graphics2D g, Vec2i coords) {
		super.drawSimple(g, coords, java.awt.Color.BLACK);
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
			System.out.println("Cannon Tower Firing");
		}
	}
	
}