package com.openrsc.server.plugins.authentic.npcs.falador;

import com.openrsc.server.constants.ItemId;
import com.openrsc.server.constants.NpcId;
import com.openrsc.server.constants.Quests;
import com.openrsc.server.model.container.Item;
import com.openrsc.server.model.entity.npc.Npc;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.net.rsc.ActionSender;
import com.openrsc.server.plugins.custom.quests.free.PeelingTheOnion;
import com.openrsc.server.plugins.triggers.TalkNpcTrigger;

import static com.openrsc.server.plugins.Functions.*;

public class MakeOverMage implements TalkNpcTrigger {
	@Override
	public void onTalkNpc(Player player, final Npc npc) {
		if (config().WANT_CUSTOM_QUESTS && player.getQuestStage(Quests.PEELING_THE_ONION) >= PeelingTheOnion.STATE_SEDRIDOR_SUGGESTED_YOU_VISIT_MAKE_OVER_MAGE) {
			PeelingTheOnion.makeOverMageDialogue(player, npc);
			return;
		}
		if (player.getCache().hasKey("ogre_makeover_voucher")) {
			PeelingTheOnion.freeMakeover(player, npc);
			return;
		}

		makeOverMageAuthenticDialogue(player, npc);
	}

	public static void makeOverMageAuthenticDialogue(Player player, Npc npc) {
		npcsay(player, npc, "Are you happy with your looks?",
			"If not I can change them for the cheap cheap price",
			"Of 3000 coins");
		int opt = multi(player, npc, "I'm happy with how I look thank you",
			"Yes change my looks please");
		if (opt == 1) {
			if (!ifheld(player, ItemId.COINS.id(), 3000)) {
				say(player, npc, "I'll just go and get the cash");
			} else {
				if (player.getCarriedItems().remove(new Item(ItemId.COINS.id(), 3000)) == -1) return;
				ActionSender.sendAppearanceScreen(player);
			}
		}
	}

	@Override
	public boolean blockTalkNpc(Player player, Npc n) {
		return n.getID() == NpcId.MAKE_OVER_MAGE.id();
	}

}
