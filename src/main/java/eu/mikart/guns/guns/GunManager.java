package eu.mikart.guns.guns;

import eu.mikart.guns.Guns;
import eu.mikart.guns.guns.impl.AssaultRifleGun;
import eu.mikart.guns.guns.impl.PistolGun;
import eu.mikart.guns.guns.impl.RPGGun;
import eu.mikart.guns.guns.impl.SniperGun;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class GunManager {

	private ArrayList<Gun> guns = new ArrayList<>();

	public GunManager(Guns plugin) {
		guns.add(new PistolGun());
		guns.add(new AssaultRifleGun());
		guns.add(new RPGGun());
		guns.add(new SniperGun());
	}

	public Gun getGun(String name) {
		for (Gun gun : guns) {
			if (gun.getId().equalsIgnoreCase(name)) {
				return gun;
			}
		}
		return null;
	}

}
