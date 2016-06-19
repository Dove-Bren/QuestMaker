package com.skyisland.questmaker.npc;

import org.bukkit.Location;

/**
 * Describes NPCs with simple movement pattern: they occasionally attempt to
 * move back to their original spot
 * @author Skyler
 *
 */
public abstract class SimpleNPC extends NPC {
	
	private Location startingLoc;
	
	protected SimpleNPC(Location startingLoc) {
		super();
		this.startingLoc = startingLoc;
	}
	
	/**
	 * @return the startingLoc
	 */
	public Location getStartingLoc() {
		return startingLoc;
	}

	/**
	 * @param startingLoc the startingLoc to set
	 */
	public void setStartingLoc(Location startingLoc) {
		this.startingLoc = startingLoc;
	}
}
