package com.github.devcdcc

object ReaderJobMain {

  def main(args: Array[String]): Unit = {
    val job = new JobDefinition(args)
    job.execute()
  }
}
