package me.tongfei.progressbar


/**
 * @author Tongfei Chen (ctongfei@gmail.com).
 */
object ProgressBarTest {

  def main(args: Array[String]) = {
    val pb = new ProgressBar("Test", 0)
    pb.start()
    pb.setExtraMessage("xxxxxxxxxxx")
    for (i ← 0 until 1000) {
      val x = Array.tabulate(1000, 1000)((i, j) => i + 0.1324 * j)
      pb.step()
      if (i == 300) {
        pb.setExtraMessage("a")
        pb.maxHint(1000)
      }
    }
    pb.stop()
    println("Success!")
  }

}
