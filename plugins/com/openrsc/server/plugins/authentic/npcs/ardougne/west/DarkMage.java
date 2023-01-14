package com.openrsc.server.plugins.authentic.npcs.ardougne.west;

import com.openrsc.server.constants.ItemId;
import com.openrsc.server.constants.NpcId;
import com.openrsc.server.constants.Quests;
import com.openrsc.server.model.container.Item;
import com.openrsc.server.model.entity.npc.Npc;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.plugins.triggers.TalkNpcTrigger;

import java.util.Optional;

import static com.openrsc.server.plugins.Functions.*;

public class DarkMage implements TalkNpcTrigger {

	@Override
	public void onTalkNpc(Player player, Npc n) {
		if (n.getID() == NpcId.DARK_MAGE.id()) {
			say(player, n, "hello there");
			npcsay(player, n, "why do do you interupt me traveller?");
			if (player.getQuestStage(Quests.UNDERGROUND_PASS) != -1) {
				say(player, n, "i'm just looking around");
				npcsay(player, n, "there's nothing to see here",
					"just despair and death");
				return;
			}
			say(player, n, "i just wondered what you're doing?");
			npcsay(player, n, "i experiment with dark magic",
				"it's a dangerous craft");
			if (player.getCarriedItems().hasCatalogID(ItemId.STAFF_OF_IBAN_BROKEN.id(), Optional.of(false))) {
				say(player, n, "could you fix this staff?");
				player.message("you show the mage your staff of iban");
				npcsay(player, n, "almighty zamorak! the staff of iban!");
				say(player, n, "can you fix it?");
				npcsay(player, n, "this truly is dangerous magic traveller",
					"i can fix it, but it will cost you",
					"the process could kill me");
				say(player, n, "how much?");
				npcsay(player, n, "200,000 gold pieces, not a penny less");
				int menu = multi(player, n,
					"no chance, that's ridiculous",
					"ok then");
				if (menu == 0) {
					npcsay(player, n, "fine by me");
				} else if (menu == 1) {
					if (!ifheld(player, ItemId.COINS.id(), 200000)) {
						player.message("you don't have enough money");
						say(player, n, "oops, i'm a bit short");
					} else {
						mes("you give the mage 200,000 coins");
						delay(3);
						mes("and the staff of iban");
						delay(3);
						if (player.getCarriedItems().remove(new Item(ItemId.COINS.id(), 200000),
							new Item(ItemId.STAFF_OF_IBAN_BROKEN.id()))) {
							player.message("the mage fixes the staff and returns it to you");
							give(player, ItemId.STAFF_OF_IBAN.id(), 1);
							say(player, n, "thanks mage");
							npcsay(player, n, "you be carefull with that thing");
						}
					}
				}
			}
		}
	}

	@Override
	public boolean blockTalkNpc(Player player, Npc n) {
		return n.getID() == NpcId.DARK_MAGE.id();
	}

}
