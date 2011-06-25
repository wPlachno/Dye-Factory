package me.wPlachno.DyeFactory;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.inventory.ItemStack;

public class DyeFactoryBlockListener extends BlockListener {
	
	public void onBlockDamage(BlockDamageEvent curEvent){
		if (curEvent.getBlock().getType() == Material.WOOL){
			if (curEvent.getItemInHand().getTypeId() == DyeFactory.fuelID){
				//check for water
				int watVal = waterCheck(DyeFactory.server.getWorld(curEvent.getBlock().getWorld().getName()).getBlockAt(curEvent.getBlock().getLocation()));
				//take away gravel
				ItemStack curStack = DyeFactory.server.getPlayer(curEvent.getPlayer().getName()).getItemInHand();
				DyeFactory.server.getPlayer(curEvent.getPlayer().getName()).getItemInHand().setAmount(curStack.getAmount()-1);
				//1: get dye color
				byte woolColor = DyeFactory.server.getWorld(curEvent.getBlock().getWorld().getName()).getBlockAt(curEvent.getBlock().getLocation()).getData();
				int dyeColor = dyeToData(woolColor);
				//2: get itemstack
				DyeFactory.logIt(" " + watVal/60 + " " + DyeColor.getByData(woolColor) + " DYES CREATED.");
				ItemStack dyeStack = new ItemStack(351, watVal/60, (byte)dyeColor);
				//3: drop itemstack
				DyeFactory.server.getWorld(curEvent.getBlock().getWorld().getName()).dropItemNaturally(curEvent.getPlayer().getLocation(), dyeStack);
			}
		}
	}
	
	private int waterCheck (Block blk){
		int waterValue = 0;
		int seedRadius = 2;
		int destx= -seedRadius;
		while (destx <= seedRadius){
			int destz= -seedRadius;
			while (destz <= seedRadius){
				int desty = -seedRadius;
				while (desty <= seedRadius){
					if(!(((destx==0)&&(destz==0))&&(desty==0))){
						Block dest = blk.getWorld().getBlockAt(blk.getX() + destx, blk.getY() + desty, blk.getZ() + destz);
						if (dest.getType() == Material.WATER){
							int blockVal = 2*(6-(Math.abs(destx) + Math.abs(desty) + Math.abs(destz)));
							waterValue = waterValue + blockVal;							
						} else if (dest.getType() == Material.STATIONARY_WATER){
							int blockVal = 4*(6-(Math.abs(destx) + Math.abs(desty) + Math.abs(destz)));
							waterValue = waterValue + blockVal;														
						} else if (dest.getType() == Material.LAVA){
							int blockVal = 10*(6-(Math.abs(destx) + Math.abs(desty) + Math.abs(destz)));
							waterValue = waterValue + blockVal;							
						}
					}desty++;
				}destz++;
			} destx++;
		}
		return waterValue;
	}
	public byte dyeToData(byte dc){
		byte high = 0xF;
		return (byte) (high - dc);
	}

}
