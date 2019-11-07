package com.pxpmc.choujiang.config;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * 配置文件类
 * @author px
 * @param <T>
 */
public abstract class Config implements ConfigGetter
{
	private Object object;
	/**
	 * 获取原始属性
	 * @return
	 */
	public Object getObject() {
		return object;
	}
	/**
	 * 获取当前路径
	 * @return
	 */
    public abstract String getCurrentPath();
	/**
	 * 设置原始属性
	 * @param object
	 */
	public void setObject(Object object) {
		this.object = object;
	}
	//父类
	private Config parent;

	//保存的文件位置
	private File file;
	/**
	 * 设置文件
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}
	/**
	 * 取文件
	 * @return
	 */
	public File getFile() {
		return file;
	}
	/**
	 * 保存
	 */
	public abstract void save();
	/**
	 * 保存
	 * @param file 保存位置
	 */
	public abstract void save(File file);
	/**
	 * 取文本类型
	 * @param path
	 * @return
	 */
	public abstract String getString(String path);
	/**
	 * 取文本
	 * @param path 路径
	 * @param def 默认返回
	 * @return
	 */
	public abstract String getString(String path,String def);
	/**
	 * 取字符串列表
	 * @param path 路径
	 * @return 默认返回空的列表
	 */
	public abstract List<String> getStringList(String path);
	/**
	 * 取数字列表
	 * @param path 路径
	 * @return 默认返回空的列表
	 */
	public abstract List<Integer> getIntList(String path);
	/**
	 * 取int类型
	 * @param path 路径
	 * @param def 默认
	 * @return
	 */
	public abstract int getInt(String path,int def);
	/**
	 * 取boolean类型
	 * @param path 路径
	 * @param def 默认
	 * @return
	 */
	public abstract boolean getBoolean(String path,boolean def);
	/**
	 * 取double类型
	 * @param path 路径
	 * @param def 默认
	 * @return
	 */
	public abstract double getDouble(String path,double def);
	/**
	 * 设置一个对象
	 * @param path 路径
	 * @param txt
	 */
	public abstract void set(String path,Object o);
	/**
	 * 设置一个文本列表
	 * @param path 路径
	 * @param txt
	 */
	public abstract void set(String path,List<String> list);
	/**
	 * 以子节点为主节点生成一个config
	 * @param path 路径
	 * @return
	 */
	public abstract Config getConfig(String path);
	/**
	 * 是否有子节点
	 * @param path 路径
	 * @return
	 */
	public abstract boolean contains(String path);
	/**
	 * 是否是文本
	 * @param path 路径
	 * @return
	 */
	public abstract boolean isString(String path);
	/**
	 * 是否有子节点
	 * @param path 路径
	 * @param paramBoolean
	 * @return
	 */
	public abstract boolean contains(String path, boolean paramBoolean);
	/**
	 * 获取父类
	 * @return
	 */
	public Config getParent() {
		return parent;
	}
	/**
	 * 获取子ID列表
	 * @param paramBoolean 是否获取所有子级
	 * @return
	 */
	public abstract Set<String> getKeys(boolean paramBoolean);
	/**
	 * 设置父类
	 * @param parent 父类配置
	 */
	public void setParent(Config parent) {
		this.parent = parent;		
	}
	/**
	 * 是否是一层config
	 * @param path 路径
	 * @return
	 */
	public abstract boolean isConfig(String path);
	/**
	 * 是否是整数
	 * @param path 路径
	 * @return
	 */
	public abstract boolean isInt(String path);
	/**
	 * 获取一个对象
	 * @param path 路径
	 * @return
	 */
	public abstract Object get(String path);
	
	/**
	 * 是否是boolean
	 * @param path 路径
	 * @return
	 */
	public abstract boolean isBoolean(String path);
	/**
	 * 配置节点是否是int行的list
	 * @param path 路径
	 * @return
	 */
	public abstract boolean isList(String path);
	/**
	 * 获取long型
	 * @param string 路径
	 * @param def 默认值
	 * @return
	 */
	public abstract long getLong(String string, long def);
	/**
	 * 获取long型
	 * @param string 路径
	 * @return
	 */
	public abstract long getLong(String string);
	/**
	 * 路径节点是否是AdapterItemStack型
	 * @param path
	 * @return
	 */
	public abstract boolean isItemStack(String path);
	/**
	 * 获取整数类型
	 * @param path
	 * @return
	 */
	public abstract int getInt(String path);
	/**
	 * 路径节点是否是double型
	 * @param path
	 * @return
	 */
	public abstract boolean isDouble(String path);
	/**
	 * 路径节点是否是long型
	 * @param path
	 * @return
	 */
	public abstract boolean isLong(String path);
}
