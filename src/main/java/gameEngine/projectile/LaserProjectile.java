package gameEngine.projectile;

import java.awt.*;
import java.awt.geom.Line2D;

import static java.awt.Color.RED;

public class LaserProjectile extends Line2D.Float implements Projectile {

	private long _timeToLive;
	
	public LaserProjectile(float x1, float y1, float x2, float y2) {
		super(x1, y1, x2, y2);
		_timeToLive = 0;
	}
	
	@Override
	public boolean action(long nanosSincePrevTick) {
		_timeToLive+=nanosSincePrevTick;
		return _timeToLive > 100000000l;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(RED);
		g.draw(this);
	}

}
