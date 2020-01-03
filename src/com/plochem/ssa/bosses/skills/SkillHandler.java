package com.plochem.ssa.bosses.skills;

import java.util.List;
import java.util.UUID;

public interface SkillHandler {
	void castSkill(Skill skill, List<UUID> nearbyPlayers);
}
