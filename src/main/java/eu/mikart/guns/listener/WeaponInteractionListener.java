package eu.mikart.guns.listener;

import eu.mikart.guns.WeaponsPlugin;
import eu.mikart.guns.guns.Gun;
import eu.mikart.guns.guns.GunType;
import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.sound.Sound;
import org.bukkit.*;
import net.kyori.adventure.key.Key;
import org.bukkit.Color;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.*;

public class WeaponInteractionListener implements Listener {

	private final Map<UUID, Map<String, Long>> lastFiredTimes = new HashMap<>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack item = event.getItem();

		if (item == null) {
			return;
		}

		PersistentDataContainerView dataContainer = item.getPersistentDataContainer();

		if (dataContainer.has(Objects.requireNonNull(NamespacedKey.fromString("gun", WeaponsPlugin.getInstance())))) {
			event.setCancelled(true);
			Player shooter = event.getPlayer();
			String gunId = dataContainer.get(NamespacedKey.fromString("gun", WeaponsPlugin.getInstance()), PersistentDataType.STRING);
			Gun gun = WeaponsPlugin.gunManager.getGun(gunId);

			if (!gun.canExecute(shooter)) {
				return;
			}

			long currentTime = System.currentTimeMillis();
			UUID playerId = shooter.getUniqueId();

			Map<String, Long> playerCooldowns = lastFiredTimes.computeIfAbsent(playerId, k -> new HashMap<>());
			long lastFiredTime = playerCooldowns.getOrDefault(gunId, 0L);

			if (currentTime - lastFiredTime < gun.getFireRate() * 1000L) {
				return;
			}

			playerCooldowns.put(gunId, currentTime);

			World world = shooter.getWorld();
			Location eyeLocation = shooter.getEyeLocation();
			Vector direction = eyeLocation.getDirection().normalize();

			boolean hasAmmo;
			try {
				hasAmmo = dataContainer.get(NamespacedKey.fromString("ammo", WeaponsPlugin.getInstance()), PersistentDataType.INTEGER) != null;
			} catch (IllegalArgumentException e) {
				hasAmmo = false;
			}

			if (!hasAmmo) {
				item.editPersistentDataContainer(pdc -> {
					pdc.set(new NamespacedKey(WeaponsPlugin.getInstance(), "ammo"),
							PersistentDataType.INTEGER,
							gun.getAmmo());
				});
			} else {
				int ammo = dataContainer.get(NamespacedKey.fromString("ammo", WeaponsPlugin.getInstance()), PersistentDataType.INTEGER);
				if (ammo <= 0) {
					shooter.sendMessage("Out of ammo!");
					return;
				}
				item.editPersistentDataContainer(pdc -> {
					pdc.set(NamespacedKey.fromString("ammo", WeaponsPlugin.getInstance()), PersistentDataType.INTEGER, ammo - 1);
				});
			}

			double maxDistance = gun.getRange();
			double stepSize = 0.2;

			boolean hitEntity = false;

			for (double d = 0; d <= maxDistance && !hitEntity; d += stepSize) {
				Location currentLocation = eyeLocation.clone().add(direction.clone().multiply(d));

				Particle.DustOptions dustOptions = new Particle.DustOptions(Color.GRAY, 0.5f);
				world.spawnParticle(Particle.DUST, currentLocation, 1, 0, 0, 0, 0, dustOptions);

				Collection<Entity> nearbyEntities = world.getNearbyEntities(currentLocation, 0.3, 0.3, 0.3);
				for (Entity entity : nearbyEntities) {
					if (entity instanceof LivingEntity target && entity != shooter) {
						boolean isHeadshot = currentLocation.getY() > (target.getLocation().getY() + (target.getHeight() * 0.8));
						String entityName = entity instanceof Player ? entity.getName() : entity.getType().toString();

						if (isHeadshot) {
							target.damage(gun.getHeadshotDamage(), shooter);
							shooter.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.PLAYER, 1.0f, 1.5f));
							shooter.sendMessage("HEADSHOT " + entityName + "!");
						} else {
							target.damage(gun.getDamage(), shooter);
							shooter.sendMessage("You hit " + entityName + "!");
						}

						gun.onHit(shooter, target);
						hitEntity = true;
						if (gun.isThroughPlayers()) {
							continue;
						} else {
							break;
						}
					}
				}


				if (currentLocation.getBlock().getType().isSolid()) {
					if (!gun.getType().equals(GunType.PROJECTILE)) {
						world.spawnParticle(Particle.BLOCK_CRUMBLE, currentLocation, 10, 0.1, 0.1, 0.1, 0.1,
								currentLocation.getBlock().getBlockData());
						break;
					} else {
						gun.onLandHit(shooter, currentLocation);
					}
				}
			}

			shooter.playSound(Sound.sound(Key.key("block.note_block.pling"), Sound.Source.PLAYER, 1.0f, 1.0f));
		}
	}

}
