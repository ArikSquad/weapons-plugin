package eu.mikart.guns.guns;

import eu.mikart.guns.WeaponsPlugin;
import eu.mikart.guns.guns.impl.AssaultRifleGun;
import eu.mikart.guns.guns.impl.PistolGun;
import eu.mikart.guns.guns.impl.RPGGun;
import eu.mikart.guns.guns.impl.SniperGun;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

@Getter
public class GunManager {

	private final ArrayList<Gun> guns = new ArrayList<>();

	public GunManager(WeaponsPlugin plugin) {
		guns.addAll(Arrays.asList(GunType.create(plugin)));
	}

	public Gun getGun(String name) {
		for (Gun gun : guns) {
			if (gun.getId().equalsIgnoreCase(name)) {
				return gun;
			}
		}
		return null;
	}

	public void reload() {
		guns.clear();
		guns.addAll(Arrays.asList(GunType.create(WeaponsPlugin.getInstance())));
	}

	public enum GunType {
		PISTOL(PistolGun::new),
		ASSAULT_RIFLE(AssaultRifleGun::new),
		RPG(RPGGun::new),
		SNIPER(SniperGun::new);

		public final Function<WeaponsPlugin, Gun> commandSupplier;

		GunType(@NotNull Function<WeaponsPlugin, Gun> supplier) {
			this.commandSupplier = supplier;
		}

		@NotNull
		public static Gun[] create(@NotNull WeaponsPlugin plugin) {
			return Arrays.stream(values()).map(type -> type.supply(plugin))
					.filter(command -> !plugin.getSettings().isGunDisabled(command))
					.toArray(Gun[]::new);
		}

		@NotNull
		public Gun supply(@NotNull WeaponsPlugin plugin) {
			return commandSupplier.apply(plugin);
		}

	}

}
