package gameEngine.towers;

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
	public CannonTower(Vec2f vec, Referee ref) {
		super(30, 100, 5000000000l, vec, ref);
		BufferedImage img = null;
		_explosionSprites = new BufferedImage[16];
		try {
			img = ImageIO.read(new File("stuff/explosion.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (SpriteImp)");
		}
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++)
			_explosionSprites[i * 4 + j] = img.getSubimage(64 * j, 64 * i, 64, 64);
		}
	}
	
	@Override
	public void draw(Graphics2D g, Vec2i coords) {
		g.setColor(java.awt.Color.BLACK);
		g.fill(new Rectangle2D.Float(coords.x, coords.y, 10, 10));
	}

	@Override
	public void drawSimple(Graphics2D g, Vec2i coords) {
		super.drawSimple(g, coords, java.awt.Color.BLACK);
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