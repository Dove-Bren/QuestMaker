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
 * Provides a simple two-option menu when interacted with. This NPC also supports simple response
 * menus in response to each option.
 * This NPC does <b>not</b> support performing actions based on the response.
 * @author Skyler
 *
 */
public class SimpleBioptionNPC extends SimpleNPC {

	/**
	 * Registers this class as configuration serializable with all defined 
	 * {@link aliases aliases}
	 */
	public static void registerWithAliases() {
		for (aliases alias : aliases.values()) {
			ConfigurationSerialization.registerClass(SimpleBioptionNPC.class, alias.getAlias());
		}
	}
	
	/**
	 * Registers this class as configuration serializable with only the default alias
	 */
	public static void registerWithoutAliases() {
		ConfigurationSerialization.registerClass(SimpleBioptionNPC.class);
	}
	

	private enum aliases {
		FULL("com.SkyIsland.QuestManager.NPC.SimpleBioptionNPCC"),
		DEFAULT(SimpleBioptionNPC.class.getName()),
		SHORT("SimpleBioptionNPC"),
		INFORMAL("SBINPC");
		
		private String alias;
		
		aliases(String alias) {
			this.alias = alias;
		}
		
		public String getAlias() {
			return alias;
		}
	}
	
	protected BioptionMessage chat;
	
	protected SimpleBioptionNPC(Location startingLoc) {
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
	
		
		return map;
	}
	
	public static SimpleBioptionNPC valueOf(Map<String, Object> map) {
		if (map == null || !map.containsKey("name") || !map.containsKey("type") 
				 || !map.containsKey("location") || !map.containsKey("equipment")
				  || !map.containsKey("message")) {
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
		

		SimpleBioptionNPC npc = new SimpleBioptionNPC(loc);
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
		
		return npc;
	}
}