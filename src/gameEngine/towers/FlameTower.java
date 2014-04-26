package gameEngine.towers;

import gameEngine.Referee;
import gameEngine.zombie.Zombie;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import cs195n.Vec2f;
import cs195n.Vec2i;


public class FlameTower extends AbstractTower {
	
	private boolean _animate = false;
	
	public FlameTower(Vec2f vec, Referee ref, BufferedImage sprite) {
		super(5, 50, 500000000, vec, ref, sprite);
	}

	@Override
	public void draw(Graphics2D g, Vec2i coords) {
		g.setColor(java.awt.Color.ORANGE);
		g.fill(new Rectangle2D.Float(coords.x, coords.y, 10, 10));
	}
	
	@Override
	public void drawSimple(Graphics2D g, Vec2i coords) {
		super.drawSimple(g, coords, java.awt.Color.ORANGE);
		
		//This is just for fun, the drawn oval is not accurate at allgh
		if (_animate) {
			g.setColor(java.awt.Color.ORANGE);
			g.fillOval((int) coords.x - 50, (int) coords.y - 50, 100, 100);
			_animate = false;
		}
	}
	
	
	
	@Override
	public boolean action() {
		List<Zombie> zombies = _ref.getZombiesInR(_vec, _radius);
		if (!zombies.isEmpty()) {
			for (Zombie z: zombies) {
				_ref.dealDamage(z, _damage);
				_animate = true;
				System.out.println("Flame Tower Firing");
			}
			return true;
		}
		return false;

	}


}