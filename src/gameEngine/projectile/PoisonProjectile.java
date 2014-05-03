package gameEngine.projectile;

import gameEngine.Constants;
import gameEngine.Referee;
import gameEngine.towers.GooTower;
import gameEngine.zombie.Zombie;

import java.awt.Graphics2D;

import cs195n.Vec2f;

public class PoisonProjectile implements Projectile {

	private Vec2f _coords;
	private Vec2f _path;
	private Vec2f _target;
	private long _nanosSince;
	private int _hits;
	private boolean _onZombie;
	private Zombie _zombie;
	private int _damage;
	private Referee _ref;

	public PoisonProjectile(Vec2f coords, Zombie zombie, Referee ref, int damage) {
		//System.out.println("Projectile created");
		_coords = coords;
		_zombie = zombie;
		_target = zombie.getCoords();
		_path =  _target.minus(_coords);
		_nanosSince = 0;
		_onZombie = false;
		_damage = damage;
		_ref = ref;
		_hits = 0;
	}
	
	@Override
	public boolean action(long nanosSincePrevTick) {
		
		if(_onZombie) {
			if(_zombie.getHealth() <= 0) {
				return true;
			}
			_nanosSince += nanosSincePrevTick;
			if(_nanosSince > Constants.POISON_DAMAGE_DELAY) {
				_ref.dealDamage(_zombie, _damage);
				_nanosSince = 0;
				_hits++;
				if(_hits >= Constants.POISON_HITS) {
					return true;
				}
			}
		}
		else {
			if(_coords.dist2(_target) < 10000) {
				_onZombie = true;
			}
			_coords = _coords.plus(_path.normalized().smult(100));
		}
		
		return false;
	}
	
	@Override
	public void draw(Graphics2D g) {
		//System.out.println("Drawing projectile at " + _coords.smult(5));
		if(_onZombie) {
			g.setColor(java.awt.Color.GREEN.darker());
			g.fillOval((int) _zombie.getCoords().x - 50, (int) _zombie.getCoords().y - 50, 100, 100);
		}
		else {
			g.setColor(java.awt.Color.GREEN.darker());
			g.fillOval((int) _coords.x - 25, (int) _coords.y - 25, 50, 50);
		}
	}

}
