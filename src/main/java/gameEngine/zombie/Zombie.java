package gameEngine.zombie;

import gameEngine.Base;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import mapbuilder.MapNode;
import cs195n.Vec2f;

public abstract class Zombie {
	private Vec2f _coords;
	private int _startHealth;
	private int _health;
	private int _strength;
	private MapNode _target;
	private float _speed;
	private long _nanoSincePrevAttack;
	private BufferedImage[] _sprites;
	private BufferedImage[] _attack;
	private int  _spriteFrame;
	private int _attackFrame;
	private long _nanoSincePrevAnimation;
	private float _angle;
	private double _dist;
	private Base _base;
	protected boolean _atBase;
	
	public Zombie(Vec2f coords, int health, int strength, MapNode target, float speed, BufferedImage[] sprites, BufferedImage[] attack, Base base) {
		_coords = coords;
		_health = health;
		_startHealth = health;
		_strength = strength;
		_target = target;
		_speed = speed;
		_sprites = sprites;
		_dist = target.getDist();
		_base = base;
		_atBase = false;
		_nanoSincePrevAttack = 1000000001;
		_attack = attack;
	}

	public void move(long nanosSincePrevTick) {
		if(!_atBase) {
			if(_coords.dist2(_target._coords) < 1000) {
				if(_target.getNext() == null) {
					_atBase = true;
					_attackFrame = 0;
					return;
				}
				_target = _target.getNext();
			}

			Vec2f path = _target._coords.minus(_coords);
			_angle = path.angle();
			_coords = _coords.plus(path.normalized().smult(_speed));
			_dist-=(_speed);
		}
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
	
	public int atttack(long nanoSincePrevTick) {
		_nanoSincePrevAttack+=nanoSincePrevTick;
		if(_nanoSincePrevAttack > 1000000000) {
			_nanoSincePrevAttack = 0;
			if(_atBase) {
				return _strength;
			}
		}
		return 0;
	}
	
	public void updateImage(long nanosSincePrevTick) {
		_nanoSincePrevAnimation+=nanosSincePrevTick;
		if(_nanoSincePrevAnimation > 1000000000 / _speed) {
			_nanoSincePrevAnimation = 0;
			_spriteFrame = (_spriteFrame+1)%_sprites.length;
			_attackFrame = (_attackFrame+1)%_sprites.length;
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
		if(_atBase) {
			g.drawImage(_attack[_attackFrame], af1, null);
		}
		else {
			g.drawImage(_sprites[_spriteFrame], af1, null);
		}
		
		g.setColor(java.awt.Color.RED);
		g.setStroke(new BasicStroke(20));
		g.drawLine((int) _coords.x - 60, (int) _coords.y - 5, (int) _coords.x + 60, (int) _coords.y - 5);
		g.setColor(java.awt.Color.GREEN);
		g.drawLine((int) _coords.x - 60, (int) _coords.y - 5, (int) (_coords.x - 60 + ((float)_health)/((float) _startHealth) * 120), (int) _coords.y - 5);
		//g.setTransform(af);
	}
	
	public double getDist() {
		return _dist;
	}
	
	public void setSpeed(float speed) {
		if(speed >= 0) {
			_speed = speed;
		}
	}
	
	public float getSpeed() {
		return _speed;
	}
	
	public int getHealth() {
		return _health;
	}
	
	
}
