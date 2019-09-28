import Config.GetConfig
import ShtrafyOnline.ShtrafyOnlineService


object App {

    @JvmStatic
    fun main(args: Array<String>) {
        val interval = GetConfig().getPollingInterval()
        while (true) {
            try {
                gibdd.rf.GIBDDService().gibddService()
                ShtrafyOnlineService().service()
                Thread.sleep(interval)
            } catch (e: Exception) {
            }
        }
    }
}