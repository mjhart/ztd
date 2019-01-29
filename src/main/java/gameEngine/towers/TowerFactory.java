package gameEngine.towers;

import cs195n.Vec2f;
import gameEngine.Referee;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TowerFactory {
	private BufferedImage _basicsprite;
	private BufferedImage _cannonsprite;
	private BufferedImage _electricsprite;
	private BufferedImage _flamesprite;
	private BufferedImage _goosprite;
	private BufferedImage _lasersprite;
	private BufferedImage _poisonsprite;
	private BufferedImage _flamecircle;
	private BufferedImage[] _explosionSprites;
	private BufferedImage[] _lightningSprites;

	public TowerFactory() {

		// read basic sprite
		_basicsprite = getBufferedImage("/towerpics/tower.png");

		// read flame sprite
		_flamesprite = getBufferedImage("/towerpics/flames.png").getSubimage(0, 0, 48, 64);
		int w = _flamesprite.getWidth();
		int h = _flamesprite.getHeight();
		BufferedImage scaled = new BufferedImage(5*w, 5*h, _flamesprite.getType());
		Graphics2D g = scaled.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_flamesprite, 0, 0, 5*w, 5*h, 0, 0, w, h, null);
	    g.dispose();
	    _flamesprite = scaled;


		// read cannon sprite
		_cannonsprite = getBufferedImage("/towerpics/cannonTower.png");
		w = _cannonsprite.getWidth();
		h = _cannonsprite.getHeight();
		scaled = new BufferedImage(3*w, 3*h, _cannonsprite.getType());
		g = scaled.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_cannonsprite, 0, 0, 3*w, 3*h, 0, 0, w, h, null);
	    g.dispose();
	    _cannonsprite = scaled;


		// read electric sprite
		_electricsprite = getBufferedImage("/towerpics/stuntowersheet.png").getSubimage(20, 0, 68, 118);
		w = _electricsprite.getWidth();
		h = _electricsprite.getHeight();
		scaled = new BufferedImage(4*w, 4*h, _electricsprite.getType());
		g = scaled.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_electricsprite, 0, 0, 4*w, 4*h, 0, 0, w, h, null);
	    g.dispose();
	    _electricsprite = scaled;

		// read goo sprite
		_goosprite = getBufferedImage("/towerpics/gooTowerSheet.png").getSubimage(20, 15, 98, 98);
		w = _goosprite.getWidth();
		h = _goosprite.getHeight();
		scaled = new BufferedImage(4*w, 4*h, _goosprite.getType());
		g = scaled.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_goosprite, 0, 0, 4*w, 4*h, 0, 0, w, h, null);
	    g.dispose();
	    _goosprite = scaled;

		// read poison sprite
		_poisonsprite = getBufferedImage("/towerpics/poisontower.png").getSubimage(10, 10, 108, 98);
		w = _poisonsprite.getWidth();
		h = _poisonsprite.getHeight();
		scaled = new BufferedImage((int) (3.5*w), (int) (3.5*h), _poisonsprite.getType());
		g = scaled.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_poisonsprite, 0, 0, (int) (3.5*w), (int) (3.5*h), 0, 0, w, h, null);
	    g.dispose();
	    _poisonsprite = scaled;

		// read laser sprite
		_lasersprite = getBufferedImage("/towerpics/laserTowerReal.png").getSubimage(0, 0, 1024, 1024);
		w = _lasersprite.getWidth();
		h = _lasersprite.getHeight();
		scaled = new BufferedImage(w/3, h/3, _lasersprite.getType());
		g = scaled.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(_lasersprite, 0, 0, w/3, h/3, 0, 0, w, h, null);
	    g.dispose();
	    _lasersprite = scaled;



		// read electric projectile sprites
		BufferedImage lightningBufferedImg = getBufferedImage("/stuff/lightning.png");
		_lightningSprites = new BufferedImage[3];
		_lightningSprites[0] = lightningBufferedImg.getSubimage(0, 0, 135, 90);
		_lightningSprites[1] = lightningBufferedImg.getSubimage(135, 0, 250, 90);
		_lightningSprites[2] = lightningBufferedImg.getSubimage(385, 0, 405, 90);



		// read electric projectile sprites
		_flamecircle = getBufferedImage("/stuff/flames4.png"
    );


		// read explosion sprites
		_explosionSprites = new BufferedImage[16];
			BufferedImage explosionSpriteSheet = getBufferedImage("/stuff/explosion.png");
		for(int i=0; i<4; i++) {
			for(int j=0; j<4; j++)
			_explosionSprites[i * 4 + j] = explosionSpriteSheet.getSubimage(64 * j, 64 * i, 64, 64);
		}

	}

	private BufferedImage getBufferedImage(String fileName) {
		try {
			return ImageIO.read(getClass().getResource(fileName));
		} catch (IOException e) {
			throw new RuntimeException(e);
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
		return new FlameTower(vec, ref, _flamesprite, _flamecircle);
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
		return new StunTower(vec, ref, null);
	}
}
