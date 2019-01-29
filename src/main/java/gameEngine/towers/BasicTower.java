package gameEngine.towers;

import cs195n.Vec2f;
import gameEngine.Constants;
import gameEngine.Referee;
import gameEngine.projectile.BasicProjectile;
import gameEngine.zombie.Zombie;

import java.awt.image.BufferedImage;


public class BasicTower extends AbstractTower {
	
	private BufferedImage _sprite;

	public BasicTower(Vec2f vec, Referee ref, BufferedImage sprite) {
		super(10, 1000000, 700000000, 100, Constants.BASIC_BLURB, vec, ref, sprite);
	}

	@Override
	public boolean action() {
		Zombie z = _ref.getFarthest(_vec, _radius);
		if (z != null) {
			super.addProjectile(new BasicProjectile(super._vec, z.getCoords()));
			_ref.dealDamage(z, _damage);
			return true;
		}
		return false;
	}
	
}