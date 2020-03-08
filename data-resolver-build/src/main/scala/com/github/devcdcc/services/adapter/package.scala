package com.github.devcdcc.services

import com.github.devcdcc.domain._
import org.apache.spark.sql.Row

package object adapter {
  sealed trait AbstractRowAdapter[T] {
    def convert(row: Row): T
  }
  object NavigationRowAdapter extends AbstractRowAdapter[Navigation] {
    override def convert(row: Row): Navigation = Navigation(
      device_id = DataDeviceID(row.getLong(0)),
      timestamp = DataTimestamp(row.getLong(1)),
      url = DataURL(row.getString(2)),
      country = DataCountry(row.getString(3))
    )
  }
  object LanguageRowAdapter extends AbstractRowAdapter[Language] {
    override def convert(row: Row): Language = Language(
      country = DataCountry(row.getString(0)),
      language = DataLanguage(row.getString(1))
    )
  }
}
