package ztdpac;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import cs195n.Vec2f;


public abstract class AbstractTower {
	
	protected double _damage;
	protected double _radius;
	protected double _delay;
	protected Vec2f _vec;
	protected Referee _ref;
	
	public AbstractTower(double damage, double radius, double delay, Vec2f vec, Referee ref) {
		_damage = damage;
		_radius = radius;
		_delay = delay;
		_vec = vec;
		_ref = ref;
	}
	
	
	public abstract void action();
	
	public abstract void draw(Graphics2D g);
	
	public abstract void drawSimple(Graphics2D g);
	
	public void doubleDamage() {
		_damage = 2*_damage;
	}
	
	public void halfDelay() {
		_delay = .5*_delay;
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


