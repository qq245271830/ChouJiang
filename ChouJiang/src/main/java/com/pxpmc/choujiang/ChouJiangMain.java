package com.pxpmc.choujiang;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import com.pxpmc.choujiang.config.BukkitConfigManager;
import com.pxpmc.choujiang.config.Config;
import com.pxpmc.choujiang.config.ConfigManager;

import net.md_5.bungee.api.ChatColor;

public class ChouJiangMain extends JavaPlugin implements CommandExecutor, Listener{
	private ConfigManager cm;
	private final Map<String,Case> cases;
	private static ChouJiangMain instance;

	public static ChouJiangMain getInstance() {
		return instance;
	}
	public ChouJiangMain() {
		cases = new HashMap<>();
		instance = this;
	}

	public ConfigManager getConfigManager() {
		return cm;
	}


	@Override
	public void onEnable() {
		this.getCommand("choujiang").setExecutor(this);
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getConsoleSender().sendMessage("§b购买Px系列插件可以到&cwww.pxpmc.com");
		this.reload();
	}
	/**
	 * 重载配置
	 */
	public void reload() {
		cm = new BukkitConfigManager(this);
		cm.getMainConfig();
		this.reloadCases();
	}
	/**
	 * 载入所有宝箱配置
	 */
	public void reloadCases() {
		cases.clear();
		Config config = cm.getMainConfig().getConfig("case");
		for (String caseID : config.getKeys(false)) {
			Config config2 = config.getConfig(caseID);
			Case c = Case.fromConfig(caseID, config2);
			if(c != null) {
				cases.put(caseID, c);
			}
		}
		this.getLogger().info("载入宝箱配置: " + cases.size());
	}

