package com.github.devcdcc.controller

import com.github.devcdcc.domain
import com.github.devcdcc.services.adapter.{
  LanguageRowAdapter,
  NavigationRowAdapter
}
import org.apache.spark.sql.{Dataset, SparkSession}

trait NavigationHelper {
  val session: SparkSession

  import session.implicits._

  private val format = "csv"
  val languageData: Dataset[domain.Language] = {
    session.read
      .format(format)
      .option("header", "true")
      .load("some/path/to/file.csv")
      .map(LanguageRowAdapter.convert)
  }
  val navigationData: Dataset[domain.Navigation] =
    session.read
      .format(format)
      .option("header", "true")
      .load("some/path/to/file.csv")
      .map(NavigationRowAdapter.convert)
}
