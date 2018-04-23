

public class StopWatch {
	/** task description (if needed)*/
	protected String descrptionOfTask; 
	/** For storing the time */
	protected long time = 0;
	
	/** Is the watch running? */
	protected boolean running = false;
	
	/** Constructor, no output */
	public StopWatch()
	{
	}
	
	/** Start the stop watch (without task description) */
	public StopWatch start()
	{
		return start(null);
	}

	/** Start the stop watch, store task description (may be {@code null}) */
	public StopWatch start(String taskDescription)
	{
		this.descrptionOfTask = taskDescription;
		running = true;
		time = System.currentTimeMillis();
		return this;
	}

	/**
	 * Stop the stop watch.
	 * If a task description and a log was given, output elapsed time.
	 * @return elapsed time in milliseconds
	 */
	public long stop()
	{
		return stop(null);
	}

	/**
	 * Stop the stop watch, optionally taking extra text for output.
	 */
	public long stop(String extraText) {
		time = System.currentTimeMillis() - time;
		running = false;
		if (descrptionOfTask != null) {
			System.out.println("Time for " + descrptionOfTask + ": " + elapsedSeconds() + " seconds");
			if (extraText != null) {
				System.out.println(", " + extraText);
			}
			System.out.println(".");
		} else if (extraText != null) {
			System.out.println("Time: " + elapsedSeconds() + " seconds, " + extraText + ".");
		}
		return time;
	}
	/** Get the number of elapsed milliseconds (fixed value after having called stop). */
	public long elapsedMillis()
	{
		return running ? System.currentTimeMillis() - time : time;
	}

	/** Get the number of elapsed seconds (fixed value after having called stop). */
	public double elapsedSeconds()
	{
		return elapsedMillis() / 1000.0;
	}
	/**
	 * Stop the execution time of a task.
	 *
	 * @return time in milliseconds
	 **/
	public long run(Runnable task)
	{
		return run(task, null, null);
	}

	/**
	 * Stop the execution time of a task.
	 *
	 * @param taskDescription description or {@code null})
	 * @param extraText text or {@code null}
	 * @return time in milliseconds
	 **/
	public long run(Runnable task, String taskDescription, String extraText)
	{
		start(taskDescription);
		task.run();
		return stop(extraText);
	}
}
