package de.rayzs.tacticalmonsters.impl.scheduler.folia;

import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import de.rayzs.tacticalmonsters.api.scheduler.*;
import org.bukkit.Bukkit;

public class FoliaSchedulerProvider implements SchedulerProvider {

    private final TacticalMonstersAPI api;

    public FoliaSchedulerProvider(TacticalMonstersAPI api) {
        this.api = api;
    }

    @Override
    public SchedulerTask createScheduler(SchedulerTask scheduler) {
        return new FoliaSchedulerTask(Bukkit.getGlobalRegionScheduler().run(api.getPlugin(), task -> scheduler.run()));
    }

    @Override
    public SchedulerTask createScheduler(SchedulerTask scheduler, long delay) {
        return new FoliaSchedulerTask(Bukkit.getGlobalRegionScheduler().runDelayed(api.getPlugin(), task -> scheduler.run(), delay));
    }

    @Override
    public SchedulerTask createScheduler(SchedulerTask scheduler, long delay, long period) {
        return new FoliaSchedulerTask(Bukkit.getGlobalRegionScheduler().runAtFixedRate(api.getPlugin(), task -> scheduler.run(), delay, period));
    }

    @Override
    public SchedulerTask createAsyncScheduler(SchedulerTask scheduler) {
        return new FoliaSchedulerTask(Bukkit.getGlobalRegionScheduler().run(api.getPlugin(), task -> scheduler.run()));
    }

    @Override
    public SchedulerTask createAsyncScheduler(SchedulerTask scheduler, long delay) {
        return new FoliaSchedulerTask(Bukkit.getGlobalRegionScheduler().runDelayed(api.getPlugin(), task -> scheduler.run(), delay));
    }

    @Override
    public SchedulerTask createAsyncScheduler(SchedulerTask scheduler, long delay, long period) {
        return new FoliaSchedulerTask(Bukkit.getGlobalRegionScheduler().runAtFixedRate(api.getPlugin(), task -> scheduler.run(), delay, period));
    }
}
