package gameEngine.projectile;

import java.awt.Graphics2D;

public interface Projectile {

	public void action(long nanosSincePrevTick);
	public void draw(Graphics2D g);
}
