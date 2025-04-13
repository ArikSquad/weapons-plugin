package eu.mikart.guns.guns.impl;

import eu.mikart.guns.guns.Gun;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

public class SniperGun extends Gun {

	public SniperGun() {
		super("sniper", Component.text("Sniper").color(NamedTextColor.GOLD), "A very overpowered sniper", 40, 70, 200, 3, 5.0F);
		throughPlayers(true);
		material(Material.STICK);
		useAnimation(ItemUseAnimation.SPYGLASS);
	}

}
