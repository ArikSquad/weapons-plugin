package eu.mikart.guns;

import de.exlll.configlib.NameFormatters;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import eu.mikart.guns.commands.GunCommand;
import eu.mikart.guns.config.Settings;
import eu.mikart.guns.guns.GunManager;
import eu.mikart.guns.listener.WeaponInteractionListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public final class WeaponsPlugin extends JavaPlugin {
	@Getter
	public static WeaponsPlugin instance;
	@Getter
	public static GunManager gunManager;

	@NotNull
	@Setter
	@Getter
	private Settings settings;

	YamlConfigurationProperties.Builder<?> YAML_CONFIGURATION_PROPERTIES = YamlConfigurationProperties.newBuilder()
			.charset(StandardCharsets.UTF_8)
			.setNameFormatter(NameFormatters.LOWER_UNDERSCORE);

	@Override
	public void onLoad() {
		CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
	}

	@Override
	public void onEnable() {
		instance = this;

		loadSettings();
		gunManager = new GunManager(this);


		CommandAPI.onEnable();
		new GunCommand();

		getServer().getPluginManager().registerEvents(new WeaponInteractionListener(), this);
	}

	private void loadSettings() {
		setSettings(YamlConfigurations.update(
				getDataPath().resolve("config.yml"),
				Settings.class,
				YAML_CONFIGURATION_PROPERTIES.header("""
						┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
						┃       GunsPlugin Config      ┃
						┃    Developed by ArikSquad    ┃
						┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛
						┗╸ Ask help in Discord: https://discord.gg/SuXGbq24wA""").build()
		));
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	public void reload() {
		loadSettings();
		gunManager.reload();
	}

}
