package com.skyisland.questmaker.configutils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;

import com.skyisland.questmanager.configuration.PluginConfiguration;
import com.skyisland.questmanager.configuration.PluginConfiguration.PluginConfigurationKey;

public class PluginConfigurationWriter {

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
		YamlConfiguration yaml = new YamlConfiguration();
		
		yaml.set(PluginConfigurationKey.VERSION.getKey(), config.getVersion());
		yaml.set(PluginConfigurationKey.CONSERVATIVE.getKey(), config.getConservative());
		yaml.set(PluginConfigurationKey.VERBOSEMENUS.getKey(), config.getMenuVerbose());
		yaml.set(PluginConfigurationKey.ALLOWCRAFTING.getKey(), config.getAllowCrafting());
		yaml.set(PluginConfigurationKey.ALLOWNAMING.getKey(), config.getAllowNaming());
		yaml.set(PluginConfigurationKey.ALLOWTAMING.getKey(), config.getAllowTaming());
		yaml.set(PluginConfigurationKey.PARTYSIZE.getKey(), config.getMaxPartySize());
		yaml.set(PluginConfigurationKey.CLEANUPVILLAGERS.getKey(), config.getVillagerCleanup());
		yaml.set(PluginConfigurationKey.XPMONEY.getKey(), config.getXPMoney());
		yaml.set(PluginConfigurationKey.PORTALS.getKey(), config.getUsePortals());
		yaml.set(PluginConfigurationKey.ADJUSTXP.getKey(), config.getAdjustXP());
		yaml.set(PluginConfigurationKey.TITLECHAT.getKey(), config.getChatTitle());
		yaml.set(PluginConfigurationKey.COMPASS.getKey(), config.getCompassEnabled());
		yaml.set(PluginConfigurationKey.COMPASSTYPE.getKey(), config.getCompassType().name());
		yaml.set(PluginConfigurationKey.COMPASSNAME.getKey(), config.getCompassName());
		yaml.set(PluginConfigurationKey.ALLOWMAGIC.getKey(), config.getMagicEnabled());
		yaml.set(PluginConfigurationKey.MANADEFAULT.getKey(), config.getStartingMana());
		yaml.set(PluginConfigurationKey.DAYREGEN.getKey(), config.getMagicRegenDay());
		yaml.set(PluginConfigurationKey.NIGHTREGEN.getKey(), config.getMagicRegenNight());
		yaml.set(PluginConfigurationKey.OUTSIDEREGEN.getKey(), config.getMagicRegenOutside());
		yaml.set(PluginConfigurationKey.KILLREGEN.getKey(), config.getMagicRegenKill());
		yaml.set(PluginConfigurationKey.XPREGEN.getKey(), config.getMagicRegenXP());
		yaml.set(PluginConfigurationKey.FOODREGEN.getKey(), config.getMagicRegenFood());
		yaml.set(PluginConfigurationKey.HOLDERNAME.getKey(), config.getSpellHolderName());
		yaml.set(PluginConfigurationKey.ALLOWWEAVING.getKey(), config.getAllowSpellWeaving());
		yaml.set(PluginConfigurationKey.USEINVOKER.getKey(), config.getUseWeavingInvoker());
		yaml.set(PluginConfigurationKey.INVOKERNAME.getKey(), config.getSpellInvokerName());
		yaml.set(PluginConfigurationKey.INVOKERTYPE.getKey(), config.getInvokerType().name());
		yaml.set(PluginConfigurationKey.ALTERTYPE.getKey(), config.getAlterType().name());
		yaml.set(PluginConfigurationKey.WORLDS.getKey(), config.getWorlds());
		yaml.set(PluginConfigurationKey.QUESTDIR.getKey(), config.getQuestPath());
		yaml.set(PluginConfigurationKey.SAVEDIR.getKey(), config.getSavePath());
		yaml.set(PluginConfigurationKey.REGIONDIR.getKey(), config.getRegionPath());
		yaml.set(PluginConfigurationKey.SPELLDIR.getKey(), config.getSpellPath());
		yaml.set(PluginConfigurationKey.SKILLDIR.getKey(), config.getSkillPath());
		yaml.set(PluginConfigurationKey.SKILLCAP.getKey(), config.getSkillCap());
		yaml.set(PluginConfigurationKey.SKILLSUCCESSGROWTH.getKey(), config.getSkillGrowthOnSuccess());
		yaml.set(PluginConfigurationKey.SKILLFAILGROWTH.getKey(), config.getSkillGrowthOnFail());
		yaml.set(PluginConfigurationKey.SKILLGROWTHCUTOFF.getKey(), config.getSkillCutoff());
		yaml.set(PluginConfigurationKey.SKILLGROWTHUPPERCUTOFF.getKey(), config.getSkillUpperCutoff());
		yaml.set(PluginConfigurationKey.SUMMONLIMIT.getKey(), config.getSummonLimit());
		
		//yaml.set(PluginConfigurationKey.MUSICDURATIONS.getKey(), config.getMusicDurations());
		Map<Sound, Double> durations = config.getMusicDurations();
		String cachekey = PluginConfigurationKey.MUSICDURATIONS.getKey();
		for (Sound key : durations.keySet()) {
			yaml.set(cachekey + "." + key.name(), durations.get(key));
		}
		
		yaml.save(saveFile);
	}
	
}
