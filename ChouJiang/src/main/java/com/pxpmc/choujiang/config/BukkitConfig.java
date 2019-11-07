package com.pxpmc.choujiang.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * bukkit的配置文件类
 * @author px
 */
public class BukkitConfig extends Config
{

	//bukkit的配置
	private final ConfigurationSection configurationSection;

	public BukkitConfig(ConfigurationSection configurationSection) {
		super();
		this.configurationSection = configurationSection;
		this.setObject(configurationSection);
	}

	/**
	 * 获取bukkit配置
	 * @return
	 */
	public ConfigurationSection getConfigurationSection() {
		return configurationSection;
	}

	@Override
	public void save() {
		save(this.getFile());
	}

	@Override
	public void save(File file) {
		//检查是否有父类
		if(this.getParent() == null) {
			if(configurationSection instanceof FileConfiguration) {
				FileConfiguration iFileConfiguration = (FileConfiguration)configurationSection;
				try {
					iFileConfiguration.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
			}
		}else {
			this.getParent().save(file);
		}
	}

	@Override
	public String getString(String path) {
		return configurationSection.getString(path);
	}

	@Override
	public String getString(String path, String def) {
		return configurationSection.getString(path,def);
	}


	@Override
	public int getInt(String path) {
		return configurationSection.getInt(path);
	}
	@Override
	public int getInt(String path, int def) {
		return configurationSection.getInt(path, def);
	}

	@Override
	public boolean getBoolean(String path, boolean def) {
		return configurationSection.getBoolean(path,def);
	}

	@Override
	public double getDouble(String path, double def) {
		return configurationSection.getDouble(path,def);
	}

	@Override
	public void set(String path, Object txt) {
		//		configurationSection./
		configurationSection.set(path, txt);
	}
	@Override
	public Config getConfig(String path) {
		ConfigurationSection cs = configurationSection.getConfigurationSection(path);
		if(cs == null) {
			cs = configurationSection.createSection(path);
		}
		
		BukkitConfig bukkitConfig = new BukkitConfig(cs);
		bukkitConfig.setFile(this.getFile());
		bukkitConfig.setParent(this);
		return bukkitConfig;
	}

	@Override
	public boolean contains(String sub) {
		return configurationSection.contains(sub);
	}

	@Override
	public boolean contains(String sub, boolean paramBoolean) {
		return configurationSection.contains(sub, paramBoolean);
	}
	@Override
	public Set<String> getKeys(boolean paramBoolean) {
		return configurationSection.getKeys(paramBoolean);
	}
	@Override
	public void set(String path, List<String> list) {
		configurationSection.set(path, list);
	}

	@Override
	public boolean isString(String sub) {
		return configurationSection.isString(sub);
	}

	@Override
	public boolean isConfig(String sub) {
		return configurationSection.isConfigurationSection(sub);
	}

	@Override
	public Object get(String string) {
		return configurationSection.get(string);
	}


	@Override
	public boolean isInt(String sub) {
		return configurationSection.isInt(sub);
	}

	@Override
	public boolean isBoolean(String string) {
		return configurationSection.isBoolean(string);
	}

	@Override
	public List<Integer> getIntList(String path) {
		return configurationSection.getIntegerList(path);
	}

	@Override
	public boolean isList(String path) {
		return configurationSection.isList(path) || this.isString(path);
	}

	@Override
	public List<String> getStringList(String path) {
		List<String> ret = new ArrayList<>();
		if(this.isString(path)) {
			String a = this.configurationSection.getString(path);
			String[] split = a.split(",");
			for (int i = 0; i < split.length; i++) {
				String string = split[i];
				ret.add(string);
			}
		}else if(configurationSection.isList(path)){
			ret = configurationSection.getStringList(path);
		}


		return ret;
	}

	@Override
	public long getLong(String string, long def) {
		return configurationSection.getLong(string,def);
	}

	@Override
	public long getLong(String string) {
		return configurationSection.getLong(string);
	}

	@Override
	public String toString() {
		return "BukkitConfig [configurationSection=" + configurationSection + "]";
	}


	@Override
	public List<String> getStringList(String path, List<String> def) {
		if(this.isList(path)) {
			return configurationSection.getStringList(path);
		}
		return def;
	}

	@Override
	public List<Integer> getIntList(String path, List<Integer> def) {
		if(this.isList(path)) {
			return configurationSection.getIntegerList(path);
		}
		return def;
	}

	@Override
	public boolean isDouble(String path) {
		return configurationSection.isDouble(path);
	}

	@Override
	public boolean isLong(String path) {
		return configurationSection.isLong(path);
	}
	@Override
	public boolean isItemStack(String path1) {
		String path = null;
		if(path1 == null || path1.length() == 0) {
			path = "";
		}else {
			path = path1+".";
		}
		return this.contains(path+"type");
	}

	@Override
	public String getCurrentPath() {
		return configurationSection.getCurrentPath();
	}


}
