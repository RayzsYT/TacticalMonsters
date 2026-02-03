package de.rayzs.tacticalmonsters.api.scheduler;

public interface SchedulerProvider {

    /**
     * Creates a sync scheduler task.
     *
     * @param runnable The task to run.
     * @return The created scheduler task.
     */
    SchedulerTask createScheduler(final SchedulerTask runnable);

    /**
     * Creates a sync delayed scheduler task.
     *
     * @param runnable The task to run.
     * @return The created scheduler task.
     */
    SchedulerTask createScheduler(final SchedulerTask runnable, final long delay);

    /**
     * Creates a sync repeating scheduler task.
     *
     * @param runnable The task to run.
     * @return The created scheduler task.
     */
    SchedulerTask createScheduler(final SchedulerTask runnable, final long delay, final long period);


    /**
     * Creates an async scheduler task.
     *
     * @param runnable The task to run.
     * @return The created scheduler task.
     */
    SchedulerTask createAsyncScheduler(final SchedulerTask runnable);

    /**
     * Creates an async delayed scheduler task.
     *
     * @param runnable The task to run.
     * @return The created scheduler task.
     */
    SchedulerTask createAsyncScheduler(final SchedulerTask runnable, final long delay);

    /**
     * Creates an async repeating scheduler task.
     *
     * @param runnable The task to run.
     * @return The created scheduler task.
     */
    SchedulerTask createAsyncScheduler(final SchedulerTask runnable, final long delay, final long period);
}
