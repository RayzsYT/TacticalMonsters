package de.rayzs.tacticalmonsters.commands;

import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import org.jetbrains.annotations.*;
import org.bukkit.command.*;
import java.util.List;

public class ReloadCommand implements CommandExecutor, TabExecutor {

    private final TacticalMonstersAPI api;

    public ReloadCommand(final TacticalMonstersAPI api) {
        this.api = api;
    }

    @Override
    public boolean onCommand(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String label,
            final @NotNull String[] args
    ) {

        api.reload();
        sender.sendMessage("Done!");

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(
            final @NotNull CommandSender sender,
            final @NotNull Command command,
            final @NotNull String label,
            final @NotNull String[] args
    ) {
        return List.of();
    }
}
