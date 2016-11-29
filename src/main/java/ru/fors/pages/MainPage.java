package ru.fors.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.concurrent.*;

/**
 * Class representing the main page of "СУЭ"
 */
public class MainPage extends Page {

    private By infopanelMenu = /*By.id("ROOT/Избранные объекты и инфопанели");*/By.xpath("//span[text()='Избранные объекты и инфопанели']");
    private By searchScript;
    private By saveButton = By.xpath("//button[text()='Сохранить']");
    private By comboUpButton = By.id("X3Button");
    private By comboUpSolveButton = By.id("X68Button");
    private By comboUpClosureCodeButton = By.id("X92Button");
    private By inWorkStatus = By.xpath("//div[text()='В работе']");
    private By solvedStatus = By.xpath("//div[text()='Решен']");
    private By permanentStatus = By.xpath("//div[text()='Постоянное']");
    private By solvedOnSecondLine = By.xpath("//div[text()='Решено на 2-й линии']");
    private By inWaitButton = By.xpath("//button[text()='В ожидание']");
    private By returnBackButton = By.xpath("//button[text()='Возврат оператору']");
    private By statusField = By.id("X3");
    private By solveTypeField = By.id("X68");
    private By closureCodeField = By.id("X92");
    private By activityField = By.id("X37");
    private By solveField = By.id("X74");
    private By logoutButton = By.linkText("Выход");
    private By loginAgainLink = By.id("loginAgain");
    private By moreIconButton = By.cssSelector("button[class*='more-icon']");
    private By inWaitButton2 = By.xpath("//span[text()='В ожидание']");
    private By inWaitReasonSelectButton = By.id("X6FillButton");
    private By incidentDiagnosticLink = By.linkText("диагностика инцидента");
    private By commentField = By.id("X11");
    private By saveIncidentButton = By.xpath("//button[text()='Готово']");
    private String message;
    private By firstColumnInTable = By.className("firstColumnColor");
    private By firsRowInTable = By.xpath("//tbody[@role='presentation']//td[2]/div");
    private By incidentReturnFrame = By.xpath("//iframe[contains(@title,'Возврат Инцидента')]");
    private By currentTasksFrame = By.xpath("//iframe[contains(@title,'Текущие задачи')]");
    private By oldIncidents = By.xpath("//iframe[contains(@title,'Открытые до 01.09.2016')]");
    private By okButtonsPath = By.xpath("//button[text()='ОК']");
    private By sendToReleaseLink = By.linkText("внесено в релиз");
    private By noButtonPath = By.xpath("//input[@value='Нет']");

