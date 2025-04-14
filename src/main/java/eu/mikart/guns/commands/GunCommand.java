package eu.mikart.guns.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import eu.mikart.guns.WeaponsPlugin;
import eu.mikart.guns.guns.Gun;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class GunCommand {

	public GunCommand() {

		CommandAPICommand reload = new CommandAPICommand("reload")
				.withPermission("guns.command.reload")
				.executesPlayer((player, args) -> {
					WeaponsPlugin.getInstance().reload();
					player.sendMessage(Component.text("Guns reloaded").color(NamedTextColor.GREEN));
				});

		new CommandAPICommand("gun")
				.withPermission("guns.command.gun")
				.withArguments(
						new StringArgument("gun").setOptional(true).replaceSuggestions(
								ArgumentSuggestions.strings(commandSenderSuggestionInfo -> WeaponsPlugin.gunManager.getGuns().stream()
										.map(Gun::getId)
										.toArray(String[]::new))
						)
				)
				.executesPlayer((player, args) -> {
					String gunName = (String) args.get("gun");
					if (gunName == null) {
						player.give(WeaponsPlugin.gunManager.getGun("pistol").getItem());
						return;
					}
					try {
						player.give(WeaponsPlugin.gunManager.getGun(gunName).getItem());
					} catch (NullPointerException e) {
						player.sendMessage(Component.text("Gun not found").color(NamedTextColor.RED));
						return;
					}
				})
				.withSubcommand(reload)
				.register();
	}

}
