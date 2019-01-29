package gameEngine.towers;

import gameEngine.Constants;
import gameEngine.Referee;
import gameEngine.projectile.FireProjectile;
import gameEngine.zombie.Zombie;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import cs195n.Vec2f;


public class FlameTower extends AbstractTower {
	
	private final BufferedImage _flamecircle;
	
	
	FlameTower(Vec2f vec, Referee ref, BufferedImage sprite, BufferedImage flamecircle) {
		super(6, 250000, 500000000, 140, Constants.FLAME_BLURB, vec, ref, sprite);
		_flamecircle = flamecircle;
	}

	@Override
	public boolean action() {
		List<Zombie> zombies = _ref.getZombiesInR(_vec, _radius);
		if (!zombies.isEmpty()) {
			for (Zombie z: zombies) {
				_ref.dealDamage(z, _damage);
				addProjectile(new FireProjectile(getCoords(), _flamecircle));
			}
			return true;
		}
		return false;

	}


}