package com.pxpmc.choujiang.config;

import java.util.List;
import java.util.Set;

/**
 * 文件获取者
 * @author px
 * 2019年10月22日23:14:45
 */
public interface ConfigGetter
{
	/**
	 * 取文本
	 * @param path 路径
	 * @param def 默认返回
	 * @return
	 */
	String getString(String path,String def);
	/**
	 * 取字符串列表
	 * @param path 路径
	 * @param def 默认列表
	 * @return 失败返回默认列表
	 */
	List<String> getStringList(String path,List<String> def);
	/**
	 * 取int列表
	 * @param path 路径
	 * @param def 默认列表
	 * @return 失败返回默认列表
	 */
	List<Integer> getIntList(String path,List<Integer> def);
	/**
	 * 取int类型
	 * @param path 路径
	 * @param def 默认
	 * @return 失败返回默认列表
	 */
	int getInt(String path,int def);
	/**
	 * 取boolean类型
	 * @param path 路径
	 * @param def 默认
	 * @return 失败返回默认列表
	 */
	boolean getBoolean(String path,boolean def);
	/**
	 * 取double类型
	 * @param path 路径
	 * @param def 默认
	 * @return 失败返回默认列表
	 */
	double getDouble(String path,double def);
	
	/**
	 * 以子节点为主节点生成一个ConfigGetter
	 * @param path 路径
	 * @return
	 */
	ConfigGetter getConfig(String path);
	/**
	 * 是否有子节点
	 * @param path 路径
	 * @return
	 */
	boolean contains(String path);
	/**
	 * 获取子ID列表
	 * @param paramBoolean 是否获取所有子级
	 * @return
	 */
	Set<String> getKeys(boolean paramBoolean);
	/**
	 * 是否是一层config
	 * @param path 路径
	 * @return
	 */
	boolean isConfig(String path);
	/**
	 * 是否是整数
	 * @param path 路径
	 * @return
	 */
	boolean isInt(String path);
	
	/**
	 * 是否是boolean
	 * @param path 路径
	 * @return
	 */
	boolean isBoolean(String path);
	/**
	 * 配置节点是否是int行的list
	 * @param path 路径
	 * @return
	 */
	boolean isList(String path);
	/**
	 * 获取long型
	 * @param string 路径
	 * @param def 默认值
	 * @return
	 */
	long getLong(String string, long def);
}
