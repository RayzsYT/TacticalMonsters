package de.rayzs.tacticalmonsters.impl.scheduler.bukkit;

import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import de.rayzs.tacticalmonsters.api.scheduler.*;
import org.bukkit.Bukkit;

public class BukkitSchedulerProvider implements SchedulerProvider {

    private final TacticalMonstersAPI api;

    public BukkitSchedulerProvider(TacticalMonstersAPI api) {
        this.api = api;
    }

    @Override
    public SchedulerTask createScheduler(SchedulerTask scheduler) {
        return new BukkitSchedulerTask(Bukkit.getScheduler().runTask(api.getPlugin(), scheduler).getTaskId());
    }

    @Override
    public SchedulerTask createScheduler(SchedulerTask scheduler, long delay) {
        return new BukkitSchedulerTask(Bukkit.getScheduler().runTaskLater(api.getPlugin(), scheduler, delay).getTaskId());
    }

    @Override
    public SchedulerTask createScheduler(SchedulerTask scheduler, long delay, long period) {
        return new BukkitSchedulerTask(Bukkit.getScheduler().runTaskTimer(api.getPlugin(), scheduler, delay, period).getTaskId());
    }

    @Override
    public SchedulerTask createAsyncScheduler(SchedulerTask scheduler) {
        return new BukkitSchedulerTask(Bukkit.getScheduler().runTaskAsynchronously(api.getPlugin(), scheduler).getTaskId());
    }

    @Override
    public SchedulerTask createAsyncScheduler(SchedulerTask scheduler, long delay) {
        return new BukkitSchedulerTask(Bukkit.getScheduler().runTaskLaterAsynchronously(api.getPlugin(), scheduler, delay).getTaskId());
    }

    @Override
    public SchedulerTask createAsyncScheduler(SchedulerTask scheduler, long delay, long period) {
        return new BukkitSchedulerTask(Bukkit.getScheduler().runTaskTimerAsynchronously(api.getPlugin(), scheduler, delay, period).getTaskId());
    }
}
