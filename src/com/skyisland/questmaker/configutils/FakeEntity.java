package com.skyisland.questmaker.configutils;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EntityEquipment;

public class FakeEntity {

	private String name;
	
	private double maxHp;
	
	private double hp;
	
	private EntityType type;
	
	private EntityEquipment equipment;
	
	private FakeLocation location;
	
	public FakeEntity(FakeLocation location, EntityType type) {
		this.location = location;
		this.type = type;
	}
	
	public FakeEntity(Location location, EntityType type) {
		this.type = type;
		if (location instanceof FakeLocation) {
			this.location = (FakeLocation) location;
			return;
		}
		
		this.location = new FakeLocation("", location.getX(), location.getY(), location.getZ(),
				location.getPitch(), location.getYaw());
		
	}
	
	public Location getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}
	
	public void setCustomName(String name) {
		this.name = name;
	}

	public double getMaxHp() {
		return maxHp;
	}

	public void setMaxHp(double maxHp) {
		this.maxHp = maxHp;
	}

	public double getHp() {
		return hp;
	}

	public void setHp(double hp) {
		this.hp = hp;
	}

	public EntityType getType() {
		return type;
	}

	public void setType(EntityType type) {
		this.type = type;
	}

	public EntityEquipment getEquipment() {
		return equipment;
	}

	public void setEquipment(EntityEquipment equipment) {
		this.equipment = equipment;
	}	
}
