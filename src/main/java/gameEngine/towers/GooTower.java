package gameEngine.towers;

import cs195n.Vec2f;
import gameEngine.Constants;
import gameEngine.Referee;
import gameEngine.projectile.GooProjectile;
import gameEngine.zombie.Zombie;

import java.awt.image.BufferedImage;
import java.util.List;


public class GooTower extends AbstractTower {

	GooTower(Vec2f vec, Referee ref, BufferedImage sprite) {
		super(10, 1000000, 2000000000, 250, Constants.GOO_BLURB, vec, ref, sprite);
	}

	@Override
	public boolean action() {
		List<Zombie> zlist = _ref.getZombiesInR(_vec, _radius);
		if (zlist.size() != 0) {
			Zombie z = null;
			float maxSpeed = 0;
			double minDist = Double.MAX_VALUE;
			for(Zombie z1 : zlist) {
				if(z1.getSpeed() > maxSpeed) {
					maxSpeed = z1.getSpeed();
					minDist = z1.getDist();
					z = z1;
				}
				else if(z1.getSpeed() == maxSpeed && z1.getDist() < minDist) {
					maxSpeed = z1.getSpeed();
					minDist = z1.getDist();
					z = z1;
				}
			}
			super.addProjectile(new GooProjectile(super._vec, z));
			_ref.dealDamage(z, _damage);
			return true;
		}
		return false;
	}

	
}