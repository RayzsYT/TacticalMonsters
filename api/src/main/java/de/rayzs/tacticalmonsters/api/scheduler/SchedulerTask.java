package de.rayzs.tacticalmonsters.api.scheduler;

public class SchedulerTask {

    private boolean stopped = false;

    /**
     * Says if the scheduler task is still running or not.
     *
     * @return true if the scheduler task is still running, false otherwise.
     */
    public boolean isRunning() {
        return !stopped;
    }

    /**
     * Stops the scheduler task.
     */
    public void stop() {
        this.stopped = true;
    }
}
