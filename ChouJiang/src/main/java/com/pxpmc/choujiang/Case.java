package com.pxpmc.choujiang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.pxpmc.choujiang.config.Config;

public class Case {
	private final Map<String,Prize> prize;
	private String loreContain;
	private String keyContain;
	private int priority;
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((keyContain == null) ? 0 : keyContain.hashCode());
		result = prime * result + ((loreContain == null) ? 0 : loreContain.hashCode());
		result = prime * result + priority;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Case other = (Case) obj;
		if (keyContain == null) {
			if (other.keyContain != null)
				return false;
		} else if (!keyContain.equals(other.keyContain))
			return false;
		if (loreContain == null) {
			if (other.loreContain != null)
				return false;
		} else if (!loreContain.equals(other.loreContain))
			return false;
		if (priority != other.priority)
			return false;
		return true;
	}
	public Case() {
		this.prize = new HashMap<>();
	}
	/**
	 * 是否是该类型的宝箱
	 * @param lore
	 * @return
	 */
	public boolean isThatCase(List<String> lore) {
		for (String s : lore) {
			if(s.contains(loreContain)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 是否是该类型的宝箱
	 * @param lore
	 * @return
	 */
	public boolean isThatKey(List<String> lore) {
		for (String s : lore) {
			if(s.contains(keyContain)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 取随机数
	 * @param min 最小值,默认为0
	 * @param max 最大值
	 * @return 随机抽取的一个值
	 */
	public static Integer nextInt(final Integer max)
	{
		return nextInt(0,max);
	}
	public String getLoreContain() {
		return loreContain;
	}
	public void setLoreContain(String loreContain) {
		this.loreContain = loreContain;
	}
	public String getKeyContain() {
		return keyContain;
	}
	public void setKeyContain(String keyContain) {
		this.keyContain = keyContain;
	}
	public Map<String, Prize> getPrize() {
		return prize;
	}
	/**
	 * 取随机数
	 * @param min 最小值,默认为0
	 * @param max 最大值
	 * @return 随机抽取的一个值
	 */
	public static Integer nextInt(final Integer min, final Integer max)
	{
		if(min > max){
			return max;
		}
		if(min == max){
			return min;
		}
		return new Random().nextInt(max - min + 1) + min;
	}
	/**
	 * 随机抽取其中一个位置,根据list内的权重
	 * @return 返回index的位置
	 */
	public static int randomAmount(List<Integer> list) {
		int max = 0;
		for (Integer integer : list) {
			max += integer;
		}
		int random = nextInt(1,max);
		int temp = 0;
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			int loc = list.get(i);
			temp += loc;
			if(loc == 0) {
				continue;
			}
			index = i;
			if(random <= temp) {
				break;
			}
		}
		return index;
	}
	/**
	 * 
	 * 运行这个宝箱
	 * @param sender
	 * @return 失败返回false
	 */
	public boolean make(CommandSender sender) {
		List<Prize> prizes = new ArrayList<>();
		List<Integer> weights = new ArrayList<>();
		for (Prize p : prize.values()) {
			prizes.add(p);
			weights.add(p.getWeight());
		}
		if(weights.isEmpty()) {
			sender.sendMessage("奖池为空");
			return false;
		}
		int index = randomAmount(weights);
		
		Prize p = prizes.get(index);
		if(p.make(sender)) {
			String msg = ChouJiangMain.getInstance().getConfigManager().getMainConfig().getString("msg", "恭喜{player}抽到了{msg}")
					.replace("{player}",sender.getName())
					.replace("{msg}",p.getMsg())
					;
			Bukkit.broadcastMessage(ChouJiangMain.reColor(msg));
			return true;
		}
		return false;
	}
	
	
	/**
	 * 根据配置获取这个宝箱配置
	 * @param key
	 * @param config
	 * @return 失败返回null
	 */
	public static Case fromConfig(String key,Config config) {
		if(!config.contains("lore-contain") || !config.isString("lore-contain") || config.getString("lore-contain").trim().length() == 0) {
			ChouJiangMain.getInstance().getLogger().warning("宝箱lore包含的字符未填写: " + key);
			return null;
		}
		if(!config.contains("key-contain") || !config.isString("key-contain") || config.getString("key-contain").trim().length() == 0) {
			ChouJiangMain.getInstance().getLogger().warning("宝箱钥匙包含的字符未填写: " + key);
			return null;
		}
		if(!config.isConfig("prize")) {
			ChouJiangMain.getInstance().getLogger().warning("宝箱奖品未填写: " + key);
			return null;
		}
		Case c = new Case();
		c.setKeyContain(config.getString("key-contain"));
		c.setLoreContain(config.getString("lore-contain"));
		Config prizes = config.getConfig("prize");
		for (String prizeKey : prizes.getKeys(false)) {
			Config prizeConfig = prizes.getConfig(prizeKey);
			Prize prize = Prize.fromConfig(prizeKey, prizeConfig);
			if(prize != null) {
				c.prize.put(prizeKey, prize);
			}
		}
		
		return c;
	}
}
