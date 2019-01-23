package gameEngine;

import gameEngine.towers.BasicTower;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import cs195n.Vec2f;
import cs195n.Vec2i;
import mapbuilder.MapNode;

public class Base extends BasicTower {

	private int _health;
	private MapNode _baseNode;
	
	public Base(MapNode n, Vec2f vec, Referee ref, BufferedImage sprite) {
		super(vec, ref, sprite);
		_baseNode = n;
		_health = 100;
	}
	
	public boolean dealDamage(int damage) {
		if(_health - damage <= 0) {
			_health = 0;
			return true;
		}
		_health-=damage;
		
		return false;
	}
	
	public MapNode getNode() {
		return _baseNode;
	}
	
	public int getHealth() {
		return _health;
	}
	
	public void setHealth(int health) {
		_health = health;
	}

	@Override
	public void drawSimple(Graphics2D g) {
		super.drawSimple(g, java.awt.Color.BLUE);
		
	}

}
