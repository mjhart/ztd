package gameEngine;

import mapbuilder.MapNode;

public class Base extends MapNode {

	private int _health;
	
	public Base(String id, double lat, double lon) {
		super(id, lat, lon);
		
	}
	
	public boolean dealDamage(int damage) {
		_health-=damage;
		if(_health >= 0) {
			return true;
		}
		return false;
	}

}
