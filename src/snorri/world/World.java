package snorri.world;

import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import snorri.entities.Collider;
import snorri.entities.Entity;
import snorri.entities.EntityGroup;
import snorri.entities.Player;
import snorri.events.CollisionEvent;
import snorri.main.FocusedWindow;
import snorri.main.Main;

public class World implements Playable {

	private Level level;
	private EntityGroup col;
	private List<Collider> colliders;
	private Queue<Entity> deleteQ;
	private Queue<Entity> addQ;

	public World() {
		
		Main.log("creating new world..");
		level = new Level(300, 300); // TODO: pass a level file to read
		col = new EntityGroup();
		colliders = new ArrayList<Collider>();
		deleteQ = new LinkedList<Entity>();
		addQ = new LinkedList<Entity>();
		
		//temporary
		addHard(new Player(new Vector(100, 100)));
		
		Main.log("new world created!");
	}

	public World(String folderName) throws FileNotFoundException, IOException {
		this(new File(folderName));
	}

	public World(File file) throws FileNotFoundException, IOException {
		load(file);
		colliders = new ArrayList<Collider>();
		deleteQ = new LinkedList<Entity>();
		addQ = new LinkedList<Entity>();
	}

	public void update(float f) {

		col.update(this, f);

		for (Collider p : colliders) {

			p.update(this, f);

			for (Entity hit : col.getAllCollisions(p)) {
				if (hit != null) {
					p.onCollision(new CollisionEvent(p, hit, this));
				}
			}

		}

		while (!deleteQ.isEmpty()) {
			deleteHard(deleteQ.poll());
		}
		
		while (!addQ.isEmpty()) {
			addHard(addQ.poll());
		}

	}

	public void render(FocusedWindow g, Graphics gr, boolean showOutlands) {

		// TODO: draw grid
		// TODO: render, not render hitboxes
		//level.renderMap(g,gr,levelMap);
		level.renderMap(g, gr, showOutlands);
		col.renderAround(g, gr);

		for (Collider p : colliders) {
			p.renderHitbox(g, gr);
		}

	}

	public EntityGroup getEntityTree() {
		return col;
	}

	public Level getLevel() {
		return level;
	}

	/**
	 * Add an Entity to the World. Detects whether Entity is a Collider or
	 * otherwise, and handles it appropriately
	 * 
	 */
	public void add(Entity e) {
		addQ.add(e);
	}
	
	public void addHard(Entity e) {

		if (e instanceof Collider) {
			colliders.add((Collider) e);
			return;
		}

		col.insert(e);

	}

	/**
	 * Use deleteSoft method in update deleteHard is a bit faster, and can be
	 * used in CollisionEvents and other contexts
	 * 
	 * @param e
	 *            the entity to delete
	 */
	public void delete(Entity e) {
		deleteQ.add(e);
	}

	/**
	 * Use deleteSoft method in update deleteHard is a bit faster, and can be
	 * used in CollisionEvents and other contexts
	 * 
	 * @param e
	 *            the entity to delete
	 */
	private boolean deleteHard(Entity e) {
		
		if (e instanceof Collider) {
			return colliders.remove(e);
		}

		return col.delete(e);

	}

	@Override
	public void save(File f) throws IOException {
		
		if (f.exists() && !f.isDirectory()) {
			Main.error("tried to save world " + f.getName() + " to non-directory");
			throw new IOException();
		}

		if (!f.exists()) {
			Main.log("creating new world directory...");
			f.mkdir();
		}
		
		String path = f.getPath();
		col.saveEntities(new File(path, "entities.dat"));
		level.save(new File(path, "level.dat"));

	}

	@Override
	public void load(File f) throws FileNotFoundException, IOException {
		
		if (!f.exists()) {
			Main.error("could not find world " + f.getName());
			throw new FileNotFoundException();
		}

		if (!f.isDirectory()) {
			Main.error("world file " + f.getName() + " is not a directory");
			throw new IOException();
		}

		col = new EntityGroup(new File(f, "entities.dat"));
		level = new Level(new File(f, "level.dat"));

	}

	@Override
	public Player getFocus() {
		for (Entity e : col.getAllEntities()) {
			if (e instanceof Player) {
				return (Player) e;
			}
		}
		return null;
	}

	@Override
	public World getCurrentWorld() {
		return this;
	}

}
