package de.rayzs.tacticalmonsters.api.helper;

import org.bukkit.Bukkit;
import java.util.*;

public class VersionHelper {

    private static final Map<Version, Integer> VERSION_MAP = new HashMap<>();
    private static final Version CURRENT_VERSION;

    public static final Version MIN_SUPPORTED_VERSION = Version.values()[1];

    static {
        final Version[] versions = Version.values();
        for (int i = 2; i < versions.length; i++) {
            final Version version = versions[i];
            VERSION_MAP.put(version, convertIntoVal(version.name()));
        }

        final String serverVersion = Bukkit.getBukkitVersion()
                .split("-")[0]
                .replace(".", "_");

        Version version;
        try {
            version = Version.valueOf("V" + serverVersion);
        } catch (Exception exception) {
            version = Version.UNSUPPORTED;
        }

        CURRENT_VERSION = version;
    }

    public static boolean isSupported() {
        return CURRENT_VERSION != Version.UNSUPPORTED;
    }

    public static boolean isBetween(final Version minVersion, final Version maxVersion) {
        final int currentVal = VERSION_MAP.get(CURRENT_VERSION);
        return minVersion.toValue() <= currentVal && currentVal <= maxVersion.toValue();
    }

    public static boolean isBelow(final Version version) {
        return CURRENT_VERSION.toValue() < version.toValue();
    }

    public static boolean isAtLeast(final Version version) {
        return CURRENT_VERSION.toValue() >= version.toValue();
    }

    public static boolean isHigher(final Version version) {
        return CURRENT_VERSION.toValue() > version.toValue();
    }

    private static int convertIntoVal(String versionName) {
        versionName = versionName.replace("_", ".");

        if (versionName.startsWith("V")) {
            versionName = versionName.substring(1);
        }

        final String[] parts = versionName.split("\\.");

        final int major = Integer.parseInt(parts[0]) * 10000;
        final int minor = Integer.parseInt(parts[1]) * 100;
        final int patch = Integer.parseInt(parts[2]);

        return major + minor + patch;
    }

    public enum Version {
        UNSUPPORTED,
        V1_21_1,
        V1_21_2,
        V1_21_3,
        V1_21_4,
        V1_21_5,
        V1_21_6,
        V1_21_7,
        V1_21_8,
        V1_21_9,
        V1_21_10,
        V1_21_11;

        public int toValue() {
            return VERSION_MAP.get(this);
        }
    }
}