    public MainPage(WebDriver driver, String representation) {
        super(driver);
        searchScript = By.xpath("//span[text()='" + representation + "']");
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void waitUntilMainPageLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(infopanelMenu));
    }

    public void userClickInfopanelMenu() {
        switchToDefaultContent();
        click(infopanelMenu);
        waitUntilElementPresent(searchScript);
    }

    public void userClickSearchScript() {
        click(searchScript);
        try {
            waitUntilFrameToBeAvaibleAndSwitchToIt(1);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        waitUntilElementPresent(saveButton);
    }

    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    private void userClickSaveButton() {
        click(saveButton);
    }

    private void userClickLogoutButton() {
        switchToDefaultContent();
        click(logoutButton);
    }

    public void userLogout() {
        userClickLogoutButton();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();
        waitUntilElementPresent(loginAgainLink);
    }

    private void userClickComboUpButton() {
        click(comboUpButton);
    }

    private void userClickComboUpSolveButton() {
        click(comboUpSolveButton);
    }

    private void userClickComboUpClosureCodeButton() {
        click(comboUpClosureCodeButton);
    }

    private void userClickInWorkStatus() {
        click(inWorkStatus);
    }

    private void userClickSolvedStatus() {
        click(solvedStatus);
    }

    private void userClickPermanentStatus() {
        click(permanentStatus);
    }

    private void userClickSolvedOnSecondLine() {
        click(solvedOnSecondLine);
    }

    public void openFrame() {
        waitUntilFrameToBeAvaibleAndSwitchToIt(1);
    }

    public void userChangeStatus() {
        userClickComboUpButton();
        userClickInWorkStatus();
        waitUntilElementSetValue(statusField, "В работе");
    }

    public void userChangeStatusSolved() {
        userClickComboUpButton();
        userClickSolvedStatus();
        waitUntilElementSetValue(statusField, "Решен");
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    public void changeActivity(String activityText) {
        type(activityField, activityText);
    }

    public void userSaveIncident() {
        waitUntilFrameToBeAvaibleAndSwitchToIt(1);
        userClickSaveButton();
        //waitUntilElementPresent(returnBackButton);
        switchToDefaultContent();
        waitUntilFrameToBeAvaibleAndSwitchToIt(1);
    }

    public void userSaveActivityChange() {
        waitUntilFrameToBeAvaibleAndSwitchToIt(1);
        userClickSaveButton();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
    }

    private void userClickMoreIconButton() {
        click(moreIconButton);
    }

    private void userClickInWaitButton2() {
        click(inWaitButton2);
    }

    public void userSetIncidentToInWork() {
        if (findIfElementVisible(inWaitButton)) {
            userClickInWaitButton();
        } else {
            userClickMoreIconButton();
            userClickInWaitButton2();
        }
        userClickInWaitReasonSelectButton();
        userClickIncidentDiagnosticLink();
        userTypeIncidentComment("диагностика инцидента");
        userClickSaveIncidentButton();
    }

    public void userSetIncidentToInWork2() {
        if (findIfElementVisible(inWaitButton)) {
            userClickInWaitButton();
        } else {
            userClickMoreIconButton();
            userClickInWaitButton2();
        }
        userClickInWaitReasonSelectButton();
        // TODO: 09.11.2016 помойму поменять вот эти 2 строчки
        userClickSendToReleaseLink();
        userTypeIncidentComment("внесено в релиз");
        ///////////////////////////////////////////////////////
        userClickSaveIncidentButton();
    }

    private void userClickSendToReleaseLink() {
        click(sendToReleaseLink);
        waitUntilElementPresent(commentField);
    }

    public void userSetIncidentSolutionType() {
        userClickComboUpSolveButton();
        userClickPermanentStatus();
        waitUntilElementSetValue(solveTypeField, "Постоянное");
    }

    public void userTypeSolveText(String solve) {
        type(solveField, solve);
    }

    public void setSolvedOnSecondLine() {
        userClickComboUpClosureCodeButton();
        userClickSolvedOnSecondLine();
        waitUntilElementSetValue(closureCodeField, "Решено на 2-й линии");
    }

    private void userClickInWaitButton() {
        click(inWaitButton);
    }

    private void userClickInWaitReasonSelectButton() {
        click(inWaitReasonSelectButton);
    }

    private void userClickIncidentDiagnosticLink() {
        click(incidentDiagnosticLink);
        waitUntilElementPresent(commentField);
    }

    private void userTypeIncidentComment(String s) {
        type(commentField, s);
    }

    private void userClickSaveIncidentButton() {
        click(saveIncidentButton);
        waitUntilElementPresent(saveButton);
    }

    public void getAndClickIncidentNumber() {
        //waitUntilFrameToBeAvaibleAndSwitchToIt(0);
        System.out.println("Next incident: " + getElementText(firsRowInTable));
        click(firsRowInTable);
        switchToDefaultContent();
        waitUntilFrameToBeAvaibleAndSwitchToIt(1);
    }

    public boolean isIncidentExist() {
        waitUntilFrameToBeAvaibleAndSwitchToIt(0);
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(firsRowInTable));
            //waitUntilFrameToBeAvaibleAndSwitchToIt(1);
            return true;
        } catch (Exception e) {
            //waitUntilFrameToBeAvaibleAndSwitchToIt(1);
            return false;
        }
    }

    public void startReturn(){

        driver.switchTo().defaultContent();
        driver.switchTo().frame(driver.findElement(currentTasksFrame));
        // У нас появляется список инцидентов. При успешном возврате инцидента этот метод будет перезапускаться
        // и каждый раз будет генериться новый лист


        //driver.switchTo().parentFrame();
        driver.findElements(By.xpath("//img[@title='Развернуть список']")).get(1).click();
        driver.findElement(By.xpath("//div[text()='Текущие задачи моей Команды']")).click();
        driver.findElement(By.xpath("//span/label[text()='Представление:']")).click();
        //waitUntilElementSetValue(, "В работе");
        //// TODO: 08.11.2016 add exception in case of timeout
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstColumnInTable));


        while (findIfElementVisible(By.xpath("//*[@id='ext-gen25']"))) {
        //лист инцидентов
            List<WebElement> ar = driver.findElements(firstColumnInTable);
            int pos = 0;

            while(pos < ar.size()) {
                try {
                    if (!ar.get(pos).isDisplayed()) {
                        driver.switchTo().defaultContent();
                        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(currentTasksFrame));
                        driver.switchTo().frame(driver.findElement(currentTasksFrame));
                    }
                    //кликаем на инцидент
                    if ( ar.get(pos).getText().equals("Открыт") || ar.get(pos).getText().equals("В работе")) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ar.get(pos));
                    } else {
                        pos++;
                        continue;
                    }
                    //меняем статус
                    userChangeStatus();
                    //это вроде работает
                    switchToDefaultContent();
                    //сохраняем
                    click(By.xpath("//button[text()='Сохранить']"));
                    click(By.xpath("//button[text()='Возврат оператору']"));
                    driver.switchTo().defaultContent();
                    if (driver.findElements(incidentReturnFrame).size() != 0) {
                        driver.switchTo().frame(driver.findElement(incidentReturnFrame));
                    } else {
                        throw new IllegalStateException("Can not switch to 'return' frame!");
                    }
                    List<WebElement> list = driver.findElements(By.tagName("textarea"));
                    WebElement textArea = list.get(0);
                    //wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("textarea")));
                    textArea.clear();
                    textArea.sendKeys(message);
                    //нажимаем готово
                    click(By.xpath("//button[text()='Готово']"));
                    break;
                } catch (Exception e){
                    e.printStackTrace();
                    //закрывем инцидент если чет пошло не так
                    if (driver.findElements(By.className("x-tab-strip-close")).size() != 0) {
                        click(By.className("x-tab-strip-close"));
                    }
                    pos++;
                }
            }
            click(By.xpath("//*[@id='ext-gen25']"));
        }
    }

    void switchToStepByStep(By... paths){
        driver.switchTo().defaultContent();
        for (By path: paths){
            driver.switchTo().frame(driver.findElement(path));
        }
    }


    public void relevanceRequest(){
        final By subFrame = By.xpath("//iframe[starts-with(@src,'../detail.do')]");

        driver.switchTo().defaultContent();
        click(infopanelMenu);
        searchScript = By.xpath("//span[text()='" + "Открытые до 01.09.2016" + "']");
        waitUntilElementPresent(searchScript);
        click(searchScript);

        driver.switchTo().frame(driver.findElement(oldIncidents));
        waitUntilElementPresent(firstColumnInTable);

        //Здесь именно через tbody потому что на этой странице будет другое представление списка инцидентов
        driver.findElements(firstColumnInTable).get(0).click();
        try {
            final By tableFrame = By.xpath("//iframe[starts-with(@src,'../list.do')]");
            wait.until(ExpectedConditions.visibilityOfElementLocated(tableFrame));
            driver.switchTo().frame(driver.findElement(tableFrame));
            while (findIfElementVisible(By.xpath("//*[@id='ext-gen28']"))) {
                int pos = 0;
                while (pos <  driver.findElements(By.xpath("//div[@role='row']")).size()){
                    try {
                        switchToDefaultContent();
                        if ((driver.findElements(okButtonsPath).size() != 0)
                                && driver.findElement(okButtonsPath).isDisplayed()) {
                            click(okButtonsPath);
                        }
                        switchToStepByStep(oldIncidents,tableFrame);
                        WebElement incident = driver.findElements(By.xpath("//div[@role='row']")).get(pos);
                        incident.click();
                        try{
                            switchToStepByStep(By.xpath("//iframe[starts-with(@src,'/sm934/cwc')]"));
                            if ((driver.findElements(noButtonPath).size() != 0)
                                    && driver.findElement(noButtonPath).isDisplayed()) {
                                click(noButtonPath);
                            }
                        }catch (Exception e){
                            System.out.println("As expected...");
                         }
                        switchToStepByStep(oldIncidents, subFrame);
                        waitUntilElementPresent(comboUpButton);
                        userChangeStatus();
                        driver.findElement(By.xpath("//label[text()='Код инцидента / Статус:']")).click();
                        switchToDefaultContent();
                        try {
                            driver.findElement(By.xpath("//button[text()='ОК']")).click();
                            continue;
                        } catch (Exception e){
                            System.out.println("As expected...");
                        }
                        int i = 0;
                        switchToStepByStep(oldIncidents);
                        try{
                            shortWait.until(ExpectedConditions.visibilityOfElementLocated(saveButton));
                            driver.findElement(saveButton).click();
                        } catch (Exception e) {
                            switchToDefaultContent();
                            wait.until(ExpectedConditions.visibilityOfElementLocated(okButtonsPath));
                            if (driver.findElements(okButtonsPath).size() > 0){
                                List<WebElement> buttons = driver.findElements(okButtonsPath);
                                buttons.get(0).click();
                                //driver.findElement(By.xpath("//button[text()='Отмена']")).click();
                                //throw new IllegalStateException("Can not change status");
                            }
                            switchToStepByStep(oldIncidents);
                        }
                        driver.switchTo().defaultContent();
                        driver.switchTo().frame(driver.findElement(oldIncidents));
                        if (driver.findElements(subFrame).size() != 1) {
                            throw new IllegalStateException("Can not find subframe");
                        }
                        driver.switchTo().frame(driver.findElement(subFrame));

                        driver.findElement(By.xpath("//a[contains(@title,'Запрос информации у Пользователя')]")).click();
                        //click(By.xpath("//a[contains(@title)]"));
                        // ждем появления нового комбобокса
                        waitUntilElementPresent(By.id("X201Button"));
                        click(By.id("X201Button"));
                        //выбираем значение выпадающего списка
                        final By inInteraction = By.xpath("//div[text()='Взаимодействие с Пользователем']");
                        driver.findElement(inInteraction).click();
                        waitUntilElementSetValue(By.xpath("//input[@id='X201']"), "Взаимодействие с Пользователем");
                        switchToStepByStep(oldIncidents, subFrame);
                        //click(inInteraction);
                        //жмакаем на лейбл, дабы убрать список
                        click(By.xpath("//label[@for='X201']"));

                        final By textarea = By.xpath("//textarea[@id='X205']");
                        switchToStepByStep(oldIncidents, subFrame);
                        System.out.println(driver.findElement(textarea).getAttribute("id"));
                        waitUntilElementPresent(textarea);
                        //заполняем текст арию
                        type(textarea, message);
                        //сохраняем инцидент
                        switchToStepByStep(oldIncidents);
                        click(saveButton);
                    } catch (Exception e){
                        e.printStackTrace();
                    } finally {
                        pos++;
                        switchToStepByStep(oldIncidents,tableFrame);
                    }
                }
                click(By.xpath("//*[@id='ext-gen28']"));
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }


}