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
import com.skyisland.questmanager.ui.menu.message.Message;

/**
 * NPC that starts a quest :D
 * This simple starting version mounts atop a {@link SimpleBioptionNPC}, and has all the capability
 * and limits defined therein.
 * @author Skyler
 *
 */
public class SimpleQuestStartNPC extends SimpleStaticBioptionNPC {
	
	/**
	 * Registers this class as configuration serializable with all defined 
	 * {@link aliases aliases}
	 */
	public static void registerWithAliases() {
		for (aliases alias : aliases.values()) {
			ConfigurationSerialization.registerClass(SimpleQuestStartNPC.class, alias.getAlias());
		}
	}
	
	/**
	 * Registers this class as configuration serializable with only the default alias
	 */
	public static void registerWithoutAliases() {
		ConfigurationSerialization.registerClass(SimpleQuestStartNPC.class);
	}
	

	private enum aliases {
		FULL("com.SkyIsland.QuestManager.NPC.SimpleQuestStartNPC"),
		DEFAULT(SimpleQuestStartNPC.class.getName()),
		SHORT("SimpleQuestStartNPC"),
		INFORMAL("QSNPC");
		
		private String alias;
		
		aliases(String alias) {
			this.alias = alias;
		}
		
		public String getAlias() {
			return alias;
		}
	}

	
	private SimpleQuestStartNPC(Location startingLoc) {
		super(startingLoc);
	}
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<>(4);
		System.out.println("point 1");
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
		
		map.put("firstmessage", chat);
		map.put("duringmessage", duringMessage);
		map.put("postmessage", afterMessage);
		map.put("badrequirementmessage", altMessage);
				
		return map;
	}
	
	public static SimpleQuestStartNPC valueOf(Map<String, Object> map) {
		if (map == null || !map.containsKey("name") || !map.containsKey("type") 
				 || !map.containsKey("location") || !map.containsKey("equipment")
				  || !map.containsKey("firstmessage") || !map.containsKey("duringmessage")
				  || !map.containsKey("postmessage") || !map.containsKey("badrequirementmessage")) {
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
			e.printStackTrace();
		}
		
		LocationState ls = (LocationState) map.get("location");
		Location loc = ls.getLocation();
		
		EntityType type = EntityType.valueOf((String) map.get("type"));
		
		
		SimpleQuestStartNPC npc = new SimpleQuestStartNPC(loc);
		
		npc.name = (String) map.get("name");
		
		//load the chunk
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
		
		npc.chat = (BioptionMessage) map.get("firstmessage");
		npc.duringMessage = (Message) map.get("duringmessage");
		npc.afterMessage = (Message) map.get("postmessage");
		npc.altMessage = (Message) map.get("badrequirementmessage");
		
		
		//provide our npc's name, unless we don't have one!
		if (npc.name != null && !npc.name.equals("")) {
			FancyMessage label = new FancyMessage(npc.name);
			npc.chat.setSourceLabel(label);
			npc.duringMessage.setSourceLabel(label);
			npc.afterMessage.setSourceLabel(label);
			npc.altMessage.setSourceLabel(label);
		}
		
		return npc;
	}
	
	private Message duringMessage;
	
	private Message afterMessage;
	
	private Message finishMessage;
	
	private Message altMessage;
	
	public void markAsEnd(Message finishMessage) {
		this.finishMessage = finishMessage;
		
		if (name != null && !name.equals("")) {
			this.finishMessage.setSourceLabel(
					new FancyMessage(name));
		}
	}
}
