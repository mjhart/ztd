package gameEngine.towers;

import cs195n.Vec2f;
import gameEngine.Referee;
import gameEngine.projectile.Projectile;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


public abstract class AbstractTower {
	
	private long _nanosSinceAction;
	protected int _damage;
	protected float _radius;
	protected long _delay;
	private int _price;
	private String _blurb;
	protected Vec2f _vec;
	protected Referee _ref;
	private Set<Projectile> _projectiles;
	private BufferedImage _sprite;
	private boolean _doubled = false;
	private boolean _faster = false;
	private static final int HALF_DELAY_COST = 200;
	private static final int DOUBLE_DAMAGE_COST = 200;
	
	public AbstractTower(int damage, float radius, long delay, int price, String blurb, Vec2f vec, Referee ref, BufferedImage sprite) {
		_damage = damage;
		_radius = radius;
		_delay = delay;
		_price = price;
		_blurb = blurb;
		_vec = vec;
		_ref = ref;
		_projectiles = new HashSet<>();
		_nanosSinceAction = _delay + 1;
		_sprite = sprite;
	}
	
	public void doAction(long nanosSincePrevTick) {
		LinkedList<Projectile> toRemove = new LinkedList<>();
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

	public BufferedImage getSprite() {
		return _sprite;
	}
	
	public boolean intersectRect(Rectangle2D r) {
		int w = _sprite.getWidth();
		int h = _sprite.getHeight();
		return r.intersects(_vec.x - w / 2, _vec.y - h / 2, w, h);
	}
	
	public boolean contains(float x, float y) {
		int w = _sprite.getWidth();
		int h = _sprite.getHeight();
		Rectangle2D r = new Rectangle2D.Float(_vec.x - w/2, _vec.y - h/2, w, h);
		return r.contains(x, y);
	}

	
	public Vec2f getCoords() {
		return _vec;
	}
	
	public void doubleDamage() {
		_damage = 2*_damage;
		_doubled = true;
	}
	
	public void halfDelay() {
		_delay = _delay/2;
		_faster = true;
	}
	
	public int getUpgradeCost(int i) {
		if (i == 1) {
			return HALF_DELAY_COST;
		}
		else {
			return DOUBLE_DAMAGE_COST;
		}
	}
	
	public boolean isUpgraded(int i) {
		if (i == 1) {
			return _faster;
		}
		else {
			return _doubled;
		}
	}
	
	public void addProjectile(Projectile p) {
		if(p != null) {
			_projectiles.add(p);
		}
	}
	
	public int getDamage() {
		return _damage;
	}
	
	public float getRadius() {
		return _radius;
	}
	
	public double getDelay() {
		return _delay/1000000000.0;
	}
	public int getPrice() {
		return _price;
	}
	public String getBlurb() {
		return _blurb;
	}
	
}