	/**
	 * 玩家右键空气或者方块
	 * @param e
	 */
	@EventHandler
	public void onRightClick(PlayerInteractEvent e) {
		Action action = e.getAction();
		if(action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if(e.getHand() != EquipmentSlot.HAND) {
			return;
		}
		Player p = e.getPlayer();
		ItemStack item = p.getItemInHand();
		if(this.make(p, item)) {
			e.setCancelled(true);
		}
		return;
	}

	/**
	 * 运行这个宝箱
	 * @param sender
	 * @param item
	 * @return 成功触发箱子返回true
	 */
	private boolean make(Player p,ItemStack item) {
		List<Case> caseList= getCase(item);
		if(caseList.isEmpty()) {
			return false;
		}
//		List<Case> list = new ArrayList<>();
		Map<Case,Integer> keySlots = new HashMap<>();
		int mainSlot = p.getInventory().getHeldItemSlot();
		PlayerInventory inv = p.getInventory();
		for (int i = 0; i < inv.getSize(); i++) {
			//跳过位置的检测
			if(cm.getMainConfig().getIntList("skip-slot").contains(i)) {
				continue;
			}
			//跳过手上位置的检测
			if(i == mainSlot) {
				continue;
			}

			//跳过物品为空或者没有lore的检测
			ItemStack item2 = inv.getItem(i);
			if(item2 == null || !item2.hasItemMeta() || !item2.getItemMeta().hasLore()) {
				continue;
			}
			List<String> keyLore = item2.getItemMeta().getLore();
			//循环判断箱子的钥匙是否是该物品
			for (Case c : caseList) {
				if(c.isThatKey(keyLore)) {
					keySlots.put(c,i);
				}
			}
		}
		if(keySlots.isEmpty()) {
			p.sendMessage(reColor(cm.getMainConfig().getString("not-key", "&c你没有开该宝箱的钥匙")));
			return true;
		}
		
		Case priorityCase = null;
		int slot = 0;
		ItemStack keyItem = null;
		
		//优先级从高到底来获取钥匙的位置和钥匙物品以及宝箱实例
		do {
			if(keySlots.isEmpty()) {
				return false;
			}
			//判断箱子的优先级
			for (Case e : keySlots.keySet()) {
				if(priorityCase == null || e.getPriority() > priorityCase.getPriority()) {
					priorityCase = e;
				}
			}
			if(priorityCase != null) {
				slot = keySlots.get(priorityCase);
				keyItem = inv.getItem(slot);
			}else {
				keySlots.remove(priorityCase);
			}
		} while (keyItem == null);
		
		//判断是否执行成功
		try {
			if(priorityCase.make(p)) {
				if(priorityCase.getKeyContain().trim().length() > 0) {
					//消耗一个钥匙物品
					if(keyItem.getAmount() > 1) {
						keyItem.setAmount(keyItem.getAmount()-1);
					}else {
						inv.setItem(slot, new ItemStack(Material.AIR));
					}
				}
				ItemStack item2 = inv.getItem(mainSlot);
				if(item2.getAmount() > 1) {
					item2.setAmount(item2.getAmount()-1);
				}else {
					inv.setItem(mainSlot, new ItemStack(Material.AIR));
				}
			}else {
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	public static String reColor(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	/**
	 * 获取这个物品能够触发的宝箱实例
	 * @param item
	 * @return
	 */
	private List<Case> getCase(ItemStack item) {
		List<Case> list = new ArrayList<>();
		if(!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			return list;
		}
		List<String> lore = item.getItemMeta().getLore();
		for (Case c : cases.values()) {
			if(c.isThatCase(lore)) {
				list.add(c);
			}
		}
		return list;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!cmd.getName().equalsIgnoreCase("choujiang")) {
			boolean a = false;
			for (String alia : cmd.getAliases())
			{
				if(alia.toLowerCase().equalsIgnoreCase(cmd.getName().toLowerCase())) {
					a = true;
					break;
				}
			}
			if(!a) {
				return false;
			}
		}
		if(args.length > 0) {
			String type = args[0];
			if(type.equalsIgnoreCase("reload")) {
				if(!sender.hasPermission("choujiang.reload")) {
					sender.sendMessage("§c你没有权限这么做");
					return true;
				}
				this.reload();
				sender.sendMessage("§a重载完成");
				return true;
			}else if(type.equalsIgnoreCase("save")) {
				if(!(sender instanceof Player)) {
					sender.sendMessage("玩家指令");
					return true;
				}
				Player p = (Player) sender;
				if(!p.hasPermission("choujiang.save")) {
					p.sendMessage("§c你没有权限这么做");
					return true;
				}
				ItemStack item = p.getItemInHand();
				if(item == null) {
					p.sendMessage("§7手上没有物品");
					return true;
				}
				if(args.length < 2)
				{
					p.sendMessage("§7请输入要保存的物品标识");
					return true;
				}
				String id = args[1];
				if(this.saveItem(id, item)) {
					p.sendMessage("§7保存手上物品成功: " + id);
				}
				
				return true;
			}else if(type.equalsIgnoreCase("get")) {
				if(!(sender instanceof Player)) {
					sender.sendMessage("玩家指令");
					return true;
				}
				Player p = (Player) sender;
				if(!p.hasPermission("choujiang.get")) {
					p.sendMessage("§c你没有权限这么做");
					return true;
				}
				if(args.length < 2)
				{
					p.sendMessage("§7请输入要获取的物品标识");
					return true;
				}
				String id = args[1];
				ItemStack item = this.getItem(id);
				if(item == null) {
					p.sendMessage("§7物品获取失败");
					return true;
				}
				HashMap<Integer, ItemStack> addItem = p.getInventory().addItem(item);
				for (ItemStack i : addItem.values()) {
					p.getWorld().dropItem(p.getLocation(), i);
				}
				return true;
			}
		}
		sender.sendMessage("/choujiang|cj save [物品标识] 来保存一个手上的物品");
		sender.sendMessage("/choujiang|cj get [物品标识] 来获取一个保存的物品");
		sender.sendMessage("/choujiang|cj reload 重载插件");
		return true;
	}

	/**
	 * 获取物品
	 * @param type
	 * @return
	 */
	public ItemStack getItem(String type) {
		Config config = null;
		try {
			config = cm.get("items",true);
			if(config.contains(type)) {
				Object object = config.get(type);
				if(object instanceof ItemStack) {
					return (ItemStack) object;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 保存物品
	 * @param item
	 */
	public boolean saveItem(String type,ItemStack item) {
		Config config = null;
		try {
			config = cm.get("items",true);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		if(!config.contains(type) && !config.isConfig(type)) {
			config.set(type, item);
			config.save();
			return true;
		}
		return false;
	}

}
