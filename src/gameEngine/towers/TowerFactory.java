package gameEngine.towers;

import gameEngine.Referee;

import java.awt.image.BufferedImage;

import cs195n.Vec2f;

public class TowerFactory {
	
	
	
	
	private BufferedImage _basicsprite;
	private BufferedImage _cannonsprite;
	private BufferedImage _electricsprite;
	private BufferedImage _flamesprite;
	private BufferedImage _goosprite;
	private BufferedImage _lasersprite;
	private BufferedImage _poisonsprite;
	private BufferedImage _stunsprite;

	public TowerFactory() {
		
	}
	
	
	
	
	
	public BasicTower makeBasic(Vec2f vec, Referee ref) {
		return new BasicTower(vec, ref, _basicsprite);
	}
	
	public CannonTower makeCannon(Vec2f vec, Referee ref) {
		return new CannonTower(vec, ref, _cannonsprite);
	}
	
	public ElectricTower makeElectric(Vec2f vec, Referee ref) {
		return new ElectricTower(vec, ref, _electricsprite);
	}
	
	public FlameTower makeFlame(Vec2f vec, Referee ref) {
		return new FlameTower(vec, ref, _flamesprite);
	}
	
	public GooTower makeGoo(Vec2f vec, Referee ref) {
		return new GooTower(vec, ref, _goosprite);
	}
	
	public LaserTower makeLaser(Vec2f vec, Referee ref) {
		return new LaserTower(vec, ref, _lasersprite);
	}
	
	public PoisonTower makePoison(Vec2f vec, Referee ref) {
		return new PoisonTower(vec, ref, _poisonsprite);
	}
	
	public StunTower makeStun(Vec2f vec, Referee ref) {
		return new StunTower(vec, ref, _stunsprite);
	}
	
	
	
	public BufferedImage getBasicSprite() {
		return _basicsprite;
	}
		
	public BufferedImage getCannonSprite() {
		return _cannonsprite;
	}
	
	public BufferedImage getElectricSprite() {
		return _electricsprite;
	}
	
	public BufferedImage getFlameSprite() {
		return _flamesprite;
	}
	
	public BufferedImage getGooSprite() {
		return _goosprite;
	}
	
	public BufferedImage getLaserSprite() {
		return _lasersprite;
	}
	
	public BufferedImage getPoisonSprite() {
		return _poisonsprite;
	}
	
	public BufferedImage getStunSprite() {
		return _stunsprite;
	}
	
}