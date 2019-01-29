package gameEngine.projectile;

import cs195n.Vec2f;

import java.awt.*;

import static java.awt.Color.BLACK;

public class BasicProjectile implements Projectile {
	
	private Vec2f _coords;
	private Vec2f _path;
	private Vec2f _target;

	public BasicProjectile(Vec2f coords, Vec2f target) {
		_coords = coords;
		_path =  target.minus(_coords);
		_target = target;
	}
	
	@Override
	public boolean action(long nanosSincePrevTick) {
		if(_coords.dist2(_target) < 10000) {
			return true;
		}
		_coords = _coords.plus(_path.normalized().smult(100));
	return false;
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(BLACK);
		g.fillOval((int) _coords.x - 25, (int) _coords.y - 25, 50, 50);
	}
}
