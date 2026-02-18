package de.rayzs.tacticalmonsters.api.scheduler;

public class SchedulerTask {

    private boolean stopped = false;

    /**
     * Checks if the scheduler task is stopped.
     *
     * @return true if the scheduler task is stopped, false otherwise.
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * Stops the scheduler task.
     */
    public void stop() {
        this.stopped = true;
    }
}
