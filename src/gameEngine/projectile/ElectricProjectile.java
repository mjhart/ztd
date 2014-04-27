package gameEngine.projectile;

import java.awt.Graphics2D;

import cs195n.Vec2f;

public class ElectricProjectile implements Projectile {

	private Vec2f _source;
	private Vec2f _dest;
	private long _timeToLive;
	
	public ElectricProjectile(Vec2f source, Vec2f dest) {
		_source = source;
		_dest = dest;
		_timeToLive = 0;
	}
	
	@Override
	public boolean action(long nanosSincePrevTick) {
		_timeToLive += nanosSincePrevTick;
		if(_timeToLive > 100000000) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(java.awt.Color.RED);
		g.drawLine((int)_source.x, (int)_source.y, (int)_dest.x, (int)_dest.y);
	}

}
