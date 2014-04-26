package cs195n;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.regex.*;

import javax.swing.*;

/**
 * An implementation of {@link CS195NFrontEnd} that uses Swing/AWT for drawing
 * and events. <br>
 * <br>
 * To use, create a subclass that overrides the various <code>on</code>***
 * methods of CS195NFrontEnd and call {@link #startup()} to begin processing
 * events.
 * 
 * @author zdavis
 */
public abstract class SwingFrontEnd extends CS195NFrontEnd {
	/**
	 * Amount of time between ticks. A value of "1000 / N" means about N frames
	 * per second
	 */
	private static final int DEFAULT_DELAY_MILLIS = 1000 / 75;
	/**
	 * Number of frames to average in FPS count
	 */
	private static final int NUM_FRAMES_TO_AVERAGE = 10;
	/**
	 * Number of milliseconds to wait for another keyPressed before dispatching
	 * a keyReleased
	 */
	private static final int MILLIS_TO_WAIT_FOR_REPEAT = 5;

	static {
		// try to set look-and-feel to current platform
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable t) {
			// too bad
		}
	}

	private JFrame frame;
	private DrawPanel panel;
	private Point framePos;
	private int closeOp;
	private String title;
	private Vec2i clientSize;

	private long[] tickTimes = new long[NUM_FRAMES_TO_AVERAGE];
	private int tickTimesIndex = -1;

	private Timer timer;
	private long lastTickNanos;

	/**
	 * Creates the front-end with a default window size. Note that events will
	 * not begin until {@link #startup()} is called.
	 * 
	 * @param title
	 *            the title of the window
	 * @param fullscreen
	 *            true for fullscreen, false for windowed
	 */
	public SwingFrontEnd(String title, boolean fullscreen) {
		this(title, fullscreen, DEFAULT_WINDOW_SIZE);
	}

	/**
	 * Creates the front-end. Note that events will not begin until
	 * {@link #startup()} is called.
	 * 
	 * @param title
	 *            the title of the window
	 * @param fullscreen
	 *            true for fullscreen, false for windowed
	 * @param windowSize
	 *            the starting size of the window
	 */
	public SwingFrontEnd(String title, boolean fullscreen, Vec2i windowSize) {
		this(title, fullscreen, windowSize, JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Creates the front-end. Note that events will not begin until
	 * {@link #startup()} is called.
	 * 
	 * @param title
	 *            the title of the window
	 * @param fullscreen
	 *            true for fullscreen, false for windowed
	 * @param windowSize
	 *            the starting size of the window
	 * @param closeOp
	 *            argument to be passed to
	 *            {@link JFrame#setDefaultCloseOperation(int)}.
	 */
	public SwingFrontEnd(String title, boolean fullscreen, Vec2i windowSize,
			int closeOp) {
		super(fullscreen, windowSize);
		this.title = title;
		this.closeOp = closeOp;
		clientSize = windowSize;

		panel = new DrawPanel();
		panel.setMinimumSize(new Dimension(MINIMUM_WINDOW_SIZE.x,
				MINIMUM_WINDOW_SIZE.y));

		timer = new Timer(DEFAULT_DELAY_MILLIS, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doTick();
			}
		});
	}

	@Override
	final void doSetTickFrequency(long nanoDelay) {
		int milliDelay = (int) (nanoDelay / 1000000);
		timer.setDelay(milliDelay);
		timer.setInitialDelay(milliDelay);
	}

	@Override
	final void doSetFullscreen() {
		if (frame != null) {
			if (fullscreen)
				framePos = frame.getLocation();
			panel.stopListening();
			frame.remove(panel);
			frame.dispose();
		}

		frame = new JFrame();
		frame.setDefaultCloseOperation(closeOp);
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		//frame.setMinimumSize(new Dimension(MINIMUM_WINDOW_SIZE.x, MINIMUM_WINDOW_SIZE.y));

		frame.setUndecorated(fullscreen);
		if (fullscreen) {
			Rectangle bounds = frame.getGraphicsConfiguration().getBounds();
			frame.setBounds(bounds);
		} else {
			// uncomment to make minimum window size the minimum size we'll test
			 panel.setPreferredSize(new Dimension(MINIMUM_WINDOW_SIZE.x,
			 MINIMUM_WINDOW_SIZE.y));
			 frame.pack();
			 frame.setMinimumSize(frame.getSize());

			panel.setPreferredSize(new Dimension(windowedSize.x, windowedSize.y));
			frame.pack();

			// workaround for department jvm install being buggy
			panel.callOnResize(panel.getSize().width, panel.getSize().height);

			if (framePos == null) {
				frame.setLocationRelativeTo(null);
			} else {
				frame.setLocation(framePos);
			}
		}

		updateTitle();
		panel.startListening();

		if (running)
			frame.setVisible(true);
	}

	@Override
	final void doSetDebugMode() {
		updateTitle();
	}

	@Override
	final void doStartup() {
		doSetFullscreen();
		timer.start();
		lastTickNanos = System.nanoTime();
	}

	@Override
	final void doShutdown() {
		timer.stop();
		panel.stopListening();
		frame.remove(panel);
		frame.dispose();
		frame = null;
	}

	final void doTick() {
		long currentNanos = System.nanoTime();
		long delta = currentNanos - lastTickNanos;

		tickTimes[tickTimesIndex = (tickTimesIndex + 1) % NUM_FRAMES_TO_AVERAGE] = delta;
		if (debug) {
			updateTitle();
		}

		try {
			onTick(delta);
		} catch (Throwable t) {
			throwableGenerated("onTick", t);
		}
		panel.repaint();
		lastTickNanos = currentNanos;
	}

	private float getFPS() {
		long sum = 0;
		for (long val : tickTimes) {
			sum += val;
		}
		return (1000000000.f * NUM_FRAMES_TO_AVERAGE) / sum;
	}

	private void updateTitle() {
		if (frame != null) {
			String title;
			if (debug) {
				title = String
						.format("%s [size= %s, aspect= %.6f:1, FPS= %.3f]",
								this.title, clientSize,
								((float) clientSize.x / (float) clientSize.y),
								getFPS());
			} else {
				title = this.title;
			}
			frame.setTitle(title);
		}
	}

	private Pattern resizePattern = Pattern
			.compile("[^0-9]*([0-9]+)[^0-9]+([0-9]+)");

	private void openResizeDialog() {
		if (fullscreen)
			return;

		String result = JOptionPane.showInputDialog(frame,
				"Please enter the exact width and height you want to test",
				"Resize", JOptionPane.QUESTION_MESSAGE);

		if (result == null)
			return;

		Matcher m = resizePattern.matcher(result);
		if (m.lookingAt()) {
			try {
				int width = Integer.valueOf(m.group(1));
				int height = Integer.valueOf(m.group(2));
				panel.setPreferredSize(new Dimension(width, height));
				frame.pack();
				return;
			} catch (NumberFormatException e) {
			}
		}

		JOptionPane.showMessageDialog(frame,
				"Could not parse width and/or height from input", "Error",
				JOptionPane.ERROR_MESSAGE);
	}

	private synchronized void throwableGenerated(String method, Throwable t) {
		System.out.flush();
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		System.err.flush();
		t.printStackTrace();
		System.err
				.println("\n***An uncaught "
						+ t.getClass().getSimpleName()
						+ " (thrown in "
						+ getClass().getSimpleName()
						+ "."
						+ method
						+ ") propagated up to the front-end, check above stack trace for details***");
		System.err.flush();
		System.exit(1);
	}

	@SuppressWarnings("serial")
	private class DrawPanel extends JComponent implements MouseListener,
			MouseMotionListener, MouseWheelListener, KeyListener,
			ComponentListener, KeyEventDispatcher {
		public DrawPanel() {
			setDoubleBuffered(true);
			setOpaque(true);
		}

		boolean resizeNotCalled = true;

		void startListening() {
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
			addComponentListener(this);
			KeyboardFocusManager.getCurrentKeyboardFocusManager()
					.addKeyEventDispatcher(this);
		}

		void stopListening() {
			removeMouseListener(this);
			removeMouseMotionListener(this);
			removeMouseWheelListener(this);
			removeComponentListener(this);
			KeyboardFocusManager.getCurrentKeyboardFocusManager()
					.removeKeyEventDispatcher(this);
		}

		public void callOnResize(int width, int height) {
			resizeNotCalled = false;
			Vec2i newSize = new Vec2i(width, height);
			clientSize = newSize;
			if (debug) {
				updateTitle();
			}
			if (!fullscreen) {
				windowedSize = newSize;
			}
			try {
				onResize(newSize);
			} catch (Throwable t) {
				throwableGenerated("onResize", t);
			}
		}

		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (frame.isFocused() && !e.isConsumed()) {
				switch (e.getID()) {
				case KeyEvent.KEY_PRESSED:
					keyPressed(e);
					return true;
				case KeyEvent.KEY_RELEASED:
					queueKeyReleased(e);
					return true;
				case KeyEvent.KEY_TYPED:
					keyTyped(e);
					return true;
				}
			}
			return false;
		}

		@Override
		public void paint(Graphics g) {
			Rectangle r = g.getClipBounds();
			g.clearRect(r.x, r.y, r.width, r.height);
			if (resizeNotCalled) {
				callOnResize(r.width, r.height);
			}
			try {
				onDraw((Graphics2D) g);
			} catch (Throwable t) {
				throwableGenerated("onDraw", t);
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {
			try {
				onKeyTyped(e);
			} catch (Throwable t) {
				throwableGenerated("onKeyTyped", t);
			}
		}

		boolean lastF11WasRelease = true;

		@Override
		public void keyPressed(KeyEvent e) {
			notifyKeyPress(e);
			if (debug) {
				if (e.getKeyCode() == KeyEvent.VK_F12) {
					openResizeDialog();
					return;
				} else if (e.getKeyCode() == KeyEvent.VK_F11) {
					if (lastF11WasRelease) {
						lastF11WasRelease = false;
						setFullscreen(!isFullscreen());
					}
					return;
				}
			}
			try {
				onKeyPressed(e);
			} catch (Throwable t) {
				throwableGenerated("onKeyPressed", t);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (debug) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_F11:
					lastF11WasRelease = true;
				case KeyEvent.VK_F12:
					return;
				default:
					break;
				}
			}
			try {
				onKeyReleased(e);
			} catch (Throwable t) {
				throwableGenerated("onKeyReleased", t);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			try {
				onMouseClicked(e);
			} catch (Throwable t) {
				throwableGenerated("onMouseClicked", t);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			try {
				onMousePressed(e);
			} catch (Throwable t) {
				throwableGenerated("onMousePressed", t);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			try {
				onMouseReleased(e);
			} catch (Throwable t) {
				throwableGenerated("onMouseReleased", t);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// no-op
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// no-op
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			try {
				onMouseDragged(e);
			} catch (Throwable t) {
				throwableGenerated("onMouseDragged", t);
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			try {
				onMouseMoved(e);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			try {
				onMouseWheelMoved(e);
			} catch (Throwable t) {
				throwableGenerated("onMouseWheelMoved", t);
			}
		}

		@Override
		public void componentResized(ComponentEvent e) {
			Dimension d = e.getComponent().getSize();
			callOnResize(d.width, d.height);
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			// no-op
		}

		@Override
		public void componentShown(ComponentEvent e) {
			// no-op
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// no-op
		}

		/*
		 * Fix X being dumb and sending keyrelease on every key repeat.
		 * Optimized+generalized version of poster Ekipur's solution at:
		 * http://bugs.sun.com/view_bug.do?bug_id=4153069
		 */

		java.util.List<RealReleaseWaiter> waiters = new ArrayList<RealReleaseWaiter>();

		private void queueKeyReleased(KeyEvent e) {
			waiters.add(new RealReleaseWaiter(e));
		}

		private void notifyKeyPress(KeyEvent e) {
			// iterate over indices for performance... I consider 3-4 millis
			// "time sensitive"
			int size = waiters.size(), code = e.getKeyCode();
			for (int i = 0; i < size; ++i) {
				RealReleaseWaiter waiter = waiters.get(i);
				if (code == waiter.code()) {
					waiter.cancel();
				}
			}
		}

		private class RealReleaseWaiter implements ActionListener {
			private boolean cancelled = false;
			private KeyEvent evt;
			private Timer t;

			public RealReleaseWaiter(KeyEvent evt) {
				this.evt = evt;
				t = new Timer(MILLIS_TO_WAIT_FOR_REPEAT, this);
				t.start();
			}

			public int code() {
				return evt.getKeyCode();
			}

			public void cancel() {
				cancelled = true;
				t.stop();
				waiters.remove(this);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				if (cancelled)
					return;
				cancel();
				keyReleased(evt);
			}

		}
	}
}
