package eu.mikart.guns.guns.impl;

import eu.mikart.guns.WeaponsPlugin;
import eu.mikart.guns.guns.Gun;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PistolGun extends Gun {

	public PistolGun(WeaponsPlugin plugin) {
		super(plugin, "pistol", Component.text("Basic Pistol").color(NamedTextColor.BLUE), "A basic pistol", 2, 10, 3, 15, 1);
	}

	@Override
	public void onHit(Player shooter, LivingEntity target) {

	}

}
