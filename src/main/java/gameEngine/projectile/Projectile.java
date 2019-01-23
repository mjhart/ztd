package gameEngine.projectile;

import java.awt.Graphics2D;

public interface Projectile {

	/**
	 * @param nanosSincePrevTick
	 * @return True if this projectile should be removed. False
	 * otherwise
	 */
	public boolean action(long nanosSincePrevTick);
	public void draw(Graphics2D g);
}
