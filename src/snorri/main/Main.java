package snorri.main;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import snorri.parser.Lexicon;
import snorri.world.World;

public class Main {

	private static GamePanel window;
	private static JFrame frame;

	public static void main(String[] args) {

		Lexicon.init();
		
		System.setProperty("apple.awt.fileDialogForDirectories", "true");
		System.setProperty("windows.awt.fileDialogForDirectories", "true");
		
		frame = new JFrame("Spoken Word");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1800, 900);
		frame.setVisible(true);

		// FOR FULL SCREEN: frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		window = new MainMenu();
		frame.getContentPane().add(window, BorderLayout.CENTER);
		frame.getContentPane().validate();
	}

	public static Rectangle getBounds() {
		return frame.getBounds();
	}

	public static void error(Object o) {
		System.out.println("[ERROR] " + o);
	}

	public static void log(String s) {
		System.out.println("[LOG] " + s);
		// System.out.println("[" + Colors.LOG + "LOG" + Colors.RESET + "] " +
		// s);
	}

	public static void log(Object o) {
		System.out.println("[RAW] " + o);
	}

	public static GamePanel getWindow() {
		return window;
	}

	public static Image getImageResource(String path) {
		try {
			return ImageIO.read(Main.class.getResource(path));
		} catch (IllegalArgumentException e) {
			Main.error("unable to find image " + path);
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	public static File getFileDialog(String msg, int flag) {
		FileDialog fd = new FileDialog(frame, msg, flag);
		fd.setVisible(true);
		
		if (fd.getFile() == null) {
			return null;
		}
		
		File f = new File(fd.getDirectory(), fd.getFile());
		if (f.exists() && ! f.isDirectory()) {
			return new File(fd.getDirectory());
		}
		
		return f;
	}

	public static void launchGame() {

		frame.getContentPane().remove(window);

		World world = new World();

		window = new GameWindow(world);
		
		Main.log(((GameWindow) window).getFocus());
		
		frame.getContentPane().add(window, BorderLayout.CENTER);
		frame.getContentPane().revalidate();
		window.requestFocus();

	}

	public static void launchEditor() {

		frame.getContentPane().remove(window);
		window = new LevelEditor();
		frame.getContentPane().add(window, BorderLayout.CENTER);
		frame.getContentPane().revalidate();
		window.requestFocus();

	}
	
	public static JFrame getFrame() {
		return frame;
	}

}
