package de.rayzs.tacticalmonsters.impl.scheduler.bukkit;

import de.rayzs.tacticalmonsters.api.scheduler.SchedulerTask;
import org.bukkit.Bukkit;

public class BukkitSchedulerTask extends SchedulerTask {

    private final int taskId;

    public BukkitSchedulerTask(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() { }

    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
