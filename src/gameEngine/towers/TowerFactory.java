package gameEngine.towers;

import gameEngine.Referee;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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
		
		// read flame sprite
		try {
			img = ImageIO.read(new File("towerpics/flames.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (flames.png)");
		}
		_flamesprite = img.getSubimage(0, 0, 48, 64);		
		int w = _flamesprite.getWidth();
		int h = _flamesprite.getHeight();
		BufferedImage scaled = new BufferedImage(5*w, 5*h, _flamesprite.getType());
		Graphics2D g = scaled.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_flamesprite, 0, 0, 5*w, 5*h, 0, 0, w, h, null);
	    g.dispose();
	    _flamesprite = scaled;
		
	    
		// read cannon sprite
		try {
			_cannonsprite = ImageIO.read(new File("towerpics/cannonTower.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (cannonTower.png)");
		}
		w = _cannonsprite.getWidth();
		h = _cannonsprite.getHeight();
		scaled = new BufferedImage(3*w, 3*h, _cannonsprite.getType());
		g = scaled.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_cannonsprite, 0, 0, 3*w, 3*h, 0, 0, w, h, null);
	    g.dispose();
	    _cannonsprite = scaled;
	    
	    
		// read electric sprite
		try {
			img = ImageIO.read(new File("towerpics/stuntowersheet.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (flames.png)");
		}
		_electricsprite = img.getSubimage(20, 0, 68, 118);		
		w = _electricsprite.getWidth();
		h = _electricsprite.getHeight();
		scaled = new BufferedImage(4*w, 4*h, _electricsprite.getType());
		g = scaled.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_electricsprite, 0, 0, 4*w, 4*h, 0, 0, w, h, null);
	    g.dispose();
	    _electricsprite = scaled;
		
		// read goo sprite
		try {
			img = ImageIO.read(new File("towerpics/gooTowerSheet.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (gooTowerSheet.png)");
		}
		_goosprite = img.getSubimage(20, 15, 98, 98);		
		w = _goosprite.getWidth();
		h = _goosprite.getHeight();
		scaled = new BufferedImage(4*w, 4*h, _goosprite.getType());
		g = scaled.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_goosprite, 0, 0, 4*w, 4*h, 0, 0, w, h, null);
	    g.dispose();
	    _goosprite = scaled;
	    
		// read poison sprite
		try {
			img = ImageIO.read(new File("towerpics/poisontower.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (flames.png)");
		}
		_poisonsprite = img.getSubimage(10, 10, 108, 98);		
		w = _poisonsprite.getWidth();
		h = _poisonsprite.getHeight();
		scaled = new BufferedImage(4*w, 4*h, _poisonsprite.getType());
		g = scaled.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_poisonsprite, 0, 0, 4*w, 4*h, 0, 0, w, h, null);
	    g.dispose();
	    _poisonsprite = scaled;
		
		// read laser sprite
		try {
			img = ImageIO.read(new File("towerpics/laserTowerReal.png"));
		} catch (IOException e) {
			System.out.println("ERROR: Could not get image (laserTowerReal.png)");
		}
		_lasersprite = img.getSubimage(0, 0, 1024, 1024);		
		w = _lasersprite.getWidth();
		h = _lasersprite.getHeight();
		scaled = new BufferedImage(w/3, h/3, _lasersprite.getType());
		g = scaled.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_lasersprite, 0, 0, w/3, h/3, 0, 0, w, h, null);
	    g.dispose();
	    _lasersprite = scaled;


	    
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

//Scaling options
/*
//BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//AffineTransform at = new AffineTransform();
//at.scale(.3, .3);
//AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
//scaled = scaleOp.filter(_flamesprite, scaled);
//_flamesprite = scaled;
*/
