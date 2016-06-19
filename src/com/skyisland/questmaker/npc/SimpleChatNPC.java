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
import com.skyisland.questmanager.ui.menu.message.Message;
import com.skyisland.questmanager.ui.menu.message.SimpleMessage;

/**
 * NPC that offers a simple message to those that interact with it.
 * SimpleChatNPCs do <b>not</b> support menus (they don't do anything if you click chat menu
 * buttons) 
 * @author Skyler
 *
 */
public class SimpleChatNPC extends SimpleNPC {

	/**
	 * Registers this class as configuration serializable with all defined 
	 * {@link aliases aliases}
	 */
	public static void registerWithAliases() {
		for (aliases alias : aliases.values()) {
			ConfigurationSerialization.registerClass(SimpleChatNPC.class, alias.getAlias());
		}
	}
	
	/**
	 * Registers this class as configuration serializable with only the default alias
	 */
	public static void registerWithoutAliases() {
		ConfigurationSerialization.registerClass(SimpleChatNPC.class);
	}
	

	private enum aliases {
		FULL("com.SkyIsland.QuestManager.NPC.SimpleChatNPC"),
		DEFAULT(SimpleChatNPC.class.getName()),
		SHORT("SimpleChatNPC"),
		INFORMAL("SCNPC");
		
		private String alias;
		
		aliases(String alias) {
			this.alias = alias;
		}
		
		public String getAlias() {
			return alias;
		}
	}
	
	private Message chat;
	
	private SimpleChatNPC(Location startingLoc) {
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
	
	public static SimpleChatNPC valueOf(Map<String, Object> map) {
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
		
		EntityType type = EntityType.valueOf((String) map.get("type"));
		
		
		SimpleChatNPC npc = new SimpleChatNPC(loc);

		;
		npc.setEntity(new FakeEntity(loc, type));
		npc.setStartingLoc(loc);
		npc.name = (String) map.get("name");
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
		
		//UPDATE: We wanna also accept regular strings, too :P
		Object msgObj = map.get("message");
		if (msgObj instanceof Message) {
			npc.chat = (Message) map.get("message");
		} else if (msgObj instanceof FancyMessage) {
			npc.chat = new SimpleMessage((FancyMessage) msgObj);
		} else if (msgObj instanceof String){
			npc.chat = new SimpleMessage((String) msgObj);
		} else {
			QuestManagerPlugin.logger.warning(
					"Invalid message type for Simple Chat NPC: " + npc.name);
		}
		
		if (npc.chat != null || npc.name != null || !npc.name.trim().isEmpty()) {
			npc.chat.setSourceLabel(new FancyMessage(npc.name));
		}
		
		return npc;
	}
}
