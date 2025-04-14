package eu.mikart.guns.config;

import de.exlll.configlib.Configuration;
import eu.mikart.guns.guns.Gun;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ArrayList;

@Getter
@Configuration
@SuppressWarnings("FieldMayBeFinal")
public class Settings {

	private boolean debug = false;
	private boolean enableGuns = true;

	@Getter(AccessLevel.NONE)
	private List<String> disabledGuns = new ArrayList<>();

	public boolean isGunDisabled(@NotNull Gun gunName) {
		return disabledGuns.stream().map(c -> c.startsWith("/") ? c.substring(1) : c).anyMatch(c -> c.equalsIgnoreCase(gunName.getId()));
	}

}
