package gameEngine.zombie;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

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
	private BufferedImage[] _sprites;
	private int  _frame;
	private long _nanoSincePrevAnimation;
	private float _angle;
	private double _dist;
	
	public Zombie(Vec2f coords, int health, int strength, MapNode target, Vec2f tCoords, float speed, BufferedImage[] sprites) {
		_coords = coords;
		_health = health;
		_strength = strength;
		_target = target;
		_tCoords = tCoords;
		_speed = speed;
		_sprites = sprites;
		_dist = target.getDist();
	}

	public void move() {
		if(_coords.dist2(_tCoords) < 100) {
			if(_target.getNext() == null) {
				return;
			}
			_target = _target.getNext();
			_tCoords = _target._coords;
		}
		
		Vec2f path = _tCoords.minus(_coords);
		_angle = path.angle();
		_coords = _coords.plus(path.normalized().smult(_speed));
		_dist-=(_speed);
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
	
	public void updateImage(long nanosSincePrevTick) {
		_nanoSincePrevAnimation+=nanosSincePrevTick;
		if(_nanoSincePrevAnimation > 1000000000 / _speed) {
			_nanoSincePrevAnimation = 0;
			_frame = (_frame+1)%_sprites.length;
			return;
		}
		//System.out.println("Not updating image");
	}
	
	public void draw(Graphics2D g) {
		//AffineTransform af = g.getTransform();
		AffineTransform af1 = new AffineTransform();
		af1.translate(_coords.x - (64 * 5), _coords.y - (64 * 5));
		af1.scale(5, 5);
		af1.rotate(_angle, 64, 64);
		g.drawImage(_sprites[_frame], af1, null);
		//g.setTransform(af);
	}
	
	public double getDist() {
		return _dist;
	}
	
	
}
