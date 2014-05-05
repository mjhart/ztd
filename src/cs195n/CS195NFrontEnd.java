package cs195n;

import java.awt.Graphics2D;
import java.awt.event.*;

/**
 * Superclass for front-ends. At the moment the only concrete implementation is
 * {@link SwingFrontEnd}, but someday we may have a GLFrontEnd.<br>
 * <ul>
 * </ul>
 * Student subclass responsibilities:
 * <ul>
 * <li>Override all <code>on</code>*** methods.</li>
 * </ul>
 * 
 * Support code subclass responsibilities:<br>
 * <ul>
 * <li>After call to <code>super()</code>, either go fullscreen or create a
 * window such that the drawable area is equal to <code>windowedSize</code>.</li>
 * <li>Implement all <code>do</code>*** methods and make them final.</li>
 * <li>Ensure all <code>on</code>*** methods are called appropriately.</li>
 * <li>Ensure no variables or methods are accidentally exposed to student
 * subclasses.</li>
 * </ul>
 * 
 * @author zdavis
 */
public abstract class CS195NFrontEnd {
	/*
	 * --------------------------------------------------------------------------
	 * ------------------ To be overridden by student
	 * ----------------------------
	 * ----------------------------------------------------------------
	 */

	/**
	 * Called at a regular interval set by {@link #setTickFrequency(long)}. Use
	 * to update any state that changes over time.
	 * 
	 * @param nanosSincePreviousTick
	 *            approximate number of nanoseconds since the previous call to
	 *            onTick
	 */
	protected abstract void onTick(long nanosSincePreviousTick);

	/**
	 * Called when the screen needs to be redrawn. This is at least once per
	 * tick, but possibly more frequently (for example, when the window is
	 * resizing). <br>
	 * <br>
	 * Note that the entire drawing area is cleared before each call to this
	 * method. Furthermore, {@link #onResize} is guaranteed to be called before
	 * the first invocation of onDraw.
	 * 
	 * @param g
	 *            a {@link Graphics2D} object used for drawing.
	 */
	protected abstract void onDraw(Graphics2D g);

	/**
	 * @param e
	 *            an AWT {@link KeyEvent} representing the input event.
	 * @see KeyListener#keyTyped(KeyEvent)
	 */
	protected abstract void onKeyTyped(KeyEvent e);

	/**
	 * @param e
	 *            an AWT {@link KeyEvent} representing the input event.
	 * @see KeyListener#keyPressed(KeyEvent)
	 */
	protected abstract void onKeyPressed(KeyEvent e);

	/**
	 * @param e
	 *            an AWT {@link KeyEvent} representing the input event.
	 * @see KeyListener#keyReleased(KeyEvent)
	 */
	protected abstract void onKeyReleased(KeyEvent e);

	/**
	 * @param e
	 *            an AWT {@link MouseEvent} representing the input event.
	 * @see MouseListener#mouseClicked(MouseEvent)
	 */
	protected abstract void onMouseClicked(MouseEvent e);

	/**
	 * @param e
	 *            an AWT {@link MouseEvent} representing the input event.
	 * @see MouseListener#mousePressed(MouseEvent)
	 */
	protected abstract void onMousePressed(MouseEvent e);

	/**
	 * @param e
	 *            an AWT {@link MouseEvent} representing the input event.
	 * @see MouseListener#mouseReleased(MouseEvent)
	 */
	protected abstract void onMouseReleased(MouseEvent e);

	/**
	 * @param e
	 *            an AWT {@link MouseEvent} representing the input event.
	 * @see MouseMotionListener#mouseDragged(MouseEvent)
	 */
	protected abstract void onMouseDragged(MouseEvent e);

	/**
	 * @param e
	 *            an AWT {@link MouseEvent} representing the input event.
	 * @see MouseMotionListener#mouseMoved(MouseEvent)
	 */
	protected abstract void onMouseMoved(MouseEvent e);

	/**
	 * @param e
	 *            an AWT {@link MouseWheelEvent} representing the input event.
	 * @see MouseWheelListener#mouseWheelMoved(MouseWheelEvent)
	 */
	protected abstract void onMouseWheelMoved(MouseWheelEvent e);

	/**
	 * Called when the size of the drawing area changes. Any subsequent calls to
	 * onDraw should note the new size and be sure to fill the entire area
	 * appropriately. Guaranteed to be called before the first call to onDraw.
	 * 
	 * @param newSize
	 *            the new size of the drawing area.
	 */
	protected abstract void onResize(Vec2i newSize);

	/*
	 * --------------------------------------------------------------------------
	 * ------------------ Public methods
	 * ----------------------------------------
	 * ----------------------------------------------------
	 */

	/**
	 * Begin processing events. None of the <code>on</code>*** methods will be
	 * called before this is called.
	 */
	public final void startup() {
		if (!running) {
			running = true;
			doStartup();
		}
	}

