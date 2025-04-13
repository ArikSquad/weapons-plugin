package eu.mikart.guns;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import eu.mikart.guns.guns.GunManager;
import eu.mikart.guns.listener.GunListener;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("UnstableApiUsage")
public final class Guns extends JavaPlugin {
	@Getter
	public static Guns instance;
	@Getter
	public static GunManager gunManager;

	@Override
	public void onLoad() {
		CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
	}

	@Override
	public void onEnable() {
		instance = this;
		gunManager = new GunManager(this);

		CommandAPI.onEnable();

		new CommandAPICommand("getgun")
				.withArguments(
						new StringArgument("gun").setOptional(true)
				)
				.executesPlayer((player, args) -> {
					String gunName = (String) args.get("gun");
					if (gunName == null) {
						player.give(gunManager.getGun("pistol").getItem());
						return;
					}
					try {
						player.give(gunManager.getGun(gunName).getItem());
					} catch (NullPointerException e) {
						player.sendMessage(Component.text("Gun not found").color(NamedTextColor.RED));
						return;
					}
				}).register(this);

		getServer().getPluginManager().registerEvents(new GunListener(), this);
	}

	@Override
	public void onDisable() {
		instance = null;
	}

}
