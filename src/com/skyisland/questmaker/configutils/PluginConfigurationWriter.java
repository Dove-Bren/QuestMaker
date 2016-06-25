package com.skyisland.questmaker.configutils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.skyisland.questmanager.configuration.PluginConfiguration;
import com.skyisland.questmanager.configuration.PluginConfiguration.PluginConfigurationKey;
import com.skyisland.questmanager.configuration.utils.YamlWriter;

public final class PluginConfigurationWriter {

	public static final int LINE_LENGTH = 50;
	
	/**
	 * Saves the given Configuration to the provided File.
	 * <p>
	 * This class breaks on updates to PluginConfiguration, but has abstracted all the breaky
	 * bits to here for ease of update
	 * </p>
	 * @param config
	 * @param saveFile
	 * @throws IOException 
	 */
	public static void saveConfig(PluginConfiguration config, File saveFile) throws IOException {
		YamlWriter writer = new YamlWriter();
		
		add(writer, PluginConfigurationKey.VERSION, config.getVersion());
		add(writer, PluginConfigurationKey.CONSERVATIVE, config.getConservative());
		add(writer, PluginConfigurationKey.VERBOSEMENUS, config.getMenuVerbose());
		add(writer, PluginConfigurationKey.ALLOWCRAFTING, config.getAllowCrafting());
		add(writer, PluginConfigurationKey.ALLOWNAMING, config.getAllowNaming());
		add(writer, PluginConfigurationKey.ALLOWTAMING, config.getAllowTaming());
		add(writer, PluginConfigurationKey.PARTYSIZE, config.getMaxPartySize());
		add(writer, PluginConfigurationKey.CLEANUPVILLAGERS, config.getVillagerCleanup());
		add(writer, PluginConfigurationKey.XPMONEY, config.getXPMoney());
		add(writer, PluginConfigurationKey.PORTALS, config.getUsePortals());
		add(writer, PluginConfigurationKey.ADJUSTXP, config.getAdjustXP());
		add(writer, PluginConfigurationKey.TITLECHAT, config.getChatTitle());
		add(writer, PluginConfigurationKey.COMPASS, config.getCompassEnabled());
		add(writer, PluginConfigurationKey.COMPASSTYPE, config.getCompassType().name());
		add(writer, PluginConfigurationKey.COMPASSNAME, config.getCompassName());
		add(writer, PluginConfigurationKey.ALLOWMAGIC, config.getMagicEnabled());
		add(writer, PluginConfigurationKey.MANADEFAULT, config.getStartingMana());
		add(writer, PluginConfigurationKey.DAYREGEN, config.getMagicRegenDay());
		add(writer, PluginConfigurationKey.NIGHTREGEN, config.getMagicRegenNight());
		add(writer, PluginConfigurationKey.OUTSIDEREGEN, config.getMagicRegenOutside());
		add(writer, PluginConfigurationKey.KILLREGEN, config.getMagicRegenKill());
		add(writer, PluginConfigurationKey.XPREGEN, config.getMagicRegenXP());
		add(writer, PluginConfigurationKey.FOODREGEN, config.getMagicRegenFood());
		add(writer, PluginConfigurationKey.HOLDERNAME, config.getSpellHolderName());
		add(writer, PluginConfigurationKey.ALLOWWEAVING, config.getAllowSpellWeaving());
		add(writer, PluginConfigurationKey.USEINVOKER, config.getUseWeavingInvoker());
		add(writer, PluginConfigurationKey.INVOKERNAME, config.getSpellInvokerName());
		add(writer, PluginConfigurationKey.INVOKERTYPE, config.getInvokerType().name());
		add(writer, PluginConfigurationKey.ALTERTYPE, config.getAlterType().name());
		add(writer, PluginConfigurationKey.WORLDS, config.getWorlds());
		add(writer, PluginConfigurationKey.QUESTDIR, config.getQuestPath());
		add(writer, PluginConfigurationKey.SAVEDIR, config.getSavePath());
		add(writer, PluginConfigurationKey.REGIONDIR, config.getRegionPath());
		add(writer, PluginConfigurationKey.SPELLDIR, config.getSpellPath());
		add(writer, PluginConfigurationKey.SKILLDIR, config.getSkillPath());
		add(writer, PluginConfigurationKey.SKILLCAP, config.getSkillCap());
		add(writer, PluginConfigurationKey.SKILLSUCCESSGROWTH, config.getSkillGrowthOnSuccess());
		add(writer, PluginConfigurationKey.SKILLFAILGROWTH, config.getSkillGrowthOnFail());
		add(writer, PluginConfigurationKey.SKILLGROWTHCUTOFF, config.getSkillCutoff());
		add(writer, PluginConfigurationKey.SKILLGROWTHUPPERCUTOFF, config.getSkillUpperCutoff());
		add(writer, PluginConfigurationKey.SUMMONLIMIT, config.getSummonLimit());
		
		add(writer, PluginConfigurationKey.MUSICDURATIONS, config.getMusicDurations());
//		Map<Sound, Double> durations = config.getMusicDurations();
//		String cachekey = PluginConfigurationKey.MUSICDURATIONS;
//		for (Sound key : durations.keySet()) {
//			yaml.set(cachekey + "." + key.name(), durations.get(key));
//		}
		
		try {
			writer.save(saveFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void add(YamlWriter writer, PluginConfigurationKey key, Object value) {
		writer.addLine(key.getKey(), value, makeList(key.getDescription()));
	}
	
	private static List<String> makeList(String desc) {
		List<String> descList = new LinkedList<>();
		
		String mid;
		int pos;
		while (desc.length() > 30) {
			
			desc = desc.trim();
			
			//find first space before 30
			mid = desc.substring(0, 30);
			pos = mid.lastIndexOf(" ");
			if (pos == -1) {
				descList.add(mid);
				desc = desc.substring(30);
				continue;
			}
			//else we found a space
			descList.add(mid.substring(0, pos));
			desc = desc.substring(pos);
		}
		
		descList.add(desc.trim());
		
		return descList;
	}
	
}
