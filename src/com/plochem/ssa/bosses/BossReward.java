package com.plochem.ssa.bosses;

import java.util.List;

public class BossReward {
	private List<List<String>> topDmgCommands;
	private List<String> otherDmgCommands;
	
	public BossReward(List<List<String>> topDmgCommands, List<String> otherDmgCommands) {
		this.topDmgCommands = topDmgCommands;
		this.otherDmgCommands = otherDmgCommands;
	}
	
	public List<List<String>> getTopDmgCommands() {
		return topDmgCommands;
	}
	
	public List<String> getOtherDmgCommands() {
		return otherDmgCommands;
	}
	
}
