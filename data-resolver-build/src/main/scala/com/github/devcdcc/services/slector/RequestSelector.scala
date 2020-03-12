package com.github.devcdcc.services.slector

import com.github.devcdcc.config
import com.github.devcdcc.controller.NavigationController
import io.circe.Json

import com.github.devcdcc.domain._
import io.circe.syntax._

class RequestSelector(
    navigationController: NavigationController,
    paramConfig: config.JobConfig
) {
  def selection: Json = {

    val json = paramConfig.getParameter match {
      case config.DeviceIdsEmptyParameters() =>
        navigationController.getSpanishDeviceIds.asJson
      case config.GetByDeviceIdParameter(id) =>
        navigationController.searchById(id).asJson
      case config.RangeParameter(start, end) =>
        navigationController.searchByDate(start, end).asJson
      case _ => throw new Exception("Wrong parameters values")
    }
    json
  }
}
