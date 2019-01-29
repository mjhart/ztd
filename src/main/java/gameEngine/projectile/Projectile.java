package gameEngine.projectile;

import java.awt.*;

public interface Projectile {

	/**
	 * @param nanosSincePrevTick
	 * @return True if this projectile should be removed. False
	 * otherwise
	 */
	boolean action(long nanosSincePrevTick);
	void draw(Graphics2D g);
}
