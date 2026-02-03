package de.rayzs.tacticalmonsters.impl.scheduler.folia;

import de.rayzs.tacticalmonsters.api.scheduler.SchedulerTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

public class FoliaSchedulerTask extends SchedulerTask {

    private final ScheduledTask task;

    public FoliaSchedulerTask(ScheduledTask task) {
        this.task = task;
    }

    @Override
    public void run() {}

    @Override
    public void stop() {
        task.cancel();
    }
}
