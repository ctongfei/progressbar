package me.tongfei.progressbar

import scala.collection.mutable

/**
 * @author Tongfei Chen (ctongfei@gmail.com).
 */
object ProgressBarTest {

  def main(args: Array[String]) = {
    val pb = new ProgressBar("Test", 100, 50)
    pb.start()
    for (i ← 0 until 100) {
      val x = Array.tabulate(1000, 1000)((i, j) => i + 0.1324 * j)
      pb.step()
    }
  }

}
