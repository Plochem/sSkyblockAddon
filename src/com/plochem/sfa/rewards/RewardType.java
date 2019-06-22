package com.plochem.sfa.rewards;

public enum RewardType {
	DAILY(1,1),
	MONTHLY(1,1),
	ELITEMONTHLY(1,1),
	MASTERMONTHLY(1,1),
	LEGENDMONTHLY(1,1),
	MYSTICMONTHLY(1,1);
	
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
