package com.github.devcdcc.services

import com.github.devcdcc.domain._
import org.apache.spark.sql.Row

package object adapter {
  sealed trait AbstractRowAdapter[T] {
    def convert(row: Row): T
  }
  object NavigationRowAdapter extends AbstractRowAdapter[Navigation] {
    override def convert(row: Row): Navigation = {
      Navigation(
        device_id = row.getString(0).toLong,
        timestamp = row.getString(1).toLong,
        url = row.getString(2),
        country = row.getString(3)
      )
    }
  }
  object LanguageRowAdapter extends AbstractRowAdapter[Language] {
    override def convert(row: Row): Language = Language(
      country = row.getString(0),
      language = row.getString(1)
    )
  }
}
