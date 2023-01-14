package com.openrsc.server.plugins.authentic.npcs.shilo;

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
public class Jiminua extends AbstractShop {

	private final Shop shop = new Shop(true, 15000, 150, 50, 2,
		new Item(ItemId.TINDERBOX.id(), 2), new Item(ItemId.EMPTY_VIAL.id(), 10), new Item(ItemId.PESTLE_AND_MORTAR.id(), 3),
		new Item(ItemId.POT.id(), 3), new Item(ItemId.BRONZE_AXE.id(), 3), new Item(ItemId.BRONZE_PICKAXE.id(), 2),
		new Item(ItemId.IRON_AXE.id(), 5), new Item(ItemId.LEATHER_ARMOUR.id(), 12), new Item(ItemId.LEATHER_GLOVES.id(), 10),
		new Item(ItemId.BOOTS.id(), 10), new Item(ItemId.COOKEDMEAT.id(), 2), new Item(ItemId.BREAD.id(), 10),
		new Item(ItemId.BRONZE_BAR.id(), 10), new Item(ItemId.SPADE.id(), 10), new Item(ItemId.UNLIT_CANDLE.id(), 10),
		new Item(ItemId.UNLIT_TORCH.id(), 10), new Item(ItemId.CHISEL.id(), 10), new Item(ItemId.HAMMER.id(), 10),
		new Item(ItemId.PAPYRUS.id(), 50), new Item(ItemId.A_LUMP_OF_CHARCOAL.id(), 50), new Item(ItemId.VIAL.id(), 50),
		new Item(ItemId.MACHETTE.id(), 50));

	@Override
	public void onTalkNpc(Player player, Npc n) {
		if (n.getID() == NpcId.JIMINUA.id()) {
			npcsay(player, n, "Welcome to the Jungle Store, Can I help you at all?");
			int menu = multi(player, n,
				"Yes please. What are you selling?",
				"No thanks");
			if (menu == 0) {
				npcsay(player, n, "Take yourself a good look");
				player.setAccessingShop(shop);
				ActionSender.showShop(player, shop);
			}
		}
	}

	@Override
	public boolean blockTalkNpc(Player player, Npc n) {
		return n.getID() == NpcId.JIMINUA.id();
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
