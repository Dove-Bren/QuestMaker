package com.skyisland.questmaker.npc;

import org.bukkit.Location;

/**
 * An npc that doesn't move rather then being reset
 * @author Skyler
 *
 */
public abstract class SimpleStaticBioptionNPC extends SimpleBioptionNPC {

	protected SimpleStaticBioptionNPC(Location startingLoc) {
		super(startingLoc);
	}
}
