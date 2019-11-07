package com.pxpmc.choujiang.config;
/**
 * 配置容器,用于获取配置
 * @author px
 * 2019年7月13日13:56:34
 */
public interface ConfigContainer {
	/**
	 * 获取配置
	 * @return
	 */
	Config getConfig();

	/**
	 * 设置配置文件
	 * @param config
	 */
	void setConfig(Config config);
}
