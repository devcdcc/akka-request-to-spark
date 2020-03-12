package com.github.devcdcc.controller

import com.github.devcdcc.domain
import com.github.devcdcc.domain.DataLanguage
import com.typesafe.config.Config
import org.apache.spark.sql.SparkSession

class NavigationController(val session: SparkSession, config: Config)
    extends NavigationHelper(config = config) {

  import session.implicits._
  private val languageTarget: String = "sp"

  def searchByDate(start: Long, end: Long): Array[domain.Navigation] = {
    val response = navigationData
      .filter(navigation => {
        println(navigation)
        println(s"${navigation.timestamp} >= $start")
        println(s"${navigation.timestamp} <= $end")
        navigation.timestamp >= start && navigation.timestamp <= end
      })
      .collect()
    println("###########" + response.length)
    println("###########" + response.toSeq)
    response
  }

  def searchById(id: Long): Array[domain.Navigation] =
    navigationData
      .filter(navigation => navigation.device_id == id)
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
      .map(_.device_id)
      .distinct()
      .collect()
}
