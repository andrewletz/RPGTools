package net.Vala.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.Vala.general.Logger;
import net.Vala.general.RPGTools;

import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public enum YAMLFile {

	// @formatter:off
	CONFIG("config"),
	PICKAXEBLOCKS("pickaxeBlocks"),
	PICKAXECONFIG("pickaxeConfig"),
	PICKAXESPCOSTS("pickaxeSpCosts"),
	SHOVELBLOCKS("shovelBlocks"),
	SHOVELCONFIG("shovelConfig"),
	SHOVELSPCOSTS("shovelSpCosts"),
	AXEBLOCKS("axeBlocks"),
	AXECONFIG("axeConfig"),
	AXESPCOSTS("axeSpCosts");
	;
	// @formatter:on

	private final String fileName;
	File file;
	FileConfiguration config;

	private YAMLFile(String name) {
		this.fileName = name + ".yml";
	}

	public String getFileName() {
		return fileName;
	}

	public File getFile() {
		return file;
	}

	public FileConfiguration getConfig() {
		return config;
	}
	
	public void setConfig(FileConfiguration input) {
		config = input;
	}

	public synchronized void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void makeFiles() {
		for (YAMLFile yaml : values()) {
			yaml.file = new File(RPGTools.getPlugin().getDataFolder(), yaml.getFileName());
		}
	}

	public static void firstRun() throws Exception {
		for (YAMLFile yaml : values()) {
			if (!yaml.getFile().exists()) {
				yaml.getFile().getParentFile().mkdirs();
				copy(RPGTools.getPlugin().getResource(yaml.getFileName()), yaml.getFile());
			}
		}
	}

	private static void copy(InputStream in, File file) {
		try {
			OutputStream out = new FileOutputStream(file);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadFiles() {
		for (YAMLFile yaml : values()) {
			try {
				yaml.getConfig().load(yaml.getFile());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void reloadConfigs() throws IOException {
		for (YAMLFile yaml : values()) {
			if (yaml.getFile() == null) {
				yaml.file = new File(RPGTools.getPlugin().getDataFolder(), yaml.getFileName());
			}
			yaml.config = YamlConfiguration.loadConfiguration(yaml.getFile());
			
			// Look for defaults in the jar
			InputStream defConfigStream = RPGTools.getPlugin().getResource(
					yaml.getFileName());
			OutputStream outputStream = new FileOutputStream(yaml.file);
			IOUtils.copy(defConfigStream, outputStream);
			outputStream.close();
			if (defConfigStream != null) {
				YamlConfiguration defConfig = YamlConfiguration
						.loadConfiguration(yaml.file);
				yaml.getConfig().setDefaults(defConfig);
			}
		}
	}

	public static void prepareFiles() {
		for (YAMLFile yaml : values()) {
			yaml.file = new File(RPGTools.getPlugin().getDataFolder(), yaml.fileName);
			yaml.config = new YamlConfiguration();
		}

		try {
			firstRun();
		} catch (Exception e) {
			e.printStackTrace();
		}
		loadFiles();
	}

}
