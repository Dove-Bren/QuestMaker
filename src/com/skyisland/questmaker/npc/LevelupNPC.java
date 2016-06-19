package com.skyisland.questmaker.npc;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

import com.skyisland.questmaker.configutils.FakeEntity;
import com.skyisland.questmanager.QuestManagerPlugin;
import com.skyisland.questmanager.configuration.EquipmentConfiguration;
import com.skyisland.questmanager.configuration.utils.LocationState;
import com.skyisland.questmanager.fanciful.FancyMessage;
import com.skyisland.questmanager.ui.menu.message.BioptionMessage;

/**
 * Prompts the player with some amount of text and gives them the option to level up.
 * The amount given in the level up's is also determined in the configuration, including options
 * for rate and base amounts.
 * If both the base and rate amounts for any attribute, the option to increase it will not be included.
 * @author Skyler
 *
 */
public class LevelupNPC extends SimpleNPC {

	/**
	 * Registers this class as configuration serializable with all defined 
	 * {@link aliases aliases}
	 */
	public static void registerWithAliases() {
		for (aliases alias : aliases.values()) {
			ConfigurationSerialization.registerClass(LevelupNPC.class, alias.getAlias());
		}
	}
	
	/**
	 * Registers this class as configuration serializable with only the default alias
	 */
	public static void registerWithoutAliases() {
		ConfigurationSerialization.registerClass(LevelupNPC.class);
	}
	

	private enum aliases {
		FULL("com.SkyIsland.QuestManager.NPC.SimpleBioptionNPCC"),
		DEFAULT(LevelupNPC.class.getName()),
		SHORT("LevelupNPC"),
		INFORMAL("LUNPC");
		
		private String alias;
		
		aliases(String alias) {
			this.alias = alias;
		}
		
		public String getAlias() {
			return alias;
		}
	}
	
	protected BioptionMessage chat;
	
	protected double hpRate;
	
	protected int hpBase;
	
	protected double mpRate;
	
	protected int mpBase;
	
	protected double fameRate;
	
	protected int fameBase;
	
	protected LevelupNPC(Location startingLoc) {
		super(startingLoc);
	}
		
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>(4);
		
		map.put("name", name);
		map.put("type", getEntity().getType());
		map.put("location", new LocationState(getEntity().getLocation()));
		
		EquipmentConfiguration econ;
		
		if (getEntity() instanceof LivingEntity) {
			econ = new EquipmentConfiguration(
					((LivingEntity) getEntity()).getEquipment()
					);
		} else {
			econ = new EquipmentConfiguration();
		}
		
		map.put("equipment", econ);
		
		map.put("message", chat);
		
		map.put("hpbase", hpBase);
		map.put("hprate", hpRate);
		map.put("mpbase", mpBase);
		map.put("mprate", mpRate);	
		map.put("famebase", fameBase);
		map.put("famerate", fameRate);
		
		return map;
	}
	
	public static LevelupNPC valueOf(Map<String, Object> map) {
		if (map == null || !map.containsKey("name") || !map.containsKey("type") 
				 || !map.containsKey("location") || !map.containsKey("equipment")
				  || !map.containsKey("message") || !map.containsKey("famerate") 
				  || !map.containsKey("famebase")) {
			QuestManagerPlugin.logger.warning("Invalid NPC info! "
					+ (map.containsKey("name") ? ": " + map.get("name") : ""));
			return null;
		}
		
		
		EquipmentConfiguration econ = new EquipmentConfiguration();
		try {
			YamlConfiguration tmp = new YamlConfiguration();
			tmp.createSection("key",  (Map<?, ?>) map.get("equipment"));
			econ.load(tmp.getConfigurationSection("key"));
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LocationState ls = (LocationState) map.get("location");
		Location loc = ls.getLocation();
		

		LevelupNPC npc = new LevelupNPC(loc);
		EntityType type = EntityType.valueOf((String) map.get("type"));
		
		npc.name = (String) map.get("name");
		

		;
		npc.setEntity(new FakeEntity(loc, type));
		npc.getEntity().setCustomName((String) map.get("name"));

		if (npc.getEntity() instanceof LivingEntity) {
			EntityEquipment equipment = ((LivingEntity) npc.getEntity()).getEquipment();
			equipment.setHelmet(econ.getHead());
			equipment.setChestplate(econ.getChest());
			equipment.setLeggings(econ.getLegs());
			equipment.setBoots(econ.getBoots());
			equipment.setItemInMainHand(econ.getHeldMain());
			equipment.setItemInOffHand(econ.getHeldOff());
			
		}
		
		npc.chat = (BioptionMessage) map.get("message");
		
		//provide our npc's name, unless we don't have one!
		if (npc.name != null && !npc.name.equals("")) {
			npc.chat.setSourceLabel(
					new FancyMessage(npc.name));
			
		}
		
		npc.fameBase = (int) map.get("famebase");
		npc.fameRate = (double) map.get("famerate");
		
		npc.hpBase = npc.mpBase = 0;
		npc.hpRate = npc.mpRate = 0.0;
		
		if (map.containsKey("hpbase")) {
			npc.hpBase = (int) map.get("hpbase");
		}
		if (map.containsKey("hprate")) {
			npc.hpRate = (double) map.get("hprate");
		}
		if (map.containsKey("mpbase")) {
			npc.mpBase = (int) map.get("mpbase");
		}
		if (map.containsKey("mprate")) {
			npc.mpRate = (double) map.get("mprate");
		}
		
		return npc;
	}
}