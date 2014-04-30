package gameEngine.projectile;

import java.awt.Graphics2D;

import cs195n.Vec2f;

public class FireProjectile implements Projectile {

	private Vec2f _coords;
	private long _timeAlive;
	
	public FireProjectile(Vec2f coords) {
		_coords = coords;
		_timeAlive = 0;
	}
	
	@Override
	public boolean action(long nanosSincePrevTick) {
		// TODO Auto-generated method stub
		_timeAlive += nanosSincePrevTick;
		if(_timeAlive > 100000000) {
			return true;
		}
		return false;
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(java.awt.Color.ORANGE);
		g.fillOval((int) _coords.x - 500, (int) _coords.y - 500, 1000, 1000);
	}

}
