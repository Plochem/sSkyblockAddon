package com.plochem.ssa.bosses;

import java.util.List;

import com.plochem.ssa.bosses.skills.Skill;

public class BossEntity {
	private String name;
	private List<Skill> skills;
	private List<String> info;
	private BossReward reward;
	private BossStatistics stats;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Skill> getSkills() {
		return skills;
	}
	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}
	public List<String> getInfo() {
		return info;
	}
	public void setInfo(List<String> info) {
		this.info = info;
	}
	public BossReward getReward() {
		return reward;
	}
	public void setReward(BossReward reward) {
		this.reward = reward;
	}
	public BossStatistics getStats() {
		return stats;
	}
	public void setStats(BossStatistics stats) {
		this.stats = stats;
	}
	
}
