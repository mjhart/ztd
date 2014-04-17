package gameEngine;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;


import cs195n.Vec2f;


public abstract class AbstractTower {
	
	private long _nanosSinceAction;
	protected int _damage;
	protected double _radius;
	protected long _delay;
	protected Vec2f _vec;
	protected Referee _ref;
	
	public AbstractTower(int damage, double radius, long delay, Vec2f vec, Referee ref) {
		_damage = damage;
		_radius = radius;
		_delay = delay;
		_vec = vec;
		_ref = ref;
	}
	
	public void doAction(long nanosSincePrevTick) {
		_nanosSinceAction+=nanosSincePrevTick;
		if(_nanosSinceAction > _delay) {
			_nanosSinceAction = 0;
			action();
		}
	}
	
	public abstract void action();
	
	public abstract void draw(Graphics2D g);
	
	public abstract void drawSimple(Graphics2D g);
	
	public void doubleDamage() {
		_damage = 2*_damage;
	}
	
	public void halfDelay() {
		_delay = _delay/2;
	}
	

//	//Definitely move to Map
//	public boolean canBuild(int x, int y) {
//		boolean canbuild = true;
//		Rectangle2D r = new Rectangle2D.Float(x+5, y+5, 10, 10);
//		for (Line2D h: _highlines) {
//			if (h.intersects(r)) {
//				canbuild = false;
//			}
//		}
//		return canbuild;
//	}
	
	
	
	
}


