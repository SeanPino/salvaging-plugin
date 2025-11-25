package com.salvaging;

import com.salvaging.SalvagingPlugin;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SalvagingPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(SalvagingPlugin.class);
		RuneLite.main(args);
	}
}