package ShtrafyOnline

import Bot.Bot
import Config.GetConfig


class ShtrafyOnlineService {

    fun service() {
        val token = ShtrafyOnlineSteps().authorize()
        val grzNumber = GetConfig().getGrzNum()
        val grzReg = GetConfig().getGrzRegion()
        val sts = GetConfig().getSTS()
        val isSuccessMessage = GetConfig().isSuccessMessage()

        if (token != "service failed") {
            val reqsIdParams = mapOf(
                "v" to "2",
                "access_token" to token,
                "auto_number" to grzNumber,
                "method" to "reqs/new/auto",
                "region" to grzReg,
                "registration_full" to sts,
                "v" to "2"
            )
            val reqsId = ShtrafyOnlineSteps().getRrqsId(reqsIdParams)
            if (reqsId != "service failed") {
                val billsParams = mapOf(
                    "v" to "2",
                    "access_token" to token,
                    "method" to "reqs/fines/check",
                    "reqs_id" to reqsId
                )
                val bills = ShtrafyOnlineSteps().getBills(billsParams)
                if(bills == "service failed") Bot().sendMessage("Штрафы Онлайн. недоступен")
                else {
                    if (bills.toInt() == 0) Bot().sendMessage(
                        "Штрафы Онлайн. GIS GMP unavailable"
                    )
                    else {
                        if(isSuccessMessage) Bot().sendMessage("Штрафы Онлайн. Штрафы ищутся")
                    }
                }
            }
            else Bot().sendMessage("Штрафы Онлайн. недоступен")


        }
    }
}