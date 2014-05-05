package gameEngine.projectile;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import cs195n.Vec2f;

public class FireProjectile implements Projectile {

	private Vec2f _coords;
	private long _timeAlive;
	private BufferedImage _flamecircle;
	
	public FireProjectile(Vec2f coords, BufferedImage flamecircle) {
		_coords = coords;
		_timeAlive = 0;
		_flamecircle = flamecircle;
	}
	
	@Override
	public boolean action(long nanosSincePrevTick) {
		_timeAlive += nanosSincePrevTick;
		if(_timeAlive > 100000000) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Graphics2D g) {
//		g.setColor(java.awt.Color.ORANGE);
//		g.fillOval((int) _coords.x - 500, (int) _coords.y - 500, 1000, 1000);
		AffineTransform af = new AffineTransform();
		af.translate(_coords.x - 144 * 3.2, _coords.y - 144 * 3.2);
		af.scale(3, 3);
		g.drawImage(_flamecircle, af, null);
	}

}
