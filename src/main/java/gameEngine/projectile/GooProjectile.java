package gameEngine.projectile;

import gameEngine.Constants;
import gameEngine.zombie.Zombie;

import java.awt.Graphics2D;

import cs195n.Vec2f;

public class GooProjectile implements Projectile {

	private Vec2f _coords;
	private Vec2f _path;
	private Vec2f _target;
	private boolean _onZombie;
	private Zombie _zombie;

	public GooProjectile(Vec2f coords, Zombie zombie) {
		_coords = coords;
		_zombie = zombie;
		_target = zombie.getCoords();
		_path =  _target.minus(_coords);
		_onZombie = false;
	}
	
	@Override
	public boolean action(long nanosSincePrevTick) {

		if(_zombie.getHealth() <= 0) {
			return true;
		}
		if(!_onZombie) {
			if(_coords.dist2(_target) < 10000) {
				_onZombie = true;
				_zombie.setSpeed(Constants.GOO_SPEED);
			}
			else {
				_coords = _coords.plus(_path.normalized().smult(100));
			}
		}
		
		return false;
	}
	
	@Override
	public void draw(Graphics2D g) {
		if(_onZombie) {
			g.setColor(java.awt.Color.GREEN);
			g.fillOval((int) _zombie.getCoords().x - 50, (int) _zombie.getCoords().y - 50, 100, 100);
		}
		else {
			g.setColor(java.awt.Color.GREEN);
			g.fillOval((int) _coords.x - 25, (int) _coords.y - 25, 50, 50);
		}
	}

}
