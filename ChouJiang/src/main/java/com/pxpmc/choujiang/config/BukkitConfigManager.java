package com.pxpmc.choujiang.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
/**
 * bukkit的配置管理器
 * @author Administrator
 *
 */
public class BukkitConfigManager extends ConfigManager
{
	public static final String CONFIG = "config";
	public static final String LANG = "lang";
	public static final String EXT = ".yml";
	private final Map<String,Config> configs = new HashMap<String, Config>();
	private final Plugin plugin;
	public BukkitConfigManager(Plugin plugin)
	{
		this.plugin= plugin;
	}
	@Override
	public void loadLang()
	{
		loadFile(LANG);
	}
	@Override
	public void loadConfig()
	{
		loadFile(CONFIG);
	}
	@Override
	public void createFile(String save) throws IOException
	{
		File file = new File(plugin.getDataFolder(),save + EXT);
		File path = file.getParentFile();
//		System.out.println(path.getPath() + " = " + file.getPath()+": " + path.isDirectory() + " = " + path.exists());
		if(!path.exists()) {
			path.mkdirs();
			
		}
		if(!file.exists())
		{
			file.createNewFile();
		}
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		BukkitConfig bc = new BukkitConfig(yaml);
		bc.setFile(file);
		bc.setObject(yaml);
		configs.put(save, bc);
	}
	@Override
	public void loadFile(String save)
	{
		File file = new File(plugin.getDataFolder(),save + EXT);
		if(!file.exists())
		{
			plugin.saveResource(save+ EXT, true);
			file =new File(file.getPath());
		}
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		BukkitConfig bc = new BukkitConfig(yaml);
		bc.setFile(file);
		bc.setObject(yaml);
		configs.put(save, bc);
	}
	/**
	 * 获取所有读取的文件
	 * @return
	 */
	public Map<String, Config> getConfigs() 
	{
		return configs;
	}
	/**
	 * 取默认Config配置
	 * @return 如果没有返回null
	 */
	public Config getConfig()
	{
		return get(CONFIG);
	}
	@Override
	public Config get(String name)
	{
		try {
			return this.get(name, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public Config get(String name,boolean create) throws IOException
	{
		if(!configs.containsKey(name))
		{
			if(create) {
				createFile(name);
			}else {
				loadFile(name);
			}
		}
		return configs.get(name);
	}
	public static String reColor(String s)
	{
		return s.replaceAll("&", "§");
	}
	public static List<String> reColor(List<String> list)
	{
		List<String> a = new ArrayList<String>();
		for (String s : list) {
			a.add(reColor(s));
		}
		return a;
	}

	@Override
	public Config getMainConfig() {
		return this.getConfig();
	}
	@Override
	public Config getLang() 
	{
		return get(LANG);
	}
	@Override
	public Config createConfig(File file) {
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
		BukkitConfig config = new BukkitConfig(yaml);
		config.setFile(file);
		config.setObject(yaml);
		//		config.setConfigurationSection(yaml);
		return config;
	}
	@Override
	public Config createConfig(String path) {
		return this.createConfig(new File(path));
	}
}
