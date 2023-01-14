package com.openrsc.server.plugins.authentic.npcs.grandtree;

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

public final class Gulluck extends AbstractShop {

	private final Shop shop = new Shop(false, 3000, 100, 25, 1, new Item(ItemId.BRONZE_ARROWS.id(),
		200), new Item(ItemId.CROSSBOW_BOLTS.id(), 150), new Item(ItemId.OYSTER_PEARL_BOLTS.id(), 1), new Item(ItemId.SHORTBOW.id(),
		4), new Item(ItemId.LONGBOW.id(), 2), new Item(ItemId.CROSSBOW.id(), 2), new Item(ItemId.BRONZE_ARROW_HEADS.id(), 200),
		new Item(ItemId.IRON_ARROW_HEADS.id(), 180), new Item(ItemId.STEEL_ARROW_HEADS.id(), 160),
		new Item(ItemId.MITHRIL_ARROW_HEADS.id(), 140), new Item(ItemId.IRON_AXE.id(), 5), new Item(ItemId.STEEL_AXE.id(), 3),
		new Item(ItemId.IRON_BATTLE_AXE.id(), 5), new Item(ItemId.STEEL_BATTLE_AXE.id(), 2), new Item(ItemId.MITHRIL_BATTLE_AXE.id(), 1),
		new Item(ItemId.BRONZE_2_HANDED_SWORD.id(), 4), new Item(ItemId.IRON_2_HANDED_SWORD.id(), 3), new Item(ItemId.STEEL_2_HANDED_SWORD.id(), 2),
		new Item(ItemId.BLACK_2_HANDED_SWORD.id(), 1), new Item(ItemId.MITHRIL_2_HANDED_SWORD.id(), 1), new Item(ItemId.ADAMANTITE_2_HANDED_SWORD.id(), 1));

	@Override
	public void onTalkNpc(Player player, final Npc n) {
		if (n.getID() == NpcId.GULLUCK.id()) {
			say(player, n, "hello");
			npcsay(player, n, "good day brave adventurer",
				"could i interest you in my fine selection of weapons?");

			int option = multi(player, n, "i'll take a look", "no thanks");
			switch (option) {
				case 0:
					player.setAccessingShop(shop);
					ActionSender.showShop(player, shop);
					break;
				case 1:
					npcsay(player, n, "grrrr");
					break;

			}
		}
	}

	@Override
	public boolean blockTalkNpc(Player player, Npc n) {
		return n.getID() == NpcId.GULLUCK.id();
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
}
