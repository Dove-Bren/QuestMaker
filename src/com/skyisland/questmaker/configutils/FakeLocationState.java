package com.skyisland.questmaker.configutils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerialization;

import com.skyisland.questmanager.configuration.utils.LocationState;

/**
 * Convenience class for saving and loading location data from config.
 * Differs from QM version in that it stores world as a string
 * @author Skyler
 *
 */
public class FakeLocationState extends LocationState {
	
	private FakeLocation location;
	
	/**
	 * Registers this class as configuration serializable with all defined 
	 * {@link aliases aliases}
	 */
	public static void registerWithAliases() {
		for (aliases alias : aliases.values()) {
			ConfigurationSerialization.registerClass(FakeLocationState.class, alias.getAlias());
		}
	}
	
	/**
	 * Registers this class as configuration serializable with only the default alias
	 */
	public static void registerWithoutAliases() {
		ConfigurationSerialization.registerClass(FakeLocationState.class);
	}
	

	private enum aliases {
		BUKKIT("org.bukkit.Location"),
		LOCATIONUPPER("LOCATION"),
		LOCATIONLOWER("location"),
		LOCATIONFORMAL("Location"),
		DEFAULT(LocationState.class.getName()),
		SIMPLE("LocationState");
		
		private String alias;
		
		aliases(String alias) {
			this.alias = alias;
		}
		
		public String getAlias() {
			return alias;
		}
	}
	
	/**
	 * Stores fields and their config keys
	 * @author Skyler
	 *
	 */
	private enum fields {
		X("x"),
		Y("y"),
		Z("z"),
		PITCH("pitch"),
		YAW("yaw"),
		WORLD("world");
		
		private String key;
		
		fields(String key) {
			this.key = key;
		}
		
		/**
		 * Returns the configuration key mapped to this field
		 */
		public String getKey() {
			return this.key;
		}
	}
	
	/**
	 * Creates a FakeLocationState with the information from the passed location.
	 */
	public FakeLocationState(FakeLocation location) {
		super(null);
		this.location = location;
	}
	
	/**
	 * Serializes the wrapped location to a format that's able to be saved to a configuration file.
	 */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> config = new HashMap<>(6);
		config.put(fields.X.getKey(), location.getX());
		config.put(fields.Y.getKey(), location.getY());
		config.put(fields.Z.getKey(), location.getZ());
		config.put(fields.PITCH.getKey(), location.getPitch());
		config.put(fields.YAW.getKey(), location.getYaw());
		config.put(fields.WORLD.getKey(), location.getWorldName());
		return config;
	}
	
	/**
	 * Uses the passed configuration map to instantiate a new location (and wrapper).
	 */
	public static FakeLocationState valueOf(Map<String, Object> configMap) {
		String world = (String) configMap.get(fields.WORLD.getKey());
		
		double x,y,z;
		float pitch, yaw;
		x = (double) configMap.get(fields.X.getKey());
		y = (double) configMap.get(fields.Y.getKey());
		z = (double) configMap.get(fields.Z.getKey());
		pitch = (float) ((double) configMap.get(fields.PITCH.getKey()));
		yaw = (float) ((double) configMap.get(fields.YAW.getKey()));
		
		return new FakeLocationState(
				new FakeLocation(
						world,
						x,
						y,
						z,
						yaw,
						pitch));
	}
	
	/**
	 * Return the location wrapped by this class
	 */
	public FakeLocation getLocation() {
		return location;
	}
}
