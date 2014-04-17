package gameEngine;

import java.awt.Graphics2D;

import cs195n.Vec2f;

import mapbuilder.MapNode;

public class Base extends BasicTower {

	private int _health;
	private MapNode _baseNode;
	
	public Base(MapNode n, Vec2f vec, Referee ref) {
		super(vec, ref);
		_baseNode = n;
		_health = 100;
	}
	
	public boolean dealDamage(int damage) {
		System.out.println("Base took " + damage + " damage");
		_health-=damage;
		if(_health <= 0) {
			System.out.println("Base Destroyed!!");
			return true;
		}
		return false;
	}
	
	public MapNode getNode() {
		return _baseNode;
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawSimple(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

}
