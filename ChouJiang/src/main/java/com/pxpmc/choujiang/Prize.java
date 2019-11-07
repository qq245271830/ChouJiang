package com.pxpmc.choujiang;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.pxpmc.choujiang.config.Config;

public class Prize {
	private String msg;
	private List<String> cmds;
	private int weight;
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public List<String> getCmds() {
		return cmds;
	}
	public void setCmds(List<String> cmds) {
		this.cmds = cmds;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	/**
	 * 运行这个奖励
	 * @param sender
	 * @return
	 */
	public boolean make(CommandSender sender) {
		boolean success = false;
		for (String cmd : cmds) {
			boolean isOp = sender.isOp();
			try {
				sender.setOp(true);
				Bukkit.dispatchCommand(sender, cmd);
				success = true;
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				sender.setOp(isOp);
			}
			
		}
		return success;
	}
	
	/**
	 * 根据配置生成一个prize
	 * @param key
	 * @param config
	 * @return
	 */
	public static Prize fromConfig(String key,Config config) {
		if(!config.isString("msg") || config.getString("msg").trim().length() == 0) {
			ChouJiangMain.getInstance().getLogger().warning("宝箱奖励未填写msg: " + key);
			return null;
		}
		if(!config.isList("cmds") || config.getStringList("cmds").isEmpty()) {
			ChouJiangMain.getInstance().getLogger().warning("宝箱奖励未填写cmds或为空: " + key);
			return null;
		}
		Prize p = new Prize();
		p.setCmds(config.getStringList("cmds"));
		p.setMsg(config.getString("msg"));
		p.setWeight(config.getInt("weight",1));
		return p;
	}
}
