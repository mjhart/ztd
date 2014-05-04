package gameEngine.towers;

import gameEngine.Constants;
import gameEngine.Referee;
import gameEngine.projectile.CannonProjectile;
import gameEngine.zombie.Zombie;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import cs195n.Vec2f;
import cs195n.Vec2i;


public class CannonTower extends AbstractTower {
	
	private BufferedImage[] _explosionSprites;
	public CannonTower(Vec2f vec, Referee ref, BufferedImage sprite, BufferedImage[] explosionSprites) {
		super(30, 4000000, 5000000000l, 150, Constants.CANNON_BLURB, vec, ref, sprite);
		_explosionSprites = explosionSprites;
	}
	

	@Override
	public void drawSimple(Graphics2D g) {
		super.drawSimple(g, java.awt.Color.BLACK);
	}

	@Override
	public boolean action() {
		Zombie z = _ref.getFarthest(_vec, _radius);
		if (z != null) {
			super.addProjectile(new CannonProjectile(super._vec, z.getCoords(), this, _ref, _damage, _explosionSprites));
			System.out.println("Cannon Tower Firing");
			return true;
		}
		return false;
	}
	
}