package eu.mikart.guns.guns.impl;

import eu.mikart.guns.guns.Gun;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class AssaultRifleGun extends Gun {

	public AssaultRifleGun() {
		super("assault", Component.text("AK-47").color(NamedTextColor.RED), "A classical gun", 5, 10, 8, 30, 0.2F);
		throughPlayers(true);
		material(Material.IRON_SHOVEL);
	}

	@Override
	public void onHit(Player shooter, LivingEntity target) {
		target.getWorld().spawnParticle(Particle.LARGE_SMOKE, target.getLocation(), 10, 0.5, 0.5, 0.5);
	}

}
