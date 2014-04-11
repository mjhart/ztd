package gameEngine;

import mapbuilder.MapNode;
import cs195n.Vec2f;

public abstract class Zombie {
	protected Vec2f _coords;
	protected int _health;
	protected int _strength;
	protected MapNode _target;
	protected Vec2f _tCoords;
	protected float _speed;

	public void move() {
		if(_coords.dist2(_tCoords) < 0.005) {
			_target = _target.getNext();
			_tCoords = new Vec2f((float)_target.lon, (float) _target.lat);
		}
		
		Vec2f path = _tCoords.minus(_coords);
		_coords.plus(path.normalized().smult(_speed));
	}
	
	public Zombie takeDamage(int damage) {
		_health-=damage;
		if(_health <= 0) {
			return this;
		}
		return null;
	}
	
	public Vec2f getCoords() {
		return _coords;
	}
}
