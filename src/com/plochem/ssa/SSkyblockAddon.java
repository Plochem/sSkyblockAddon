package com.plochem.ssa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.plochem.ssa.boosters.Booster;
import com.plochem.ssa.boosters.BoosterActivate;
import com.plochem.ssa.boosters.BoosterClaim;
import com.plochem.ssa.boosters.BoosterManager;
import com.plochem.ssa.boosters.BoosterType;
import com.plochem.ssa.bouncepads.BouncePadBreak;
import com.plochem.ssa.bouncepads.BouncePadInteract;
import com.plochem.ssa.bouncepads.BouncePadPlace;
import com.plochem.ssa.cosmetics.CosmeticListener;
import com.plochem.ssa.cosmetics.CosmeticManager;
import com.plochem.ssa.cosmetics.CosmeticMenuListener;
import com.plochem.ssa.economy.BanknoteListener;
import com.plochem.ssa.economy.BanknoteManager;
import com.plochem.ssa.economy.SEconomy;
import com.plochem.ssa.economy.SEconomyImplementer;
import com.plochem.ssa.generator.Generator;
import com.plochem.ssa.generator.GeneratorListeners;
import com.plochem.ssa.generator.GeneratorManager;
import com.plochem.ssa.homes.Home;
import com.plochem.ssa.homes.HomeManager;
import com.plochem.ssa.kits.Kit;
import com.plochem.ssa.kits.KitManager;
import com.plochem.ssa.kits.KitPreview;
import com.plochem.ssa.kits.KitSelection;
import com.plochem.ssa.listeners.ChatHandling;
import com.plochem.ssa.listeners.EventsCancelInSpawn;
import com.plochem.ssa.listeners.InvseeEdit;
import com.plochem.ssa.listeners.PlayerDeath;
import com.plochem.ssa.listeners.PlayerJoin;
import com.plochem.ssa.listeners.PlayerKillEntity;
import com.plochem.ssa.listeners.PlayerMove;
import com.plochem.ssa.listeners.PlayerRespawn;
import com.plochem.ssa.menu.MenuListener;
import com.plochem.ssa.oregen.OreGenListener;
import com.plochem.ssa.oregen.OreManager;
import com.plochem.ssa.perks.PerkListeners;
import com.plochem.ssa.repair.RepairManager;
import com.plochem.ssa.rewards.RewardListener;
import com.plochem.ssa.rewards.RewardManager;
import com.plochem.ssa.seasons.SeasonManager;
import com.plochem.ssa.seasons.SeasonMenuListener;
import com.plochem.ssa.sellchest.SellChestListener;
import com.plochem.ssa.sellchest.SellChestManager;
import com.plochem.ssa.staffheadabilities.SkullAbility;
import com.plochem.ssa.staffheadabilities.SkullEquipListeners;
import com.plochem.ssa.stats.LeaderboardHandler;
import com.plochem.ssa.stats.StatsListener;
import com.plochem.ssa.tags.TagManager;
import com.plochem.ssa.tags.TagsMenuListener;
import com.plochem.ssa.trading.TradeListener;
import com.plochem.ssa.trading.TradeManager;


public class SSkyblockAddon extends JavaPlugin {
	private File bpFile;
	private YamlConfiguration bpData;
	private File storage;
	private YamlConfiguration storageData;
	private SEconomy sEco = new SEconomy(this);
	private List<UUID> runningSomeTPCmd = new ArrayList<>();
	private List<UUID> viewingInv = new ArrayList<>();
	private Map<UUID, UUID> tpReq = new HashMap<>();
	private List<String> consoleCmds = Arrays.asList("addbal", "givebooster", "setbal", "givegen", "tags", "givesellchest", "givebanknote");

	public void onEnable(){
		sEco.hook();
        Iterator<Recipe> it = Bukkit.getServer().recipeIterator();
        Recipe recipe;
        while(it.hasNext())
        {
            recipe = it.next();
            if (recipe != null && recipe.getResult().getType() == Material.HOPPER){
                it.remove();
            }
        }        
		new BukkitRunnable() { // waits for one tick before reading config because worlds have to be loaded first
			@Override
			public void run() {
				bpFile = new File("plugins/SFA/bouncepads-locations.yml");
				bpData = YamlConfiguration.loadConfiguration(bpFile);
				if(!(bpFile.exists())) {
					Bukkit.getServer().getLogger().info("[SFA] Creating bounce pad storage file!");
					bpData.createSection("bp.locs");
					saveBP();
				}  else {
					Bukkit.getServer().getLogger().info("[SFA] Bounce pad storage file already exists! Skipping creation...");
				}
				storage = new File("plugins/SFA/storage.yml");
				storageData = YamlConfiguration.loadConfiguration(storage);
				if(!storage.exists()) {
					saveStorage();
				} else {
					Bukkit.getServer().getLogger().info("[SFA] General storage file already exists! Skipping creation...");
				}
				registerThings();
				RewardManager.createCollectedFile();
				BoosterManager.createQueueFile();
				KitManager.createKitFile();
				GeneratorManager.createGeneratorFile();
				GeneratorManager.startGlobalCounter();
				RewardManager.resetListTimer();
				RepairManager.createRepairFile();
				RepairManager.startTimer();
				SeasonManager.createSeasonFile();
				SeasonManager.startTimer();
				CosmeticManager.registerPerms();
				CosmeticManager.createConfig();
				for(Player p : Bukkit.getOnlinePlayers()) {
					KitManager.readCooldownFiles(p.getUniqueId());
				}
				for(File f : new File("plugins/SFA/playerbalance").listFiles()) { //loads balance from file to hashmap
					YamlConfiguration playerData = YamlConfiguration.loadConfiguration(f);
					sEco.loadToMap(UUID.fromString(f.getName().replaceAll(".yml", "")), playerData.getDouble("balance"));
				}
				BoosterManager.activate(BoosterType.EXPERIENCE);
				BoosterManager.activate(BoosterType.MONEY);	
				TagManager.createTagFiles();

				if(!(bpFile.exists())) {
					Bukkit.getServer().getLogger().info("[SFA] Creating bounce pad storage file!");
					bpData.createSection("bp.locs");
					saveBP();
				}  else {
					Bukkit.getServer().getLogger().info("[SFA] Bounce pad storage file already exists! Skipping creation...");
				}
				LeaderboardHandler.update(SSkyblockAddon.getPlugin(SSkyblockAddon.class), storageData);
			}

		}.runTaskLater(this, 1);	
	}