	/**
	 * Stop processing events. Since there may already be events pending, a few
	 * <code>on</code>*** methods may be called even after this is called. <br>
	 * <br>
	 * Calling this method is not strictly necessary, but useful if you want to
	 * close the front-end without exiting the program (e.g. if you write a map
	 * editor that directly launches the game).
	 */
	public final void shutdown() {
		if (running) {
			doShutdown();
			running = false;
		}
	}

	/**
	 * Returns whether or not events are currently being processed
	 * (specifically, if {@link #startup()} has been called without a
	 * corresponding call to {@link #shutdown()}).
	 * 
	 * @return true if the front-end is running, false if not
	 */
	public final boolean isRunning() {
		return running;
	}

	/**
	 * Switches the front-end between fullscreen and windowed.
	 * 
	 * @param fullscreen
	 *            true for fullscreen, false for windowed
	 */
	public final void setFullscreen(boolean fullscreen) {
		if (fullscreen != this.fullscreen) {
			this.fullscreen = fullscreen;
			doSetFullscreen();
		}
	}

	/**
	 * Gets whether the front-end is currently fullscreen or windowed.
	 * 
	 * @return true if the fullscreen is currently set, false if windowed
	 */
	public final boolean isFullscreen() {
		return fullscreen;
	}

	/**
	 * Enable or disable debug mode. When debug mode is enabled, the current
	 * size of the draw area and aspect ratio is displayed in the title of the
	 * window along with the current FPS count, and pressing F12 will bring up a
	 * dialog to resize the window.
	 * 
	 * @param debug
	 *            true to enable debug mode, false to disable
	 */
	public final void setDebugMode(boolean debug) {
		if (debug != this.debug) {
			this.debug = debug;
			doSetDebugMode();
		}
	}

	/**
	 * Gets whether debug mode is currently set.
	 * 
	 * @return true if the debug mode is currently on, false if not
	 */
	public final boolean isDebugMode() {
		return debug;
	}

	/**
	 * Controls the frequency of {@link #onTick(long) onTick()} calls. Ticks
	 * will occur approximately once every <code>nanoDelay</code> nanoseconds,
	 * but <b>no specific accuracy guarantees can be made</b>. Always use the
	 * argument of onTick to determine the actual amount of time passed.
	 * 
	 * @param nanoDelay
	 *            the number of nanoseconds between the start of each ticks. For
	 *            example, for 50 FPS, this value would be 1000000000/50
	 *            (20000000). Must be >= 0.
	 */
	public final void setTickFrequency(long nanoDelay) {
		if (nanoDelay < 0)
			throw new IllegalArgumentException("nanoDelay must be >= 0");
		doSetTickFrequency(nanoDelay);
	}

	/*
	 * --------------------------------------------------------------------------
	 * ------------------ Methods to be overridden by front-end implementations
	 * --
	 * ------------------------------------------------------------------------
	 * ------------------
	 */

	/**
	 * Actually set the tick frequency. <code>nanoDelay</code> is guaranteed to
	 * be valid (>= 0).
	 */
	abstract void doSetTickFrequency(long nanoDelay);

	/**
	 * Actually run code to go fullscreen or windowed. The
	 * <code>fullscreen</code> field will hold the value of the desired new
	 * state. When returning from fullscreen back to windowed, it's nice if the
	 * subclass remembers the old window size and position, but this behavior is
	 * not strictly required.
	 */
	abstract void doSetFullscreen();

	/**
	 * Actually run code to enable or disable debug mode. The <code>debug</code>
	 * field will hold the value of the desired new state.
	 */
	abstract void doSetDebugMode();

	/**
	 * Actually run code to start up. NO EVENTS SHOULD BE DELIVERED BEFORE THIS
	 * IS CALLED IN CASE THE USER WANTS TO DO SETUP.
	 */
	abstract void doStartup();

	/**
	 * Actually run code to shut down.
	 */
	abstract void doShutdown();

	/*
	 * --------------------------------------------------------------------------
	 * ------------------ Private stuff
	 * ------------------------------------------
	 * --------------------------------------------------
	 */

	/**
	 * The default window size; should be passed to the constructor if the user
	 * does not specify a window size.
	 */
	static final Vec2i DEFAULT_WINDOW_SIZE = new Vec2i(980, 800);

	/**
	 * The minimum window size. Games are expected to work with resolutions at
	 * least this small; smaller resolutions will not be tested or graded, and
	 * the window should not allow itself to be resized any smaller than this.
	 */
	static final Vec2i MINIMUM_WINDOW_SIZE = new Vec2i(980, 800);

	// default access is intentional
	boolean fullscreen;
	Vec2i windowedSize;
	volatile boolean running = false;
	boolean debug = true;

	/**
	 * Constructor. Note that this currently just stores the values; subclasses
	 * should actually create a window or go fullscreen after this based on the
	 * values of <code>fullscreen</code> and <code>windowSize</code>.
	 * 
	 * @param fullscreen
	 *            true for starting in fullscreen, false for starting in a
	 *            window
	 * @param windowSize
	 *            the starting window size
	 */
	CS195NFrontEnd(boolean fullscreen, Vec2i windowSize) {
		this.fullscreen = fullscreen;
		this.windowedSize = windowSize;
	}
}
