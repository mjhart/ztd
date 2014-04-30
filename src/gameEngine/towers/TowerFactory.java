package gameEngine.towers;

import gameEngine.Referee;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cs195n.Vec2f;

public class TowerFactory {
	
	
	
	
	private BufferedImage _basicsprite;
	private BufferedImage _cannonsprite;
	private BufferedImage _electricsprite;
	private BufferedImage _flamesprite;
	private BufferedImage _goosprite;
	private BufferedImage _lasersprite;
	private BufferedImage _poisonsprite;
	private BufferedImage _stunsprite;
	private BufferedImage[] _explosionSprites;
	private BufferedImage[] _lightningSprites;

	public TowerFactory() {
		BufferedImage img = null;
		
		// read basic sprite
		try {
			_basicsprite = ImageIO.read(new File("towerpics/tower.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (tower.png)");
		}
		
		// read cannon sprite
		try {
			_cannonsprite = ImageIO.read(new File("towerpics/cannonTower.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (cannonTower.png)");
		}
		
		// read cannon sprite
		try {
			_cannonsprite = ImageIO.read(new File("towerpics/cannonTower.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (cannonTower.png)");
		}
		
		// read electric tower sprite
		try {
			_electricsprite = ImageIO.read(new File("towerpics/cannonTower.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (cannonTower.png)");
		}
		
		// read flame sprite
		try {
			img = ImageIO.read(new File("towerpics/flames.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (flames.png)");
		}
		_flamesprite = img.getSubimage(0, 0, 48, 64);		
		BufferedImage holder = new BufferedImage(96, 128, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = holder.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		//g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer)
		
		
		
		
		
		
		
		
		
		// read electric projectile sprites
		try {
			img = ImageIO.read(new File("stuff/lightning.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (explosion.png)");
		}
		_lightningSprites = new BufferedImage[3];
		_lightningSprites[0] = img.getSubimage(0, 0, 135, 90);
		_lightningSprites[1] = img.getSubimage(135, 0, 250, 90);
		_lightningSprites[2] = img.getSubimage(385, 0, 405, 90);
		
		
		
		// read explosion sprites
		_explosionSprites = new BufferedImage[16];
		try {
			img = ImageIO.read(new File("stuff/explosion.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (explosion.png)");
		}
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++)
			_explosionSprites[i * 4 + j] = img.getSubimage(64 * j, 64 * i, 64, 64);
		}
		
	}
	
	
	
	
	
	public BasicTower makeBasic(Vec2f vec, Referee ref) {
		return new BasicTower(vec, ref, _basicsprite);
	}
	
	public CannonTower makeCannon(Vec2f vec, Referee ref) {
		return new CannonTower(vec, ref, _cannonsprite, _explosionSprites);
	}
	
	public ElectricTower makeElectric(Vec2f vec, Referee ref) {
		return new ElectricTower(vec, ref, _electricsprite, _lightningSprites);
	}
	
	public FlameTower makeFlame(Vec2f vec, Referee ref) {
		return new FlameTower(vec, ref, _flamesprite);
	}
	
	public GooTower makeGoo(Vec2f vec, Referee ref) {
		return new GooTower(vec, ref, _goosprite);
	}
	
	public LaserTower makeLaser(Vec2f vec, Referee ref) {
		return new LaserTower(vec, ref, _lasersprite);
	}
	
	public PoisonTower makePoison(Vec2f vec, Referee ref) {
		return new PoisonTower(vec, ref, _poisonsprite);
	}
	
	public StunTower makeStun(Vec2f vec, Referee ref) {
		return new StunTower(vec, ref, _stunsprite);
	}
	
	
	
	public BufferedImage getBasicSprite() {
		return _basicsprite;
	}
		
	public BufferedImage getCannonSprite() {
		return _cannonsprite;
	}
	
	public BufferedImage getElectricSprite() {
		return _electricsprite;
	}
	
	public BufferedImage getFlameSprite() {
		return _flamesprite;
	}
	
	public BufferedImage getGooSprite() {
		return _goosprite;
	}
	
	public BufferedImage getLaserSprite() {
		return _lasersprite;
	}
	
	public BufferedImage getPoisonSprite() {
		return _poisonsprite;
	}
	
	public BufferedImage getStunSprite() {
		return _stunsprite;
	}
	
}