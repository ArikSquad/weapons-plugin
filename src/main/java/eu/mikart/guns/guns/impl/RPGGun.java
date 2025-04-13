package eu.mikart.guns.guns.impl;

import eu.mikart.guns.guns.Gun;
import eu.mikart.guns.guns.GunType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class RPGGun extends Gun {

	public RPGGun() {
		super("rpg", Component.text("RPG").color(NamedTextColor.GREEN), "A fantastic way to blow someone up", 50, 40, 50, 5, 0.2F);
		gunType(GunType.PROJECTILE);
		material(Material.FIRE_CORAL);
	}

	@Override
	public void onHit(Player shooter, LivingEntity target) {
		target.getLocation().createExplosion(shooter, 5F);
	}

	@Override
	public void onLandHit(Player shooter, Location location) {
		location.createExplosion(shooter, 5F);
	}
}
