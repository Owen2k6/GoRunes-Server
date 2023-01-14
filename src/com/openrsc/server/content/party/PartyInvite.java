package com.openrsc.server.content.party;

import com.openrsc.server.constants.Constants;
import com.openrsc.server.constants.IronmanMode;
import com.openrsc.server.event.SingleEvent;
import com.openrsc.server.model.entity.player.Player;
import com.openrsc.server.net.rsc.ActionSender;

public class PartyInvite {

	public Player inviter, invited;
	private SingleEvent timeOutEvent;

	private PartyInvite(Player inviter, Player invited) {
		this.inviter = inviter;
		this.invited = invited;
	}

	public static void createPartyInvite(Player player, Player invited) {

		if (invited.isIronMan(IronmanMode.Ironman.id()) || invited.isIronMan(IronmanMode.Ultimate.id())
			|| invited.isIronMan(IronmanMode.Hardcore.id()) || invited.isIronMan(IronmanMode.Transfer.id())) {
			player.message(invited.getUsername() + " is an Iron Man. " + (invited.isMale() ? "He" : "She") + " stands alone.");
			return;
		}
		if (player.isIronMan(IronmanMode.Ironman.id()) || player.isIronMan(IronmanMode.Ultimate.id())
			|| player.isIronMan(IronmanMode.Hardcore.id()) || player.isIronMan(IronmanMode.Transfer.id())) {
			player.message("You are an Iron Man. You stand alone.");
			return;
		}
		if (player.getParty() == null) {
			return;
		}
		if (player.getParty().getPlayers().size() >= Constants.MAX_PARTY_SIZE) {
			player.message("Your party has reached the maximum party members limit");
			return;
		}
		if(player.getSocial().isIgnoring(invited.getUsernameHash())){
			ActionSender.sendBox(player,
				"@lre@Party: %"
					+ " % Remove "
					+ invited.getUsername() + " from your ignore list and try again. %"
				, true);
				return;
		}
		if(invited.getSocial().isIgnoring(player.getUsernameHash())){
			ActionSender.sendBox(player,
				"@lre@Party: %"
					+ " %"
					+ invited.getUsername() + " is not accepting party invitations right now. %"
				, true);
				return;
		}
		if (invited.getCache().hasKey("party_block_invites")) {
			boolean blockInvites = invited.getCache().getBoolean("party_block_invites");
			if (blockInvites) {
				ActionSender.sendBox(player,
					"@lre@Party: %"
						+ " %"
						+ invited.getUsername() + " is not accepting party invitations right now. %"
					, true);
				//invited.message(player.getUsername() + " tried to send you a party invite, but you have party invite settings blocked");
				return;
			}
		} else {
			if (invited.getPartyInviteSetting() == true) {
				ActionSender.sendBox(player,
					"@lre@Party: %"
						+ " %"
						+ invited.getUsername() + " is not accepting party invitations right now. %"
					, true);
				return;
			}

		}
		if (invited.getActivePartyInvite() != null) {
			//player.message(invited.getUsername() + " has already and active party invitation, please try again later.");
			ActionSender.sendBox(player,
				"@lre@Party: %"
					+ " %"
					+ invited.getUsername() + " already has an active party invitation. Please wait 30 seconds and try again %"
				, true);

			return;
		}
		if (invited.getParty() == player.getParty()) {
			ActionSender.sendBox(player,
				"@lre@Party: %"
					+ " %"
					+ invited.getUsername() + " is already in your party %"
				, true);
			return;
		} else if (invited.getParty() != null) {
			ActionSender.sendBox(player,
				"@lre@Party: %"
					+ " %"
					+ invited.getUsername() + " is already in a party %"
				, true);
			invited.message(player.getUsername() + " tried to send you a party invite, but you are already in a party");
			return;
		}
		if (invited.equals(player)) {
			if (invited.equals(player)) {
				ActionSender.sendBox(player,
					"@lre@Party: %"
						+ " %"
						+ "You cannot invite yourself %"
					, true);
				return;
			}
			return;
		}
		PartyInvite PartyInvite = new PartyInvite(player, invited);
		PartyInvite.startTimeoutCounter();

		ActionSender.sendBox(player,
			"@lre@Party: %"
				+ " %"
				+ "You have successfully invited " + invited.getUsername() + " to the Party %"
			, true);
		PartyPlayer p3 = player.getParty().getLeader();
		if(p3.getShareLoot() > 0){
			invited.message("@whi@[@gre@Party@whi@]@yel@" + player.getParty().getPlayers().size() + " @whi@members. (Loot Sharing) - @gre@YES");
		} else {
			invited.message("@whi@[@gre@Party@whi@]@yel@" + player.getParty().getPlayers().size() + " @whi@members. (Loot Sharing) - @red@NO");
		}
		if(p3.getShareExp() > 0){
			invited.message("@whi@[@gre@Party@whi@]@yel@" + player.getParty().getPlayers().size() + " @whi@members. (Exp Sharing) - @gre@YES");
		} else {
			invited.message("@whi@[@gre@Party@whi@]@yel@" + player.getParty().getPlayers().size() + " @whi@members. (Exp Sharing) - @red@NO");
		}
		for (Player p : player.getWorld().getPlayers()) {
			if (p.getParty() == player.getParty()) {
				invited.message("@gre@[Party]@whi@" + p + "");
			}
		}
		invited.setActivePartyInvite(PartyInvite);

		if (invited.getLocation().inWilderness() || invited.inCombat()) {
			invited.message("Type ::partyaccept to accept your invitation");
		} else {
			ActionSender.sendPartyInvitationGUI(invited, player.getParty().getPartyName(), player.getUsername());
		}
	}

	private void startTimeoutCounter() {
		timeOutEvent = new SingleEvent(invited.getWorld(), null, 60000, "Party Invite") {
			@Override
			public void action() {
				inviter.message(invited.getUsername() + " did not respond to your invitation");
				invited.setActivePartyInvite(null);
				invited.message("You did not respond to your Party invite in time");
				inviter.message(invited.getUsername() + "'s Party invitation is no longer active");
			}
		};
		invited.getWorld().getServer().getGameEventHandler().add(timeOutEvent);
	}

	public void accept() {
		if (inviter.getParty() != null) {
			if (invited.getParty() != null) {
				return;
			}
			inviter.getParty().addPlayer(invited);
		}
		invited.setActivePartyInvite(null);
		timeOutEvent.stop();
	}

	public void decline() {
		if (inviter != null) {
			inviter.message(invited.getUsername() + " has declined your party invitation");
		}
		invited.setActivePartyInvite(null);
		timeOutEvent.stop();
	}
}
