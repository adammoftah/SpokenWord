package snorri.main;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import snorri.world.Vector;

public abstract class GamePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final int FRAME_DELTA = 30;

	protected void startAnimation() {
		
		SwingWorker<Object, Object> sw = new SwingWorker<Object, Object>() {
			@Override
			protected Object doInBackground() throws Exception {
				while (true) {
					onFrame();
					Thread.sleep(FRAME_DELTA);
				}
			}
		};

		sw.execute();
	}
	
	protected JButton createButton(String text) {
		JButton button = new JButton(text);
		add(button);
		button.setSelected(false);
		button.addActionListener(this);
		return button;
	}

	public Vector getDimensions() {
		return new Vector(getBounds());
	}
	
	protected void onFrame() {
		// TODO Auto-generated method stub

	}

	// returns nanosecond-accurate time
	protected long getTimestamp() {
		return System.nanoTime();
	}

}
