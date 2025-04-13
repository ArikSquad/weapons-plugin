package eu.mikart.guns.guns;

import eu.mikart.guns.Guns;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

@Getter
@SuppressWarnings("UnstableApiUsage")
public abstract class Gun {

	private final String id;
	private final Component name;
	private final String description;

	private final int damage;
	private final double range;
	private final int headshotDamage;
	private final int ammo;
	private final float fireRate;

	private boolean throughPlayers = false;
	private GunType type = GunType.BASIC;
	private Material material = Material.WOODEN_HOE;
	private ItemUseAnimation itemUseAnimation = ItemUseAnimation.BOW;

	public Gun(String id, Component name, String description, int damage, double range, int headshotDamage, int ammo, float fireRate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.damage = damage;
		this.range = range;
		this.headshotDamage = headshotDamage;
		this.ammo = ammo;
		this.fireRate = fireRate;
	}

	public void throughPlayers(boolean throughPlayers) {
		this.throughPlayers = throughPlayers;
	}

	public void gunType(GunType type) {
		this.type = type;
	}

	public void material(Material material) {
		this.material = material;
	}

	public void useAnimation(ItemUseAnimation itemUseAnimation) {
		this.itemUseAnimation = itemUseAnimation;
	}

	public void onHit(Player shooter, LivingEntity target) {}

	public void onLandHit(Player shooter, Location location) {}

	public ItemStack getItem() {
		ItemStack gun = new ItemStack(this.material);
		gun.setData(DataComponentTypes.CUSTOM_NAME, this.name);
		gun.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(this.description).color(NamedTextColor.GRAY)).build());
		gun.setData(DataComponentTypes.CONSUMABLE, Consumable.consumable().animation(this.itemUseAnimation).consumeSeconds(32767).build()); // very long time
		gun.setData(DataComponentTypes.DAMAGE, 0); // disable vanilla damage

		gun.editPersistentDataContainer(pdc -> {
			pdc.set(
					new NamespacedKey(Guns.getInstance(), "gun"),
					PersistentDataType.STRING,
					this.id
			);
		});
		return gun;
	}

}
