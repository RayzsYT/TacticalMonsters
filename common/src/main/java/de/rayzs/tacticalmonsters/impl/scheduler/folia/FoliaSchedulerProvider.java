package de.rayzs.tacticalmonsters.impl.scheduler.folia;

import de.rayzs.tacticalmonsters.api.TacticalMonstersAPI;
import de.rayzs.tacticalmonsters.api.scheduler.SchedulerProvider;
import de.rayzs.tacticalmonsters.api.scheduler.SchedulerTask;
import org.bukkit.Bukkit;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class FoliaSchedulerProvider implements SchedulerProvider {

    private final TacticalMonstersAPI api;

    public FoliaSchedulerProvider(TacticalMonstersAPI api) {
        this.api = api;
    }

    @Override
    public SchedulerTask createScheduler(Consumer<SchedulerTask> scheduler) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        Bukkit.getGlobalRegionScheduler().run(api.getPlugin(), s -> {
            if (schedulerTask.isStopped()) {
                s.cancel();
                return;
            }

            scheduler.accept(schedulerTask);
        });

        return schedulerTask;
    }

    @Override
    public SchedulerTask createScheduler(Consumer<SchedulerTask> scheduler, long delay) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        Bukkit.getGlobalRegionScheduler().runDelayed(api.getPlugin(), s -> {
            if (schedulerTask.isStopped()) {
                s.cancel();
                return;
            }

            scheduler.accept(schedulerTask);
        }, delay);

        return schedulerTask;
    }

    @Override
    public SchedulerTask createScheduler(Consumer<SchedulerTask> scheduler, long delay, long period) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        Bukkit.getGlobalRegionScheduler().runAtFixedRate(api.getPlugin(), s -> {
            if (schedulerTask.isStopped()) {
                s.cancel();
                return;
            }

            scheduler.accept(schedulerTask);
        }, delay, period);

        return schedulerTask;
    }

    @Override
    public SchedulerTask createAsyncScheduler(Consumer<SchedulerTask> scheduler) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        Bukkit.getAsyncScheduler().runNow(api.getPlugin(), s -> {
            if (schedulerTask.isStopped()) {
                s.cancel();
                return;
            }

            scheduler.accept(schedulerTask);
        });

        return schedulerTask;
    }

    @Override
    public SchedulerTask createAsyncScheduler(Consumer<SchedulerTask> scheduler, long delay) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        Bukkit.getAsyncScheduler().runDelayed(api.getPlugin(), s -> {
            if (schedulerTask.isStopped()) {
                s.cancel();
                return;
            }

            scheduler.accept(schedulerTask);
        }, delay * 50, TimeUnit.MILLISECONDS);

        return schedulerTask;
    }

    @Override
    public SchedulerTask createAsyncScheduler(Consumer<SchedulerTask> scheduler, long delay, long period) {
        final SchedulerTask schedulerTask = new SchedulerTask();

        Bukkit.getAsyncScheduler().runAtFixedRate(api.getPlugin(), s -> {
            if (schedulerTask.isStopped()) {
                s.cancel();
                return;
            }

            scheduler.accept(schedulerTask);
        }, delay * 50, period * 50, TimeUnit.MILLISECONDS);

        return schedulerTask;
    }
}
