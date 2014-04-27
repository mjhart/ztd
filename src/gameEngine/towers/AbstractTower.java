package gameEngine.towers;

import gameEngine.Referee;
import gameEngine.projectile.Projectile;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import cs195n.Vec2f;
import cs195n.Vec2i;


public abstract class AbstractTower {
	
	private long _nanosSinceAction;
	protected int _damage;
	protected float _radius;
	protected long _delay;
	private int _price;
	protected Vec2f _vec;
	protected Referee _ref;
	private Set<Projectile> _projectiles;
	private BufferedImage _sprite;
	
	public AbstractTower(int damage, float radius, long delay, int price, Vec2f vec, Referee ref, BufferedImage sprite) {
		_damage = damage;
		_radius = radius;
		_delay = delay;
		_price = price;
		_vec = vec;
		_ref = ref;
		_projectiles = new HashSet<Projectile>();
		_nanosSinceAction = _delay + 1;
		_sprite = sprite;
	}
	
	public void doAction(long nanosSincePrevTick) {
		LinkedList<Projectile> toRemove = new LinkedList<Projectile>();
		for(Projectile p : _projectiles) {
			if(p.action(nanosSincePrevTick)) {
				toRemove.add(p);
			}
		}
		_projectiles.removeAll(toRemove);
		_nanosSinceAction+=nanosSincePrevTick;
		if(_nanosSinceAction > _delay) {
			if(action()) {
				_nanosSinceAction = 0;
			}
		}
	}
	
	public abstract boolean action();
	
	public abstract void draw(Graphics2D g, Vec2i coords);
	
	
	protected void drawSimple(Graphics2D g, java.awt.Color c) {
		for(Projectile p : _projectiles) {
			p.draw(g);
		}
		g.setColor(c);
		g.fill(new Rectangle2D.Float(_vec.x - 50, _vec.y - 50, 100, 100));
	}
	
	public void draw2(Graphics2D g) {
		for(Projectile p : _projectiles) {
			p.draw(g);
		}
		AffineTransform af1 = new AffineTransform();
		af1.translate(_vec.x - (_sprite.getWidth() / 2), _vec.y - (_sprite.getHeight() / 2));
		g.drawImage(_sprite, af1, null);
	}
	
	public abstract void drawSimple(Graphics2D g);
	
	public BufferedImage getSprite() {
		return _sprite;
	}

	
	public Vec2f getCoords() {
		return _vec;
	}
	
	public void doubleDamage() {
		_damage = 2*_damage;
	}
	
	public void halfDelay() {
		_delay = _delay/2;
	}
	
	public void removeProjectile(Projectile p) {
		_projectiles.remove(p);
	}
	
	public void addProjectile(Projectile p) {
		if(p != null) {
			_projectiles.add(p);
		}
	}
	
	public float getRadius() {
		return _radius;
	}
	
	public int getPrice() {
		return _price;
	}
	
}


