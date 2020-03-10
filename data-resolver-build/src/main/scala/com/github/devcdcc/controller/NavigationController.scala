package com.github.devcdcc.controller

import com.github.devcdcc.domain
import com.github.devcdcc.domain.DataLanguage
import com.typesafe.config.Config
import org.apache.spark.sql.SparkSession

class NavigationController(val session: SparkSession, config: Config)
    extends NavigationHelper {

  import session.implicits._
  private val languageTarget: DataLanguage = DataLanguage("sp")

  def searchByDate(start: Long, end: Long): Array[domain.Navigation] =
    navigationData
      .filter(navigation =>
        navigation.timestamp.value >= start && navigation.timestamp.value <= end
      )
      .collect()

  def searchById(id: Long): Array[domain.Navigation] =
    navigationData
      .filter(navigation => navigation.device_id.value == id)
      .collect()

  def getSpanishDeviceIds: Array[Long] =
    navigationData
      .filter(navigation =>
        !languageData
          .filter(_.country == navigation.country)
          .map(language => language.language)
          .filter(lang => lang == languageTarget)
          .isEmpty
      )
      .map(_.device_id.value)
      .distinct()
      .collect()
}
