package com.pxpmc.choujiang.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 配置文件工厂接口
 * @author xpx
 *
 */
public abstract class ConfigManager
{
	
	/**
	 * 读取一个文件
	 * @param path 读取的文件名.如果有子目录则也需要添加如: 插件名/"子目录/文件名"(不需要后缀名,默认yml)
	 */
	public abstract void loadFile(String path);
	/**
	 * 获取一个配置
	 * @param name 之前设置的名字
	 * @param create 是否创建新的文件,如果=true,未找到则创建文件,如果=false,则尝试从jar内输出文件 ,默认为 false
	 * @return 如果没有返回null
	 * @throws IOException 
	 */
	public abstract Config get(String name);
	/**
	 * 获取一个配置
	 * @param name 之前设置的名字
	 * @param create 是否创建新的文件,如果=true,未找到则创建文件,如果=false,则尝试从jar内输出文件 ,默认为 false
	 * @return 如果没有返回null
	 * @throws IOException 
	 */
	public abstract Config get(String name,boolean create) throws IOException;

	/**
	 * 获取所有读取的文件
	 * @return
	 */
	public abstract Map<String, Config> getConfigs();
	/**
	 * 取主要的配置文件
	 * @return
	 */
	public abstract Config getMainConfig();
	/**
	 * 取语言文件
	 * @return
	 */
	public abstract Config getLang();
	/**
	 * 加载语言文件
	 */
	public abstract void loadLang();
	/**
	 * 加载配置文件
	 */
	public abstract void loadConfig();
	/**
	 * 根据文件创建一个config
	 * @param file
	 * @return
	 */
	public abstract Config createConfig(File file);
	/**
	 * 根据路径创建一个config
	 * @param file
	 * @return
	 */
	public abstract Config createConfig(String path);
	/**
	 * 创建一个新的文件
	 * @param save
	 * @throws IOException 
	 */
	public abstract void createFile(String save) throws IOException;
}
