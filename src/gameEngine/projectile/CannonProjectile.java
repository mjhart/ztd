package gameEngine.projectile;

import gameEngine.towers.BasicTower;
import gameEngine.towers.CannonTower;

import java.awt.Color;
import java.awt.Graphics2D;

import cs195n.Vec2f;

public class CannonProjectile implements Projectile {

	private Vec2f _coords;
	private Vec2f _path;
	private Vec2f _target;
	private CannonTower _t;
	private boolean _explode;
	private long _nanosExploding;

	public CannonProjectile(Vec2f coords, Vec2f target, CannonTower t) {
		System.out.println("Projectile created");
		_coords = coords;
		_path =  target.minus(_coords);
		_target = target;
		_t = t;
	}
	
	@Override
	public void action(long nanosSincePrevTick) {
		// TODO Auto-generated method stub
		if(_explode) {
			_nanosExploding+=nanosSincePrevTick;
			if(_nanosExploding > 100000000l) {
				_t.removeProjectile(this);
			}
		}
		else {
			if(_coords.dist2(_target) < 1) {
				_explode = true;
			}
			_coords = _coords.plus(_path.normalized().smult(0.5f));
		}
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		if(_explode) {
			Color c = g.getColor();
			g.setColor(java.awt.Color.ORANGE);
			g.fillOval((int) (_coords.x * 6) - 20, (int) (_coords.y * 5) - 20, 40, 40);
			g.setColor(c);
		}
		else {
			g.setColor(java.awt.Color.BLACK);
			g.fillOval((int) (_coords.x * 6) - 2, (int) (_coords.y * 5) - 2, 5, 5);
		}
	}

}
