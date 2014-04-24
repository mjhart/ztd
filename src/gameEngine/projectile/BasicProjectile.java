package gameEngine.projectile;

import gameEngine.towers.BasicTower;

import java.awt.Graphics2D;

import cs195n.Vec2f;

public class BasicProjectile implements Projectile {
	
	private Vec2f _coords;
	private Vec2f _path;
	private Vec2f _target;
	private BasicTower _t;

	public BasicProjectile(Vec2f coords, Vec2f target, BasicTower t) {
		System.out.println("Projectile created");
		_coords = coords;
		_path =  target.minus(_coords);
		_target = target;
		_t = t;
	}
	
	@Override
	public void action(long nanosSincePrevTick) {
		System.out.println("Projectile moving");
		if(_coords.dist2(_target) < 1) {
			_t.removeProjectile(this);
		}
		_coords = _coords.plus(_path.normalized());
	}
	
	@Override
	public void draw(Graphics2D g) {
		System.out.println("Drawing projectile at " + _coords.smult(5));
		g.setColor(java.awt.Color.BLACK);
		g.fillOval((int) (_coords.x * 6) - 1, (int) (_coords.y * 5) - 1, 3, 3);
	}
}
