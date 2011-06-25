package me.wPlachno.DyeFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DyeFactory extends JavaPlugin{

	public static Logger log = Logger.getLogger("Minecraft");
	String versionNum = "0.0.1";
	public static String logPre = "[DyeFactory]";
	public static String pluginMainDir = "./plugins/DyeFactory";
	public static int fuelID = 13;
	public static Server server;
	public DyeFactoryBlockListener bListener;
	
	public void onDisable() {
		log.info("DyeFactory has been disabled. (Not my gumdrop buttons!)");

	}

	public void onEnable() {
		server = this.getServer();
		setConstants();
		bListener = new DyeFactoryBlockListener();
		PluginManager pm = this.getServer().getPluginManager();
		pm.registerEvent(Event.Type.BLOCK_DAMAGE, bListener, Event.Priority.Normal, this);
		log.info("DyeFactory " + versionNum + " has been enabled!");
	}
	
	public void setConstants(){
		File cfgFile = new File(pluginMainDir+"/config.yml");
		cfgFile.getParentFile().mkdirs();
		try{
			if (cfgFile.exists()){
				BufferedReader bRead = new BufferedReader(new FileReader(cfgFile));
				fuelID = getNextInt(bRead);
			}
			else {
				BufferedWriter bWrite = new BufferedWriter(new FileWriter(cfgFile));
				bWrite.write("fuelID: " + fuelID);
				bWrite.flush();
				bWrite.close();
			}
		} catch (IOException ex) {
			log.info("Unable to load the config file. :(");
			log.info("You do not know the muffin man. :(");
		}
	}
	public static void logIt(String msg){
		log.info(logPre + msg);
	}
	/**
	 * gets the next line from the given reader. Ignores comment lines and trims whats left
	 * @param bRead: the reader we will be reading from.
	 * @return String: The string representing the line we got from the reader
	 * @throws IOException: This function does use the reader.
	 */
	public String getNextLine(BufferedReader bRead) throws IOException{
		String buffer;
		while ((buffer = bRead.readLine()) != null){
			if((!buffer.contains("#"))&&(buffer.contains(":"))){
				return buffer.trim();
			}
		}
		return "END OF FILE";
	}
	
	/**
	 * Gets the String value of the next line from the reader. Checks for the ':' character
	 * @param bRead: the reader to use when reading the value from
	 * @return String: the string representing whatever was after the ':' char
	 * @throws IOException: See getNextLine
	 */
	public String getNextValue(BufferedReader bRead) throws IOException{
		String buffer;
		buffer = getNextLine(bRead);
		if (buffer == "END OF FILE"){
			throw new IOException();
		}
		else{
			int assignIdx = buffer.indexOf(":");
			return buffer.substring(assignIdx+1);
		}
	}
	
	/**
	 * Gets the int value of the nextline from the reader.
	 * @param bRead: the reader to read from.
	 * @return int: the int we found
	 * @throws IOException: See getNextLine
	 */
	public int getNextInt (BufferedReader bRead) throws IOException{
		String buffer = getNextValue(bRead);
		return Integer.parseInt(buffer.trim());
	}
	

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		 
		 if(cmd.getName().equalsIgnoreCase("dyefactory")){ // If the player typed /basic then do the following...
		   logIt(DyeFactory.logPre + this.versionNum);
		   logIt("Smash some " + Material.getMaterial(DyeFactory.fuelID) + " into a block of wool!");
		   logIt("The grain mixes with any surrounding water and creates dye!");
		   logIt("I heard a rumor about lava being a good dye ingredient...");
		   return true;
		 } //If this has happened the function will break and return true. if this hasn't happened the a value of false will be returned.
		 return false; 
		}
}
