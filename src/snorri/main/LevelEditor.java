package snorri.main;

import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import snorri.entities.Entity;
import snorri.world.Vector;
import snorri.world.World;

public class LevelEditor extends FocusedWindow {
	
	private static final long serialVersionUID = 1L;

	private World world;
	private Entity focus;
	private boolean openingFile = false;
		
	public LevelEditor() {
		super();
		createButton("New");
		createButton("Load");
		createButton("Save");
		focus = new Entity(new Vector(50, 50));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		switch (e.getActionCommand()) {
		case "New":
			world = new World();
			break;
		case "Load":
			openingFile = true;
			File file = Main.getFileDialog("Select file to load", FileDialog.LOAD);
			try {
				if (file != null)
					world = new World(file);
			}
			catch (IOException er) {
				Main.error("error opening world " + file.getName());
				er.printStackTrace();
			}
			openingFile = false;
			break;
		case "Save":
			
			if (world == null) {
				return;
			}
			
			openingFile = true;
			File f = Main.getFileDialog("Select save destination", FileDialog.SAVE);
			Main.log(f);
			try {
				if (f != null)
					world.save(f);
			}
			catch (IOException er) {
				Main.error("error saving world " + f.getName());
				er.printStackTrace();
			}
			openingFile = false;
			
		}
		
		repaint();
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if (world == null) {
			return;
		}
		
		world.render(this, g, false);
		
	}
	
	@Override
	protected void onFrame() {
				
		if (world != null) {
			focus.getPos().add(states.getMovementVector().scale(10));
		}
		
		repaint();
		if (!openingFile)
			requestFocus();
		
	}

	@Override
	public Entity getFocus() {
		return focus;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	//TOBY do stuff here!
}
