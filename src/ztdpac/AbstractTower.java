package ztdpac;

import java.awt.Graphics2D;

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
	

}



//Abstract Tower
//Tower(damage, radius, coordinates)
//action(): Search for zombies within radius and deal damage on them, working with the referee. Different towers may not perform the action on every call. For example, a bomb tower might wait 3 seconds before it deals damage.
//halfDelay(): Halves the delay time for the tower
//doubleDamage(): Doubles the damage a tower delivers
//Towers can call getNeighbors in Engine to find zombies within their radius
