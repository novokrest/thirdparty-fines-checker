package gibdd.rf

import frontend.core.Block

import org.openqa.selenium.By
import selenium.topBlock

class BaseBlock : Block()
class Blocks {
    val grzNum = topBlock<BaseBlock>(
        name = "ГРЗ номер",
        locator = By.id("checkFinesRegnum")
    )
    val grzRegion = topBlock<BaseBlock>(
        name = "ГРЗ регион",
        locator = By.id("checkFinesRegreg")
    )

    val stsNum = topBlock<BaseBlock>(
        name = "Номер СТС",
        locator = By.id("checkFinesStsnum")
    )
    val searchButton = topBlock<BaseBlock>(
        name = "Запросить проверку",
        locator = By.cssSelector("[class='check-space check-for']")
    )

}