	public void onDisable(){
		sEco.unhook();
		CosmeticManager.saveSelections();
		LeaderboardHandler.deleteLeaderboards();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		Player pl = null;
		if(sender instanceof Player) {
			pl = (Player) sender;
		} else {
			if(consoleCmds.contains(command.getName().toLowerCase())) {
				// continue
			} else {
				sender.sendMessage("[SFA] You must be a player to do this!");
				return false;
			}
		}
		final Player p = pl;
		if(command.getName().equalsIgnoreCase("giveBouncePad")) {
			if(args.length > 1) {
				p.sendMessage("Unknown command. Type \"/help\" for help.");
				return false;
			}
			if(p.hasPermission("sfa.giveBouncePad") || p.isOp()){
				ItemStack t = new ItemStack(Material.SLIME_BLOCK);
				ItemMeta tm = t.getItemMeta();
				tm.setDisplayName("§aBounce Pad");
				t.setItemMeta(tm);
				if(args.length == 0) {
					p.getInventory().addItem(t);
				} else if(args.length == 1) {
					Player targetted = Bukkit.getPlayer(args[0]);
					if(targetted == null) {
						p.sendMessage("§cSorry, but that player cannot be found!");
					} else {
						targetted.getInventory().addItem(t);
					}
				}

			} else {
				p.sendMessage("§cYou do not have permission to perform this command!");
				return false;
			}
		} else if (command.getName().equalsIgnoreCase("bal") || command.getName().equalsIgnoreCase("balance")) {
			if(args.length == 0) { // display own balance
				double balance = sEco.getEconomyImplementer().getBalance(p);
				p.sendMessage("§eYou have §a$" + String.format("%,.2f", balance) + "§e in your account.");
			} else if(args.length == 1) { //display target player balance
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
				if(!target.isOnline() && !target.hasPlayedBefore()) {
					p.sendMessage("§cSorry, but that player cannot be found!");
					return false;
				}
				double balance = sEco.getEconomyImplementer().getBalance(target);
				p.sendMessage("§e" + target.getName() + " has §a$" + String.format("%,.2f", balance) + "§e in their account.");
			} else { // too many args
				p.sendMessage("§cUsage: /bal [player name]");
			}
			return true;
		} else if(command.getName().equalsIgnoreCase("pay")) {
			if(args.length == 2) {
				if(NumberUtils.isNumber(args[1])) {
					OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
					if(!target.isOnline() && !target.hasPlayedBefore()) {
						p.sendMessage("§cSorry, but that player cannot be found!");
						return false;
					}
					double amt = Double.parseDouble(args[1]);
					if(amt < 0) {
						p.sendMessage("§cYou can only send an amount greater than 0!");
						return false;
					}
					SEconomyImplementer sei = sEco.getEconomyImplementer();
					if(sei.getBalance(p) >= amt) {
						sei.depositPlayer(target, amt);
						sei.withdrawPlayer(p, amt);
						p.sendMessage("§eYou paid §a$" + String.format("%,.2f", amt) + "§e to §a" + target.getName() + "§e.");
						if(target.isOnline())
							target.getPlayer().sendMessage("§eYou received §a$" + String.format("%,.2f", amt) + "§e from §a" + p.getName() + "§e.");
					} else {
						p.sendMessage("§cYou do not have enough money to send to §e" + target.getName() + "§c.");
					}
				} else {
					p.sendMessage("§cEnter a valid numerical amount to send.");
				}
			} else {
				p.sendMessage("§cUsage: /pay [player name] [integer amount]");
			}
			return true;
		} else if(command.getName().equalsIgnoreCase("addbal")) {
			if(sender.hasPermission("sfa.addBal") || sender.isOp()){
				if(args.length != 2) {
					sender.sendMessage("§cUsage: /addbal [player name] [integer amount]");
					return false;
				}
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
				if(!target.isOnline() && !target.hasPlayedBefore()) {
					sender.sendMessage("§cSorry, but that player cannot be found!");
					return false;
				}
				if(NumberUtils.isNumber(args[1])) {
					double amt = Double.parseDouble(args[1]);
					SEconomyImplementer sei = sEco.getEconomyImplementer();
					sei.depositPlayer(target, amt);
					sender.sendMessage("§eYou added §a$" + String.format("%,.2f", amt) + "§e to §a" + target.getName() + "'s §eaccount.");
					if(target.isOnline())
						target.getPlayer().sendMessage("§eYou received §a$" + String.format("%,.2f", amt) + "§e from §a" + sender.getName() + "§e.");
				} else {
					sender.sendMessage("§cEnter a valid numerical amount to send.");
				}
			} else {
				sender.sendMessage("§cYou do not have permission to perform this command!");
			}
		}  else if(command.getName().equalsIgnoreCase("setbal")) {
			if(sender.hasPermission("sfa.setBal") || sender.isOp()){
				if(args.length != 2) {
					sender.sendMessage("§cUsage: /setbal [player name] [integer amount]");
					return false;
				}
				OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
				if(!target.isOnline() && !target.hasPlayedBefore()) {
					sender.sendMessage("§cSorry, but that player cannot be found!");
					return false;
				}
				if(NumberUtils.isNumber(args[1])) {
					double amt = Double.parseDouble(args[1]);
					SEconomyImplementer sei = sEco.getEconomyImplementer();
					sei.setBalance(target, amt);
					sender.sendMessage("§eYou set " + target.getName() + "'s §eaccount to §a$" + String.format("%,.2f", amt) + "§e.");
					if(target.isOnline())
						target.getPlayer().sendMessage("§eYour balance has been set to §a$" + String.format("%,.2f", amt) + "§e.");
				} else {
					sender.sendMessage("§cEnter a valid numerical amount to send.");
				}
			} else {
				sender.sendMessage("§cYou do not have permission to perform this command!");
			}
		} else if(command.getName().equalsIgnoreCase("givebanknote")) {
			if(!sender.hasPermission("sfa.banknote.give")) {
				sender.sendMessage("§cYou do not have permission to perform this command!");
				return false;
			}
			if(args.length != 2) {
				sender.sendMessage("§cUsage: /givebanknote [player name] [amount]");
				return false;
			}
			Player to = Bukkit.getPlayer(args[0]);
			if(to == null) {
				sender.sendMessage("§cSorry, but that player cannot be found!");
				return false;
			}
			if(NumberUtils.isNumber(args[1])) {
				double amt = Double.parseDouble(args[1]);
				BanknoteManager.giveBanknote(to, amt);
			} else {
				sender.sendMessage("§cEnter a valid numerical value.");
			}
		} else if(command.getName().equalsIgnoreCase("withdraw")) {
			if(args.length != 1) {
				sender.sendMessage("§cUsage: /withdraw [amount]");
				return false;
			}
			if(NumberUtils.isNumber(args[0])) {
				double amt = Double.parseDouble(args[0]);
				BanknoteManager.withdraw(p, amt);
			} else {
				p.sendMessage("§cEnter a valid numerical value.");
			}
		}else if(command.getName().equalsIgnoreCase("boosterqueue")) { // sends player current queue in boosters
			p.sendMessage("§l---------------------------------------------");
			p.sendMessage("                   §aMoney §eand §3Experience §eBoosters");
			p.sendMessage("");
			String cBoosters = "§aMoney Boosters: ";
			if(BoosterManager.getMoneyBoosters().isEmpty()) {
				cBoosters = cBoosters + "§cNo boosters queued!";
			} else {
				for(Booster booster : BoosterManager.getMoneyBoosters()) {
					cBoosters = cBoosters + "§6x2 §aby §b" + Bukkit.getPlayer(booster.getUUID()).getName() + "§a, ";
				}
				cBoosters = cBoosters.substring(0, cBoosters.length() - 2);  // removes unwanted comma and space from end of string
			}
			p.sendMessage(cBoosters); 
			p.sendMessage("");
			String expBoosters = "§3Experience Boosters: ";
			if(BoosterManager.getExpBoosters().isEmpty()) {
				expBoosters = expBoosters + "§cNo boosters queued!";
			} else {
				for(Booster booster : BoosterManager.getExpBoosters()) {
					expBoosters = expBoosters + "§6x2 §aby §b" + Bukkit.getPlayer(booster.getUUID()).getName() + "§a, ";
				}
				expBoosters = expBoosters.substring(0, expBoosters.length() - 2);  // removes unwanted comma and space from end of string
			}
			p.sendMessage(expBoosters);
			p.sendMessage("§l---------------------------------------------");
		} else if(command.getName().equalsIgnoreCase("myboosters")) {
			File playerFile = new File("plugins/SFA/playerBoosters/" + p.getUniqueId().toString() + ".yml");
			YamlConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
			Inventory inv = Bukkit.createInventory(p, 54, "Your Boosters");
			for(Booster booster : ((List<Booster>)playerData.getList("boosters"))) {
				ItemStack itemRep = new ItemStack(Material.INK_SACK, 1, (short)13);
				if(booster.getType() == BoosterType.MONEY) {
					itemRep = new ItemStack(Material.INK_SACK, 1, (short)10);
				}
				ItemMeta im = itemRep.getItemMeta();
				im.setDisplayName(booster.getName().replaceAll("&", "§"));
				im.setLore(Arrays.asList("§7This gives a x2 " + booster.getType().toString().toLowerCase() + " boost to all players.", "", "§eClick to activate the booster!"));
				itemRep.setItemMeta(im);
				inv.setItem(inv.firstEmpty(), itemRep);
			}
			p.openInventory(inv);
		} else if(command.getName().equalsIgnoreCase("givebooster")) {
			if(args.length != 4) {
				sender.sendMessage("§cUsage: /givebooster [name] [experience/money] [minutes] [amount]");
				return false;
			}
			if(!sender.hasPermission("sfa.getbooster")) {
				sender.sendMessage("§cYou do not have permission to perform this command!");
				return false;
			}
			OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
			if(!target.isOnline() && !target.hasPlayedBefore()) {
				sender.sendMessage("§cSorry, but that player cannot be found!");
				return false;
			}
			String color = "";
			if(args[1].equalsIgnoreCase("experience")) {
				color = "§3§l";				
			} else if(args[1].equalsIgnoreCase("money")) {
				color = "§6§l";
			} else {
				sender.sendMessage("§cUsage: /getbooster [name] [experience/money] [minutes]");
				return false;
			}
			if(StringUtils.isNumeric(args[2])) {
				if(StringUtils.isNumeric(args[3])) {
					String name = color + Integer.parseInt(args[2]) + " minute " + args[1].toLowerCase() + " booster"; //TODO
					BoosterManager.claimBooster(new Booster(target.getUniqueId(), Integer.parseInt(args[2]), Integer.parseInt(args[2]), BoosterType.valueOf(args[1].toUpperCase()), name), color, Integer.parseInt(args[3]));
				} else {
					sender.sendMessage("§cEnter a valid amount of boosters to give.");
				}
			} else {
				sender.sendMessage("§cEnter a valid integer amount of minutes.");
				return false;
			}
		} else if(command.getName().equalsIgnoreCase("kits") || command.getName().equalsIgnoreCase("kit")) {
			if(KitManager.getKits().isEmpty()) {
				p.sendMessage("§cNo kits have been created yet!");
			} else {
				KitManager.openMenu(p);
			}
		} else if(command.getName().equalsIgnoreCase("createkit")) { 
			if(args.length != 1) {
				p.sendMessage("§cUsage: /createkit [name]");
				return false;
			}
			if(!p.hasPermission("sfa.createkit")) {
				p.sendMessage("§cYou do not have permission to perform this command!");
				return false;
			}
			List<ItemStack> temp = new ArrayList<>();
			for(ItemStack i : p.getInventory().getContents()) {
				if(i != null)
					temp.add(i);
			}
			KitManager.addKit(new Kit(86400, args[0], new ItemStack(Material.CHEST), temp));
			p.sendMessage("§aYou created the the §e" + args[0] + " §akit!");
		} else if(command.getName().equalsIgnoreCase("pvp")) {
			if(getSpawn()==null)
				p.sendMessage("§cThe PVP spawnpoint has not been created yet!");
			else
				teleportCoolDown(getPvPSpawn(), p, 5);
		} else if(command.getName().equalsIgnoreCase("tpa")) {
			if(args.length != 1) {
				p.sendMessage("§cUsage: /tpa [player name]");
				return false;
			} 
			Player to = Bukkit.getPlayer(args[0]);
			if(to == null) {
				p.sendMessage("§cSorry, but that player cannot be found!");
				return false;
			}
			if(tpReq.containsValue(p.getUniqueId())) {
				p.sendMessage("§cYou already have a pending teleport request.");
				return false;
			}

			if(tpReq.containsKey(to.getUniqueId())) {
				p.sendMessage("§cThat player currently has a teleport request to handle.");
				return false;
			}

			if(runningSomeTPCmd.contains(p.getUniqueId())) {
				p.sendMessage("§cWait for the command to finish executing!");
				return false;
			}
			p.sendMessage("§aRequest sent to §f" + to.getName() + "§a.");
			to.sendMessage("§f" + p.getName() + "§6 requested to teleport to you.");
			to.sendMessage("§6Do §c/tpaccept §6to accept the request or §c/tpdeny §6to decline the request.");
			tpReq.put(to.getUniqueId(), p.getUniqueId());
		} else if (command.getName().equalsIgnoreCase("tpaccept")) {
			if(tpReq.containsKey(p.getUniqueId())) {
				p.sendMessage("§aYou accepted the teleport request.");
				new BukkitRunnable() {
					int time = 5;
					Player from = Bukkit.getPlayer(tpReq.get(p.getUniqueId()));
					{
						runningSomeTPCmd.add(from.getUniqueId());
						from.sendMessage("§f" + p.getName() + "§a has accepted your request! §cDon't move!");
					}
					@Override
					public void run() {
						if(!runningSomeTPCmd.contains(from.getUniqueId())) {
							from.sendMessage("§cTeleportation has been canceled by your movement!");
							tpReq.remove(p.getUniqueId());
							this.cancel();
							return;
						}
						if(time == 0) { 
							this.cancel();
							from.teleport(p);
							tpReq.remove(p.getUniqueId());
							runningSomeTPCmd.remove(p.getUniqueId());
							return;
						}
						from.sendMessage("§6Teleporting you in §c" + time + " §6second(s)...");
						time--;
					}
				}.runTaskTimer(this, 0, 20);
			} else {
				p.sendMessage("§cNobody has requested to teleport to you.");
			}
		} else if(command.getName().equalsIgnoreCase("tpdeny")) {
			if(tpReq.containsKey(p.getUniqueId())) {
				Player from = Bukkit.getPlayer(tpReq.get(p.getUniqueId()));
				p.sendMessage("§aYou declined the teleport request!");
				from.sendMessage("§f" + p.getName() + " §chas declined your request!");
				tpReq.remove(p.getUniqueId());
			} else {
				p.sendMessage("§cNobody has requested to teleport to you.");
			}
		} else if(command.getName().equalsIgnoreCase("invsee")) {
			if(!p.hasPermission("sfa.invsee")) {
				p.sendMessage("§cYou do not have permission to perform this command!");
				return false;
			}
			if(args.length != 1) {
				p.sendMessage("§cUsage: /invsee [player name]");
				return false;
			}

			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				p.sendMessage("§cSorry, but that player cannot be found!");
				return false;
			}
			p.openInventory(target.getInventory());
			viewingInv.add(p.getUniqueId());
		} else if(command.getName().equalsIgnoreCase("givegen")) {
			if(args.length != 2) {
				sender.sendMessage("§cUsage: /givegen [player name] [item type]");
			} else {
				GeneratorManager.giveGenerator(sender, args[0], args[1]);
			}
		} else if(command.getName().equalsIgnoreCase("sethome")) {
			HomeManager.addHome(p, args);
		} else if(command.getName().equalsIgnoreCase("delhome")) {
			HomeManager.delHome(p, args);
		} else if(command.getName().equalsIgnoreCase("home")) {
			HomeManager.goHome(p, args);
		} else if(command.getName().equalsIgnoreCase("homes")) {
			HomeManager.displayHomes(p);
		} else if(command.getName().equalsIgnoreCase("menu")) {
			p.openInventory(buildMenu());
		} else if(command.getName().equalsIgnoreCase("baltop")) {
			p.sendMessage("§l---------------------------------------------");
			p.sendMessage("§eHere are the top balances:");
			int idx = 1;
			for(Entry<UUID, Double> e : sEco.getTopBals()) {
				p.sendMessage("§e" + idx + ". §6" + Bukkit.getOfflinePlayer(e.getKey()).getName() + " §e- §a$" + String.format("%,.2f", e.getValue()));
				idx++;
			}
			p.sendMessage("§l---------------------------------------------");
		} else if(command.getName().equalsIgnoreCase("help")) {
			p.sendMessage("§l---------------------------------------------");
			p.sendMessage("§eTo start, do /kit or /kits to view a selection of kits.");
			p.sendMessage("");
			p.sendMessage("§ePerform the /rewards to claim daily or monthly rewards.");
			p.sendMessage("");
			p.sendMessage("§eDo /menu to access a list of things to purchase.");
			p.sendMessage("");
			p.sendMessage("§eIf you need additional help, contact an online staff member or join the server discord: §bdiscord.gg/c9fc5yR");
			p.sendMessage("§l---------------------------------------------");
		} else if(command.getName().equalsIgnoreCase("clearchat")) {
			if(p.hasPermission("sfa.clearchat")) {
				for(Player player : Bukkit.getOnlinePlayers()) {
					for(int i = 0; i <100; i++) {
						player.sendMessage("");
					}
					player.sendMessage("§b" + p.getName() + " has cleared the chat!");
				}
			} else {
				p.sendMessage("§cYou do not have permission to perform this command!");
			}
		} else if(command.getName().equalsIgnoreCase("rewards")) {
			RewardManager.openRewardMenu(p);
		} else if(command.getName().equalsIgnoreCase("trade")) {
			if(args.length == 1) { // trade <name> TODO
				Player to = Bukkit.getPlayer(args[0]);
				if(to == null) {
					p.sendMessage("§cSorry, but that player cannot be found!");
					return false;
				}
				if(to.equals(p)) {
					p.sendMessage("§cYou cannot trade with yourself.");
					return false;
				}

				if(TradeManager.getTradeReq().containsKey(p.getUniqueId())) {
					p.sendMessage("§cYou already sent a trade request.");
					return false;
				}
				if(TradeManager.getTradeReq().containsValue(p.getUniqueId())) {
					p.sendMessage("§cSomeone already requested to trade with you.");
					return false;
				}
				if(TradeManager.getTradeReq().containsValue(to.getUniqueId()) || TradeManager.getTradeReq().containsKey(to.getUniqueId())) {
					p.sendMessage("§cThat player has an active request or is currently trading.");
					return false;
				}
				TradeManager.getTradeReq().put(p.getUniqueId(), to.getUniqueId());
				p.sendMessage("§aRequest sent to §f" + to.getName() + "§a.");
				to.sendMessage(p.getName() + "§6 has requested to trade with you.");
				to.sendMessage("§6Do §c/trade accept " + p.getName() + " §6to accept the request or §c/trade deny " + p.getName() + " §6to decline the request.");
			} else if(args.length == 2) { // trade accept <name>
				if(args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("deny")) {
					if(TradeManager.getTradeReq().containsValue(p.getUniqueId())) {
						Player requester = Bukkit.getPlayer(args[1]);
						if(requester == null) {
							p.sendMessage("§cSorry, but that player cannot be found!");
							return false;
						}
						if(args[0].equalsIgnoreCase("deny")) {
							TradeManager.getTradeReq().remove(requester.getUniqueId());
							requester.sendMessage("§c" + p.getName() + " has declined your trade request.");
							p.sendMessage("§aYou declined " + requester.getName() + "'s trade request.");
						} else { //accept
							TradeManager.trade(requester, p);
						}
					} else {
						p.sendMessage("§cNobody requested to trade with you.");
					}
				} else {
					p.sendMessage("§cUsage: /trade [accept/deny] [player name]");
				}
			} else {
				p.sendMessage("§cUsage: /trade [player name] or /trade [accept/deny] [player name]");
			}
		} else if(command.getName().equalsIgnoreCase("setpvpspawn")) {
			if(p.hasPermission("sfa.setspawns")) {
				storageData.set("pvpspawn", p.getLocation());
				saveStorage();
				p.sendMessage("§aYou have successfully set a new PVP spawn point.");
			} else {
				p.sendMessage("§cYou do not have permission to perform this command!");
			}
		} else if(command.getName().equalsIgnoreCase("setKillLb")) {
			if(p.hasPermission("sfa.setleaderboards")) {
				LeaderboardHandler.deleteLeaderboards();
				storageData.set("killLb", p.getLocation()); 
				saveStorage();
				p.sendMessage("§aYou have successfully set a new location for the Kills Leaderboard.");
				LeaderboardHandler.createLeaderboards(storageData);
			} else {
				p.sendMessage("§cYou do not have permission to perform this command!");
			}
		} else if(command.getName().equalsIgnoreCase("setDeathLb")) {
			if(p.hasPermission("sfa.setleaderboards")) {
				LeaderboardHandler.deleteLeaderboards();
				storageData.set("deathLb", p.getLocation()); 
				saveStorage();
				p.sendMessage("§aYou have successfully set a new location for the Deaths Leaderboard.");
				LeaderboardHandler.createLeaderboards(storageData);
			} else {
				p.sendMessage("§cYou do not have permission to perform this command!");
			}
		} else if(command.getName().equalsIgnoreCase("ores")) {
			OreManager.openOresMenu(p);
		} else if(command.getName().equalsIgnoreCase("tags")) {
			if(args.length == 0) {
				if(!(sender instanceof Player))	
					sender.sendMessage("[SFA] You must be a player to do this!");
				else
					TagManager.openMenu(p, 0);
			} else {
				if(args[0].equalsIgnoreCase("create")) {
					if(args.length < 3) {
						sender.sendMessage("§cUsage: /tags create [identifier] [tag]");
					} else {
						if(!sender.hasPermission("sfa.tagsadmin")) {
							sender.sendMessage("§cYou do not have permission to perform this command!");
							return false;
						}
						if(TagManager.exists(args[1])) {
							sender.sendMessage("§cA tag by this identifier already exists!");
							return false;
						}

						if(!StringUtils.isAlphanumeric(args[1])) {
							sender.sendMessage("§cThe identifier has to be alphanumeric!");
							return false;
						}
						TagManager.create(args[1], String.join(" ", ArrayUtils.subarray(args, 2, args.length)));
						sender.sendMessage("§aYou have created a new tag called §6" + args[1] + "§a.");
					}
				} else if(args[0].equalsIgnoreCase("delete")) {
					if(!sender.hasPermission("sfa.tagsadmin")) {
						sender.sendMessage("§cYou do not have permission to perform this command!");
						return false;
					}
					if(args.length != 2) {
						sender.sendMessage("§cUsage: /tags delete [identifier]");
					} else {
						if(!TagManager.exists(args[1])) {
							sender.sendMessage("§cA tag by this identifier does not exist!");
							return false;
						}
						TagManager.delete(args[1]);
						sender.sendMessage("§aYou have deleted a tag called §6" + args[1] + "§a.");
					}
				} else if(args[0].equalsIgnoreCase("give")) {
					if(!sender.hasPermission("sfa.tagsadmin")) {
						sender.sendMessage("§cYou do not have permission to perform this command!");
						return false;
					}
					if(args.length != 3) {
						sender.sendMessage("§cUsage: /tags give [player name] [identifier]");
					} else {
						if(!TagManager.exists(args[2])) {
							sender.sendMessage("§cA tag by this identifier does not exist!");
							return false;
						}
						OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
						if(!target.hasPlayedBefore()) {
							sender.sendMessage("§cSorry, but that player cannot be found!");
							return false;
						}
						TagManager.giveTag(args[2], args[1]);
						sender.sendMessage("§aYou gave a tag called §6" + args[2] + " §ato " + args[1] + ".");
					}
				}
			}
		} else if(command.getName().equalsIgnoreCase("repair")) {
			if(!p.hasPermission("sfa.repair.5")) {
				sender.sendMessage("§cYou do not have permission to perform this command!");
				return false;
			}
			if(RepairManager.canRepair(p)) {
				ItemStack curr = p.getItemInHand();
				if(curr.getDurability() > (short)0) {
					curr.setDurability((short)0);
					p.sendMessage("§aYou repaired the current item in your hand.");
					RepairManager.addRepair(p);
				} else {
					p.sendMessage("§cThis item is either already fully repaired or not repairable.");
				}

			} else {
				p.sendMessage("§cYou already exceeded the maximum number of repairs. Purchase a rank to increase the limit or wait for one week.");
			}
		} else if(command.getName().equalsIgnoreCase("giveSellChest")) {
			if(!sender.hasPermission("sfa.givesellchest")) {
				sender.sendMessage("§cYou do not have permission to perform this command!");
				return false;
			}
			if(args.length != 2) {
				sender.sendMessage("§cUsage: /givesellchest [player] [amount]");
				return false;
			}
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				sender.sendMessage("§cSorry, but that player cannot be found!");
				return false;
			}
			if(!StringUtils.isNumeric(args[1]) || Integer.parseInt(args[1]) > 64) {
				sender.sendMessage("§cThe amount has to be numeric and at most 64.");
				return false;
			}
			SellChestManager.giveItem(target, Integer.parseInt(args[1]));
		} else if(command.getName().equalsIgnoreCase("season") || command.getName().equalsIgnoreCase("seasons")) {
			if(args.length == 0)
				SeasonManager.openMenu(p);
			else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("reload")) {
					if(p.hasPermission("sfa.season.reload")) {
						SeasonManager.refreshRewards();
						p.sendMessage("§aSeason rewards reloaded!");
					} else {
						p.sendMessage("§cYou do not have permission to perform this command.");
					}
				}
			}
		}
		return false;
	}

	private Inventory buildMenu() {
		Inventory i = Bukkit.createInventory(null, 45, "Menu");
		ItemStack item = new ItemStack(Material.SLIME_BALL);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName("§aCosmetics");
		im.setLore(Arrays.asList("§7Click here to purchase cosmetics."));
		item.setItemMeta(im);
		i.setItem(10, item);

		item = new ItemStack(Material.IRON_SWORD);
		im = item.getItemMeta();
		im.setDisplayName("§aPerks");
		im.setLore(Arrays.asList("§7Click here to purchase skyblock perks."));
		item.setItemMeta(im);
		i.setItem(12, item);

		item = new ItemStack(Material.ENDER_PORTAL_FRAME);
		im = item.getItemMeta();
		im.setDisplayName("§aOre Generators");
		im.setLore(Arrays.asList("§7Click here to purchase ore generators."));
		item.setItemMeta(im);
		i.setItem(14, item);

		item = new ItemStack(Material.EMERALD);
		im = item.getItemMeta();
		im.setDisplayName("§aShop");
		im.setLore(Arrays.asList("§7Click here to purchase other items."));
		item.setItemMeta(im);
		i.setItem(16, item);
		
		item = new ItemStack(Material.STORAGE_MINECART);
		im = item.getItemMeta();
		im.setDisplayName("§aRewards");
		im.setLore(Arrays.asList("§7Click here to open the rewards menu."));
		item.setItemMeta(im);
		i.setItem(29, item);
		
		item = new ItemStack(Material.ENDER_CHEST);
		im = item.getItemMeta();
		im.setDisplayName("§aCrates");
		im.setLore(Arrays.asList("§7Click here to open the crates menu"));
		item.setItemMeta(im);
		i.setItem(31, item);
		
		item = new ItemStack(Material.CHEST);
		im = item.getItemMeta();
		im.setDisplayName("§aKits");
		im.setLore(Arrays.asList("§7Click here to open the kits menu"));
		item.setItemMeta(im);
		i.setItem(33, item);
		
		return i;
	}

	private void teleportCoolDown(Location loc, Player p, int sec) {
		if(p.hasPermission("sfa.bypassteleportcooldown")) {
			p.teleport(loc);
			return;
		}
		if(runningSomeTPCmd.contains(p.getUniqueId())) {
			p.sendMessage("§cWait for the command to finish executing!");
			return;
		}
		p.sendMessage("§aTeleporting you in " + sec + " seconds. §cDon't move!");
		runningSomeTPCmd.add(p.getUniqueId());
		new BukkitRunnable() {
			int time = sec;
			@Override
			public void run() {
				if(!runningSomeTPCmd.contains(p.getUniqueId())) {
					p.sendMessage("§cTeleportation has been canceled by your movement!");
					this.cancel();
				}
				if(time == 0) {
					this.cancel();
					p.teleport(loc);
					runningSomeTPCmd.remove(p.getUniqueId());
				}
				time--;
			}
		}.runTaskTimer(this, 0, 20);
	}

	private void registerThings() {
		ConfigurationSerialization.registerClass(Booster.class, "Booster");
		ConfigurationSerialization.registerClass(Kit.class, "Kit");
		ConfigurationSerialization.registerClass(Generator.class, "Generator");
		ConfigurationSerialization.registerClass(Home.class, "Home");
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new ChatHandling(this), this);
		pm.registerEvents(new BouncePadInteract(), this);
		pm.registerEvents(new BouncePadPlace(), this);
		pm.registerEvents(new BouncePadBreak(), this);
		pm.registerEvents(new SkullEquipListeners(), this);
		pm.registerEvents(new SkullAbility(), this);
		pm.registerEvents(new EventsCancelInSpawn(), this);
		pm.registerEvents(new PlayerJoin(), this);
		pm.registerEvents(new BanknoteListener(), this);
		pm.registerEvents(new BoosterClaim(), this);
		pm.registerEvents(new BoosterActivate(), this);
		pm.registerEvents(new PlayerKillEntity(), this);
		pm.registerEvents(new PlayerMove(), this);
		pm.registerEvents(new KitSelection(), this);
		pm.registerEvents(new InvseeEdit(), this);
		pm.registerEvents(new PlayerRespawn(), this);
		pm.registerEvents(new GeneratorListeners(), this);
		pm.registerEvents(new MenuListener(), this);
		pm.registerEvents(new PerkListeners(), this);
		pm.registerEvents(new RewardListener(), this);
		pm.registerEvents(new KitPreview(), this);
		pm.registerEvents(new StatsListener(), this);
		pm.registerEvents(new OreGenListener(), this);
		pm.registerEvents(new TradeListener(), this);
		pm.registerEvents(new OreManager(), this);
		pm.registerEvents(new TagsMenuListener(), this);
		pm.registerEvents(new PlayerDeath(), this);
		pm.registerEvents(new SellChestListener(), this);
		pm.registerEvents(new SeasonMenuListener(), this);
		pm.registerEvents(new CosmeticMenuListener(), this);
		pm.registerEvents(new CosmeticListener(), this);
		pm.addPermission(new Permission("sfa.giveBouncePad"));
		pm.addPermission(new Permission("sfa.editspawn"));
		pm.addPermission(new Permission("sfa.addBal"));
		pm.addPermission(new Permission("sfa.setBal"));
		pm.addPermission(new Permission("sfa.givebooster"));
		pm.addPermission(new Permission("sfa.createKit"));
		pm.addPermission(new Permission("sfa.invsee"));
		pm.addPermission(new Permission("sfa.invsee.edit"));
		pm.addPermission(new Permission("sfa.givesellwand"));
		pm.addPermission(new Permission("sfa.givegen"));
		pm.addPermission(new Permission("sfa.sethomemultiple2"));
		pm.addPermission(new Permission("sfa.sethomemultiple4"));
		pm.addPermission(new Permission("sfa.sethomemultiple6"));
		pm.addPermission(new Permission("sfa.sethomemultiple8"));
		pm.addPermission(new Permission("sfa.setspawns"));
		pm.addPermission(new Permission("sfa.clearchat"));
		pm.addPermission(new Permission("sfa.setleaderboards"));
		pm.addPermission(new Permission("sfa.rewards.elite"));
		pm.addPermission(new Permission("sfa.rewards.master"));
		pm.addPermission(new Permission("sfa.rewards.legend"));
		pm.addPermission(new Permission("sfa.rewards.mystic"));
		pm.addPermission(new Permission("sfa.banknote.give"));
		pm.addPermission(new Permission("sfa.tagsadmin"));
		pm.addPermission(new Permission("sfa.bypassteleportcooldown"));
		pm.addPermission(new Permission("sfa.repair.5"));
		pm.addPermission(new Permission("sfa.repair.10"));
		pm.addPermission(new Permission("sfa.repair.15"));
		pm.addPermission(new Permission("sfa.repair.unlimited"));
		pm.addPermission(new Permission("sfa.keepexp"));
		pm.addPermission(new Permission("sfa.givesellchest"));
		pm.addPermission(new Permission("sfa.season.reload"));
	}

	public YamlConfiguration getBpData() {
		return bpData;
	}

	public void saveStorage() {
		try {
			storageData.save(storage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveBP() {
		try {
			bpData.save(bpFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SEconomy getSEconomy() {
		return sEco;
	}

	public Location getSpawn() {
		return (Location)storageData.get("spawn");
	}

	public List<UUID> getWhoRunningSomeTPCmd(){
		return runningSomeTPCmd;
	}

	public List<UUID> getViewingInv(){
		return viewingInv;
	}

	public Location getPvPSpawn() {
		return (Location)storageData.get("pvpspawn");
	}


}
