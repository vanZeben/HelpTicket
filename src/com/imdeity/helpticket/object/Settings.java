package com.imdeity.helpticket.object;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class Settings {

	public static YamlConfiguration config = new YamlConfiguration();

	public void loadDefaults() {
		config = YamlConfiguration.loadConfiguration(new File("plugins/HelpTicket/config.yml"));
		if (!config.contains("help_ticket.notify_delay"))
			config.set("help_ticket.notify_delay", 20);
		if (!config.contains("help_ticket.min_word_count"))
			config.set("help_ticket.min_word_count", 3);
		if (!config.contains("mysql.server.address"))
			config.set("mysql.server.address", "localhost");
		if (!config.contains("mysql.server.port"))
			config.set("mysql.server.port", 3306);
		if (!config.contains("mysql.database.name"))
			config.set("mysql.database.name", "kingdoms");
		if (!config.contains("mysql.database.username"))
			config.set("mysql.database.username", "root");
		if (!config.contains("mysql.database.password"))
			config.set("mysql.database.password", "root");
		if (!config.contains("mysql.database.table_prefix"))
			config.set("mysql.database.table_prefix", "helpticket_");
		this.save();
	}

	public void save() {
		try {
			config.save(new File("plugins/HelpTicket/config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getNotifyDelay() {
		return config.getInt("help_ticket.notify_delay");
	}
	
	public int getMinWordCount() {
		return config.getInt("help_ticket.min_word_count");
	}
	
	public String getMySQLServerAddress() {
		return config.getString("mysql.server.address");
	}
	
	public int getMySQLServerPort() {
		return config.getInt("mysql.server.port");
	}
	
	public String getMySQLDatabaseName() {
		return config.getString("mysql.database.name");
	}
	
	public String getMySQLDatabaseUsername() {
		return config.getString("mysql.database.username");
	}
	
	public String getMySQLDatabasePassword() {
		return config.getString("mysql.database.password");
	}
	
	public String getMySQLDatabaseTablePrefix() {
		return config.getString("mysql.database.table_prefix");
	}
}
