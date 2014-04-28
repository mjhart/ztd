package gameEngine.towers;


import gameEngine.Referee;
import gameEngine.projectile.ElectricProjectile;
import gameEngine.zombie.Zombie;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cs195n.Vec2f;
import cs195n.Vec2i;


public class ElectricTower extends AbstractTower {
	
	private BufferedImage[] _lightningSprites;
	
	public ElectricTower(Vec2f vec, Referee ref, BufferedImage sprite, BufferedImage[] lightningSprites) {
		super(30, 500000, 1000000000, 30, vec, ref, sprite);
		_lightningSprites = lightningSprites;
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
		HashSet<Zombie> alreadyhit = new HashSet<>();
		int i = 1;
		Vec2f prevCoords = _vec;
		Zombie z = _ref.getFarthest(_vec, _radius);
		if (z == null) {
			return false;
		}
		while ((z != null) && (i < 5)) {
			_ref.dealDamage(z, _damage/i);
			//TODO animation
			addProjectile(new ElectricProjectile(prevCoords, z.getCoords(), _lightningSprites));
			alreadyhit.add(z);
			List<Zombie> nbs = _ref.getZombiesInR(z.getCoords(), _radius);
			Zombie nearnb = null;
			double minsqdist = Double.POSITIVE_INFINITY;
			for (Zombie nb: nbs) {
				if ((z.getCoords().dist2(nb.getCoords()) < minsqdist) && (!alreadyhit.contains(nb))) {
					nearnb = nb;
					minsqdist = z.getCoords().dist2(nb.getCoords());
				}
			}
			prevCoords = z.getCoords();
			z = nearnb;
			i++;
		}
		return true;
	}
	
	private double sqdist(Vec2f v1, Vec2f v2) {
		float x = v1.x - v2.x;
		float y = v1.y - v2.y;
		return x*x + y*y;
	}


}