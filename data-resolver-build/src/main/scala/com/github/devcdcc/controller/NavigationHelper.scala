package com.github.devcdcc.controller

import com.github.devcdcc.domain
import com.github.devcdcc.services.adapter.{
  LanguageRowAdapter,
  NavigationRowAdapter
}
import com.typesafe.config.Config
import org.apache.spark.sql.{Dataset, SparkSession}

abstract class NavigationHelper(config: Config) {
  val session: SparkSession

  import session.implicits._

  private val format = "csv"
  val languageData: Dataset[domain.Language] = {
    session.read
      .format(format)
      //      .option("header", "true")
      .load("/data/language.csv")
      //      .load(config.getString("files.language"))
      .map(LanguageRowAdapter.convert)
  }
  val navigationData: Dataset[domain.Navigation] =
    session.read
      .format(format)
      //      .option("header", "true")
      .load("/data/navigation.csv")
      //      .load(config.getString("files.navigation"))
      .map(NavigationRowAdapter.convert)
}
