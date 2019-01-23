package gameEngine.towers;


import gameEngine.Constants;
import gameEngine.Referee;
import gameEngine.projectile.ElectricProjectile;
import gameEngine.zombie.Zombie;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.List;

import cs195n.Vec2f;


public class ElectricTower extends AbstractTower {
	
	private BufferedImage[] _lightningSprites;
	
	public ElectricTower(Vec2f vec, Referee ref, BufferedImage sprite, BufferedImage[] lightningSprites) {
		super(30, 500000, 1000000000, 300, Constants.ELECTRIC_BLURB, vec, ref, sprite);
		_lightningSprites = lightningSprites;
	}
	
	@Override
	public void drawSimple(Graphics2D g) {
		super.drawSimple(g, java.awt.Color.ORANGE);
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


}