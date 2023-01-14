package com.openrsc.server.event.custom;

import com.openrsc.server.model.Point;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.model.world.World;
import com.openrsc.server.net.rsc.ActionSender;
import com.openrsc.server.util.rsc.MessageType;

public class HourlyNpcLootEvent extends HourlyEvent  {
	private Point location;
	private int npcId;
	private int npcAmount;
	private int itemId;
	private int itemAmount;
	private int npcLifetime;
	private String eventMessage;

	public HourlyNpcLootEvent(World world, int lifeTime, Point location, int npcId, int npcAmount, int itemId) {
		this(world, lifeTime, null, location, npcId, npcAmount, itemId, 1, 10);
	}

	public HourlyNpcLootEvent(World world, int lifeTime, Point location, int npcId, int npcAmount, int itemId, int itemAmount) {
		this(world, lifeTime, null, location, npcId, npcAmount, itemId, itemAmount, 10);
	}

	public HourlyNpcLootEvent(World world, int lifeTime, String eventMessage, Point location, int npcId, int npcAmount, int itemId) {
		this(world, lifeTime, eventMessage, location, npcId, npcAmount, itemId, 1, 10);
	}

	public HourlyNpcLootEvent(World world, int lifeTime, String eventMessage, Point location, int npcId, int npcAmount, int itemId, int itemAmount) {
		this(world, lifeTime, eventMessage, location, npcId, npcAmount, itemId, itemAmount, 10);
	}

	public HourlyNpcLootEvent(World world, int lifeTime, String eventMessage, Point location, int npcId, int npcAmount, int itemId, int itemAmount, int npcLifeTime) {
		super(world, lifeTime, "Hourly NPC Loot Event");
		this.location = location;
		this.npcId = npcId;
		this.npcAmount = npcAmount;
		this.itemId = itemId;
		this.itemAmount = itemAmount;
		this.npcLifetime = npcLifeTime;
		this.eventMessage = eventMessage;
	}

	public void action() {
		getWorld().getServer().getGameEventHandler().add(new NpcLootEvent(getWorld(), getLocation(), getNpcId(), getNpcAmount(), getItemId(), getItemAmount(), getNpcLifetime()*60*1000));
		if(getEventMessage() != null) {
			for (Player p : getWorld().getPlayers())
				ActionSender.sendMessage(p, null,  MessageType.QUEST, getEventMessage(), 0, null);
		}
	}

	public Point getLocation() {
		return location;
	}

	public int getNpcId() {
		return npcId;
	}

	public int getNpcAmount() {
		return npcAmount;
	}

	public int getItemId() {
		return itemId;
	}

	private int getItemAmount() {
		return itemAmount;
	}

	public int getNpcLifetime() {
		return npcLifetime;
	}

	private String getEventMessage() {
		return eventMessage;
	}
}
