package gibdd.rf

import Config.GetConfig
import framework.selenium.wait
import selenium.applyDesiredCapabilities
import selenium.deleteAllCookies
import selenium.webDriver

class GIBDDService {


    fun gibddService() {
        val grzNumber = GetConfig().getGrzNum()
        val grzReg = GetConfig().getGrzRegion()
        val sts = GetConfig().getSTS()
        val isSuccessMessage = GetConfig().isSuccessMessage()
        try {
            applyDesiredCapabilities { }
            webDriver.get("https://xn--90adear.xn--p1ai/check/fines")
            with(Blocks()) {
                grzNum.sendKeys(grzNumber)
                grzRegion.sendKeys(grzReg)
                stsNum.click()
                stsNum.sendKeys(sts)

            }
            try {
                Blocks().searchButton.click()
                Thread.sleep(2000L)
                Blocks().searchButton.click()
            } catch (e: Exception) {
            }
            wait(20)
            {
                webDriver.pageSource.contains("ГРЗ: $grzNumber, СТС: $grzReg")
            }
            if (webDriver.pageSource
                    .contains("Штраф №")
            ) {
                if(isSuccessMessage)Bot.Bot().sendMessage("ГИБДД.рф Штрафы ищутся")
            }
            else Bot.Bot().sendMessage("ГИБДД.рф GIS GMP unavailable")
            webDriver.deleteAllCookies()
            webDriver.close()
        } catch (e: Exception) {
            Bot.Bot().sendMessage("ГИБДД.рф недоступен")
            webDriver.close()
        }
    }
}