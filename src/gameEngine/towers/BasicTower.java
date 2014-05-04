package gameEngine.towers;

import gameEngine.Referee;
import gameEngine.projectile.BasicProjectile;
import gameEngine.zombie.Zombie;
import gameEngine.Constants;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cs195n.Vec2f;
import cs195n.Vec2i;


public class BasicTower extends AbstractTower {
	
	private BufferedImage _sprite;
	//private static final String blurb = "A basic tower that shoots regular bullets";

	public BasicTower(Vec2f vec, Referee ref, BufferedImage sprite) {
		super(10, 1000000, 1000000000, 100, Constants.BASIC_BLURB, vec, ref, sprite);
	}

	@Override
	public void drawSimple(Graphics2D g) {
		super.drawSimple(g, java.awt.Color.BLUE);
	}

	@Override
	public boolean action() {
		Zombie z = _ref.getFarthest(_vec, _radius);
		if (z != null) {
			super.addProjectile(new BasicProjectile(super._vec, z.getCoords()));
			_ref.dealDamage(z, _damage);
			System.out.println("Basic Tower Firing");
			return true;
		}
		return false;
	}
	
}