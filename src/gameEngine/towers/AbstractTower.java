package gameEngine.towers;

import gameEngine.Referee;
import gameEngine.projectile.Projectile;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import cs195n.Vec2f;
import cs195n.Vec2i;


public abstract class AbstractTower {
	
	private long _nanosSinceAction;
	protected int _damage;
	protected double _radius;
	protected long _delay;
	protected Vec2f _vec;
	protected Referee _ref;
	private Set<Projectile> _projectiles;
	
	public AbstractTower(int damage, double radius, long delay, Vec2f vec, Referee ref) {
		_damage = damage;
		_radius = radius;
		_delay = delay;
		_vec = vec;
		_ref = ref;
		_projectiles = new HashSet<Projectile>();
		_nanosSinceAction = _delay + 1;
	}
	
	public void doAction(long nanosSincePrevTick) {
		for(Projectile p : _projectiles) {
			p.action(nanosSincePrevTick);
		}
		_nanosSinceAction+=nanosSincePrevTick;
		if(_nanosSinceAction > _delay) {
			if(action()) {
				_nanosSinceAction = 0;
			}
		}
	}
	
	public abstract boolean action();
	
	public abstract void draw(Graphics2D g, Vec2i coords);
	
	
	public void drawSimple(Graphics2D g, Vec2i coords, java.awt.Color c) {
		g.setColor(c);
		g.fill(new Rectangle2D.Float(coords.x - 5, coords.y - 5, 10, 10));
	}
	
	public void doDraw(Graphics2D g, Vec2i coords) {
		for(Projectile p : _projectiles) {
			p.draw(g);
		}
		drawSimple(g, coords);
	}
	
	public abstract void drawSimple(Graphics2D g, Vec2i coords);

	
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
	
}


