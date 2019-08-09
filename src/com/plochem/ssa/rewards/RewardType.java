package com.plochem.ssa.rewards;

public enum RewardType {
	DAILY(200,300, "Rare"),
	MONTHLY(5000,3000, "Mythic"),
	ELITE_MONTHLY(5000,3000, "Destiny"),
	MASTER_MONTHLY(5000,3000, "Destiny"),
	LEGEND_MONTHLY(5000,3000, "Mythic"),
	MYSTIC_MONTHLY(5000,3000, "Mythic");
	
	private int money;
	private int xp;
	private String crateKey;
	
	private RewardType(int money, int xp, String crateKey) {
		this.setMoney(money);
		this.setXp(xp);
		this.crateKey = crateKey;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}
	
	public String getCrateKey() {
		return crateKey;
	}
}
