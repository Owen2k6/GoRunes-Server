package com.openrsc.server.plugins.authentic.npcs.taverly;

import com.openrsc.server.constants.ItemId;
import com.openrsc.server.constants.NpcId;
import com.openrsc.server.model.Shop;
import com.openrsc.server.model.container.Item;
import com.openrsc.server.model.entity.npc.Npc;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.model.world.World;
import com.openrsc.server.net.rsc.ActionSender;
import com.openrsc.server.plugins.AbstractShop;

import static com.openrsc.server.plugins.Functions.*;

public class HelemosShop extends AbstractShop {

	private final Shop shop = new Shop(false, 60000, 100, 55, 3, new Item(ItemId.DRAGON_AXE.id(), 1));

	@Override
	public boolean blockTalkNpc(final Player player, final Npc n) {
		return n.getID() == NpcId.HELEMOS.id();
	}

	@Override
	public Shop[] getShops(World world) {
		return new Shop[]{shop};
	}

	@Override
	public boolean isMembers() {
		return true;
	}

	@Override
	public Shop getShop() {
		return shop;
	}

	@Override
	public void onTalkNpc(final Player player, final Npc n) {
		npcsay(player, n, "Welcome to the hero's guild");
		final int option = multi(player, n, false, //do not send over
			"So do you sell anything here?", "So what can I do here?");
		if (option == 0) {
			say(player, n, "So do you sell anything here?");
			npcsay(player, n, "Why yes we do run an exclusive shop for our members");
			player.setAccessingShop(shop);
			ActionSender.showShop(player, shop);
		} else if (option == 1) {
			say(player, n, "so what can I do here?");
			npcsay(player, n, "Look around there are all sorts of things to keep our members entertained");
		}
	}
}
