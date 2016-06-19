package com.skyisland.questmaker.npc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

import com.avaje.ebean.enhance.asm.commons.Method;
import com.skyisland.questmaker.configutils.FakeEntity;
import com.skyisland.questmanager.QuestManagerPlugin;
import com.skyisland.questmanager.configuration.EquipmentConfiguration;
import com.skyisland.questmanager.configuration.utils.LocationState;
import com.skyisland.questmanager.fanciful.FancyMessage;
import com.skyisland.questmanager.ui.menu.message.BioptionMessage;
import com.skyisland.questmanager.ui.menu.message.Message;

/**
 * NPC which offers to take a player and move them, for a fee?
 * @author Skyler
 *
 */
public class TeleportNPC extends SimpleStaticBioptionNPC {
	
	/**
	 * Registers this class as configuration serializable with all defined 
	 * {@link aliases aliases}
	 */
	public static void registerWithAliases() {
		for (aliases alias : aliases.values()) {
			ConfigurationSerialization.registerClass(TeleportNPC.class, alias.getAlias());
		}
	}
	
	/**
	 * Registers this class as configuration serializable with only the default alias
	 */
	public static void registerWithoutAliases() {
		ConfigurationSerialization.registerClass(TeleportNPC.class);
	}
	

	private enum aliases {
		FULL("com.SkyIsland.QuestManager.NPC.TeleportNPC"),
		DEFAULT(TeleportNPC.class.getName()),
		SHORT("TeleportNPC"),
		ALT("FerryNPC"),
		INFORMAL("TPNPC");
		
		private String alias;
		
		aliases(String alias) {
			this.alias = alias;
		}
		
		public String getAlias() {
			return alias;
		}
	}

	
	private TeleportNPC(Location startingLoc) {
		super(startingLoc);
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>(4);
		
		map.put("name", name);
		map.put("type", getEntity().getType());
		map.put("cost", cost);
		map.put("destination", new LocationState(destination));
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
		
		map.put("badrequirementmessage", altMessage);
		
		map.put("requiredquests", requirements);
	
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static TeleportNPC valueOf(Map<String, Object> map) {
		if (map == null || !map.containsKey("name") || !map.containsKey("type") 
				 || !map.containsKey("location") || !map.containsKey("equipment")
				  || !map.containsKey("message") || !map.containsKey("cost") 
				|| !map.containsKey("destination") || !map.containsKey("requiredquests")
				|| !map.containsKey("badrequirementmessage")) {
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
		
		EntityType type = EntityType.valueOf((String) map.get("type"));
		
		TeleportNPC npc = new TeleportNPC(loc);
		
		npc.name = (String) map.get("name");
		
		npc.cost = (int) map.get("cost");
		
		npc.destination = ((LocationState) map.get("destination")).getLocation();
		

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
		npc.altMessage = (Message) map.get("badrequirementmessage");
		npc.requirements = (List<String>) map.get("requiredquests");
		
		//provide our npc's name, unless we don't have one!
		if (npc.name != null && !npc.name.equals("")) {
			FancyMessage label = new FancyMessage(npc.name);
			npc.chat.setSourceLabel(label);			
			npc.altMessage.setSourceLabel(label);
		}
		
//		NEED TO DECIDE TO TAKE OVER MESSAGES SUBDIRECTORY OR NOT. IT'S JUST ONE... RIGHT? WHY NOT?
//		OR SHOULD I STOP BRINGING OVER CRAP LIKE NPC AND MESSAGE? AFTER ALL, THEY DON'T EVEN PROPERLY
//		IMPLEMENT THE SERIALIZE Method. tHAT'S AT LEAST THE CASE FOR NPCS
		
		return npc;
	}
		
	private int cost;
	
	private Location destination;
	
	private Message altMessage;
	
	private List<String> requirements;
}
