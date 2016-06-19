package com.skyisland.questmaker.configutils;

import org.bukkit.Location;

/**
 * Super secret class that is like a regular Location, except it stores world info as a string!
 * @author Skyler
 *
 */
public class FakeLocation extends Location {

	private String worldName;

	public FakeLocation(String world, double x, double y, double z, float pitch, float yaw) {
		super(null, x, y, z, pitch, yaw);
		this.worldName = world;
	}

	public void setWorldName(String world) {
		this.worldName = world;
	}
	
	public String getWorldName() {
		return worldName;
	}

}
