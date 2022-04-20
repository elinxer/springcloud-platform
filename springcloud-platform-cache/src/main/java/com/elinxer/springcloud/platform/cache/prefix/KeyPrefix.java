package com.elinxer.springcloud.platform.cache.prefix;

/**
 * KeyPrefix
 *
 * @author elinx
 * @version 3.0.0
 */
public interface KeyPrefix {
		
	public int expireSeconds();
	
	public String getPrefix();
	
}
