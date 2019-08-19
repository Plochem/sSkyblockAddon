package com.plochem.ssa.cosmetics.types;

public enum KillMessage {
	Default,
	Insect(5000, "has squished");
	
	private int cost;
	private String killMessage;
	
	private KillMessage(int cost, String killMessage) {
		this.killMessage = killMessage;
		this.cost = cost;
	}
	
	private KillMessage() {
	}
	
	public int getCost() {
		return cost;
	}
	
	public String getKillMessage(){
		return killMessage;
	}
}
