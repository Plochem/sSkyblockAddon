package com.plochem.sfa.rewards;

public enum RewardType {
	DAILY(1000,300),
	MONTHLY(10000,3000),
	ELITEMONTHLY(10000,3000),
	MASTERMONTHLY(10000,3000),
	LEGENDMONTHLY(10000,3000),
	MYSTICMONTHLY(10000,3000);
	
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
