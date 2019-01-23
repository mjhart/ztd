package gameEngine.projectile;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class LaserProjectile extends Line2D.Float implements Projectile {

	private long _timeToLive;
	
	public LaserProjectile(float x1, float y1, float x2, float y2) {
		super(x1, y1, x2, y2);
		_timeToLive = 0;
	}
	
	@Override
	public boolean action(long nanosSincePrevTick) {
		_timeToLive+=nanosSincePrevTick;
		if(_timeToLive > 100000000l) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(java.awt.Color.RED);
		g.draw(this);
	}

}
