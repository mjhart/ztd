package gameEngine.towers;

import gameEngine.Constants;
import gameEngine.Referee;
import gameEngine.projectile.BasicProjectile;
import gameEngine.projectile.GooProjectile;
import gameEngine.projectile.PoisonProjectile;
import gameEngine.zombie.Zombie;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import cs195n.Vec2f;
import cs195n.Vec2i;


public class PoisonTower extends AbstractTower {

	public PoisonTower(Vec2f vec, Referee ref, BufferedImage sprite) {
		super(10, 1000000, 3000000000l, 250, Constants.POISON_BLURB, vec, ref, sprite);
		
	}
	
	@Override
	public void draw(Graphics2D g, Vec2i coords) {
		g.setColor(java.awt.Color.BLUE);
		g.fill(new Rectangle2D.Float(coords.x, coords.y, 10, 10));
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
			super.addProjectile(new PoisonProjectile(super._vec, z, super._ref, super._damage));
			System.out.println("Goo Tower Firing");
			return true;
		}
		return false;
	}
	
}