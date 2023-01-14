package com.openrsc.server.event.rsc.impl.combat.scripts;

import com.openrsc.server.model.entity.Mob;

public interface OnCombatStartScript {

	public boolean shouldExecute(Mob attacker, Mob defender);

	public void executeScript(Mob attacker, Mob defender);
}
