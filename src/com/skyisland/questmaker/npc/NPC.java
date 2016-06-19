package com.skyisland.questmaker.npc;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import com.skyisland.questmaker.configutils.FakeEntity;

public abstract class NPC implements ConfigurationSerializable {
	
	/**
	 * Cache value for saving lookup times for entities
	 */
	private FakeEntity entity;
	
	protected String name;
	
	/**
	 * Which quests is this NPC associated? Meaning which quest's history should include this npc's dialogue?
	 */
	protected String questName;
	
	protected NPC() {
		;
	}
	
	/**
	 * Returns the entity this NPC is attached to.
	 * This method attempts to save cycles by caching the last known entity to
	 * represent our UUID'd creature. If the cache is no longer valid, an entire
	 * sweep of worlds and entities is performed to lookup the entity.
	 * @return The entity attached to our UUID, or NULL if none is found
	 */
	public FakeEntity getEntity() {
		return entity;
	}
	
	/**
	 * Register an entity to this NPC. This method also updates the ID of this npc
	 */
	public void setEntity(FakeEntity entity) {
		this.entity = entity;
	}
	
	public String getName() {
		return name;
	}
	
	public String getQuestName() {
		return questName;
	}
	
	public void setQuestName(String questName) {
		this.questName = questName;
	}
	
	
}
