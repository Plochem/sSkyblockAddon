package com.plochem.ssa.rewards;

public enum RewardType {
	DAILY(200,300),
	MONTHLY(5000,3000),
	ELITE_MONTHLY(5000,3000),
	MASTER_MONTHLY(5000,3000),
	LEGEND_MONTHLY(5000,3000),
	MYSTIC_MONTHLY(5000,3000);
	
	private int money;
	private int xp;
	
	private RewardType(int money, int xp) {
		this.setMoney(money);
		this.setXp(xp);
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
}
