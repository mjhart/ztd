package gameEngine;

import java.awt.Graphics2D;

import mapbuilder.MapNode;
import cs195n.Vec2f;
import cs195n.Vec2i;

public abstract class Zombie {
	private Vec2f _coords;
	private int _health;
	private int _strength;
	private MapNode _target;
	private Vec2f _tCoords;
	private float _speed;
	private long _nanoSincePrevAttack;
	
	public Zombie(Vec2f coords, int health, int strength, MapNode target, Vec2f tCoords, float speed) {
		_coords = coords;
		_health = health;
		_strength = strength;
		_target = target;
		_tCoords = tCoords;
		_speed = speed;
		
	}

	public void move() {
		if(_coords.dist2(_tCoords) < 1) {
			if(_target.getNext() == null) {
				return;
			}
			_target = _target.getNext();
			_tCoords = _target._coords;
		}
		
		Vec2f path = _tCoords.minus(_coords);
		_coords = _coords.plus(path.normalized().smult(_speed));
	}
	
	public Zombie takeDamage(int damage) {
		_health-=damage;
		if(_health <= 0) {
			System.out.println("Zombie killed");
			return this;
		}
		return null;
	}
	
	public Vec2f getCoords() {
		return _coords;
	}
	
	public int atttack(long nanoSincePrevTick) {
		_nanoSincePrevAttack+=nanoSincePrevTick;
		if(_nanoSincePrevAttack > 1000000000) {
			_nanoSincePrevAttack = 0;
			return _strength;
		}
		return 0;
	}
	
	public abstract void updateImage(long nanosSincePrevTick);
	public abstract void draw(Graphics2D g, Vec2i coords);
	
}
