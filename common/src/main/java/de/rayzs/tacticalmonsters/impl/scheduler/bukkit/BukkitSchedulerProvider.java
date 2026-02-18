package de.rayzs.tacticalmonsters.impl.scheduler.bukkit;

import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import de.rayzs.tacticalmonsters.api.scheduler.*;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class BukkitSchedulerProvider implements SchedulerProvider {

    private final TacticalMonstersAPI api;

    public BukkitSchedulerProvider(TacticalMonstersAPI api) {
        this.api = api;
    }

    @Override
    public SchedulerTask createScheduler(Consumer<SchedulerTask> scheduler) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (schedulerTask.isStopped()) {
                    this.cancel();
                    return;
                }

                scheduler.accept(schedulerTask);
            }
        }.runTask(api.getPlugin());

        return schedulerTask;
    }

    @Override
    public SchedulerTask createScheduler(Consumer<SchedulerTask> scheduler, long delay) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (schedulerTask.isStopped()) {
                    this.cancel();
                    return;
                }

                scheduler.accept(schedulerTask);
            }
        }.runTaskLater(api.getPlugin(), delay);

        return schedulerTask;
    }

    @Override
    public SchedulerTask createScheduler(Consumer<SchedulerTask> scheduler, long delay, long period) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (schedulerTask.isStopped()) {
                    this.cancel();
                    return;
                }

                scheduler.accept(schedulerTask);
            }
        }.runTaskTimer(api.getPlugin(), delay, period);

        return schedulerTask;
    }

    @Override
    public SchedulerTask createAsyncScheduler(Consumer<SchedulerTask> scheduler) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (schedulerTask.isStopped()) {
                    this.cancel();
                    return;
                }

                scheduler.accept(schedulerTask);
            }
        }.runTaskAsynchronously(api.getPlugin());

        return schedulerTask;
    }

    @Override
    public SchedulerTask createAsyncScheduler(Consumer<SchedulerTask> scheduler, long delay) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (schedulerTask.isStopped()) {
                    this.cancel();
                    return;
                }

                scheduler.accept(schedulerTask);
            }
        }.runTaskLaterAsynchronously(api.getPlugin(), delay);

        return schedulerTask;
    }

    @Override
    public SchedulerTask createAsyncScheduler(Consumer<SchedulerTask> scheduler, long delay, long period) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (schedulerTask.isStopped()) {
                    this.cancel();
                    return;
                }

                scheduler.accept(schedulerTask);
            }
        }.runTaskTimerAsynchronously(api.getPlugin(), delay, period);

        return schedulerTask;
    }
}
