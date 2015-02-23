package me.tongfei.progressbar

import java.time._

/**
 * A simple console-based progress bar.
 *
 * @param task Name of the progress bar
 * @param max Number of steps when the task is complete.
 * @param length The length of the progress bar shown in console. Default value is 50 characters.
 * @author Tongfei Chen (ctongfei@gmail.com).
 */
class ProgressBar(val task: String, val max: Int, val length: Int = 40) {

  private[this] var current = 0
  private[this] var startTime: LocalDateTime = null
  private[this] var lastTime: LocalDateTime = null


  private[this] def repeat(x: Char, n: Int): String = {
    new String(Array.fill[Char](n)(x))
  }

  private[this] def progress = math.round(current.toDouble / max * length).toInt

  private[this] def formatDuration(d: Duration): String = {
    val s = d.getSeconds
    "%d:%02d:%02d".format(s / 3600, (s % 3600) / 60, s % 60)
  }

  private[this] def eta(elapsed: Duration) = {
    if (current == 0) "?"
    else formatDuration(elapsed.dividedBy(current).multipliedBy(max - current))
  }


  private[this] def forceShow(currentTime: LocalDateTime): Unit = {
    print('\r')
    val elapsed = Duration.between(startTime, currentTime)
    lastTime = currentTime
    print(task + " [" + repeat('=', progress) + repeat(' ', length - progress) + "] " + math.round(current.toDouble / max * 100) + "% " +
      "(" + formatDuration(elapsed) + " / " + eta(elapsed) + ")    ")
  }

  private[this] def show(): Unit = {
    val currentTime = LocalDateTime.now
    if (Duration.between(lastTime, currentTime).getSeconds < 1) return
    forceShow(currentTime)
  }

  /**
   * Starts the progress bar.
   */
  def start() = {
    startTime = LocalDateTime.now
    lastTime = LocalDateTime.now
    show()
  }

  /**
   * Advances the progress bar by a specific amount.
   * @param n Steps
   */
  def stepBy(n: Int) = {
    current += n
    show()
  }

  /**
   * Advances the progress bar by one step.
   */
  def step() = {
    current += 1
    show()
  }

  /**
   * Stops the progress bar.
   */
  def stop() = {
    forceShow(LocalDateTime.now)
    println()
  }


}
