package gameEngine.towers;

import gameEngine.Constants;
import gameEngine.Referee;
import gameEngine.zombie.Zombie;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import cs195n.Vec2f;


public class StunTower extends AbstractTower {

	public StunTower(Vec2f vec, Referee ref, BufferedImage sprite) {
		super(10, 100, 1000000000, 120, Constants.STUN_BLURB, vec, ref, sprite);
		
	}

	@Override
	public void drawSimple(Graphics2D g) {
		super.drawSimple(g, java.awt.Color.BLUE);
	}

	@Override
	public boolean action() {
		Zombie z = _ref.getFarthest(_vec, _radius);
		if (z != null) {
			//TODO Goo Projectile
			//super.addProjectile(new GooProjectile(super._vec, z.getCoords(), this));
			return true;
		}
		return false;
	}
	
}