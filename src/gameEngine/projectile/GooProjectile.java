package gameEngine.projectile;

import gameEngine.Constants;
import gameEngine.towers.BasicTower;
import gameEngine.towers.GooTower;
import gameEngine.zombie.Zombie;

import java.awt.Graphics2D;

import cs195n.Vec2f;

public class GooProjectile implements Projectile {

	private Vec2f _coords;
	private Vec2f _path;
	private Vec2f _target;
	private GooTower _t;
	private long _timeAlive;
	private boolean _onZombie;
	private Zombie _zombie;

	public GooProjectile(Vec2f coords, Zombie zombie, GooTower t) {
		//System.out.println("Projectile created");
		_coords = coords;
		_zombie = zombie;
		_target = zombie.getCoords();
		_path =  _target.minus(_coords);
		_t = t;
		_timeAlive = 0;
		_onZombie = false;
	}
	
	@Override
	public boolean action(long nanosSincePrevTick) {
		//System.out.println("Projectile moving");
		if(_onZombie) {
			_timeAlive += nanosSincePrevTick;
			if(_timeAlive > Constants.GOO_DURATION) {
				_zombie.changeSpeed(Constants.GOO_DELTA);
				return true;
				
			}
		}
		else {
			if(_coords.dist2(_target) < 10000) {
				_onZombie = true;
				_zombie.changeSpeed(-Constants.GOO_DELTA);
			}
			_coords = _coords.plus(_path.normalized().smult(100));
		}
		
		return false;
	}
	
	@Override
	public void draw(Graphics2D g) {
		//System.out.println("Drawing projectile at " + _coords.smult(5));
		if(_onZombie) {
			
		}
		else {
			g.setColor(java.awt.Color.GREEN);
			g.fillOval((int) _coords.x - 25, (int) _coords.y - 25, 50, 50);
		}
	}

}
