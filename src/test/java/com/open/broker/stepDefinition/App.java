package com.open.broker.stepDefinition;

import org.junit.*;


import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.openqa.selenium.winium.WiniumDriverService;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.nio.file.Paths;


public class App {

//    private jdbcShare r;
//    public App(jdbcShare r) {
//        this.r = r;
//    }
//
//    private String schema;
//    private Connection connection;
//
//
//    public static void mai(String[] args) throws SQLException {
//
//        // Connect to database
//        String hostName = "10.48.34.216";
//        String dbName = "bd-vm-tt-titan";
//        String user = "ragimov";
//        String password = "No4noioxotnik";
//        String url = String.format("jdbc:sqlserver://10.48.34.216:1433;integratedSecurity=true;databaseName=opendb");
//
//        try {
//            Connection connection = DriverManager.getConnection(url);
//            String schema = connection.getSchema();
//            System.out.println("Successful connection - Schema: " + schema);
//
//            System.out.println("Query data example:");
//            System.out.println("=========================================");
//
//            // Create and execute a SELECT SQL statement.
//            String selectSql = "select * from planned_transactions pt inner join client cl on pt.source_account_id = cl.client_id inner join servicesopen SO on cl.client_id=so.clientid where cl.client_base_id in (select r.client_id from opentaxes.dbo.taxes_save_running_total_iis_registry_total r where r.iis_client_id in (select cl.client_id from client cl inner join servicesopen SO  on SO.clientid = cl.client_id inner join dogovor d on d.client_id = SO.clientid where SO.serviceid <> 626 and SO.end_date is not null and d.dogovor_type = 643 and d.date_end >='20170101' ) and r.calc_year = 0 ) and so.serviceid = 623 and pt.account = 73 and pt.creation_date >='20171110' and pt.comment like '%i>%'";
//
//            try (Statement statement = connection.createStatement();
//                 ResultSet resultSet = statement.executeQuery(selectSql)) {
//
//                ArrayList<String> cols = new ArrayList<String>();
//                String column = "clientid";
//                while (resultSet.next()) {
//                    cols.add(resultSet.getObject(387).toString());
//                }
//                //  connection.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Test
//    public void getToQueryString() {
//        r.cols.toString();
//    }

@Test
    public void WiniumDriver() throws IOException, InterruptedException {

        String ApplicationPath = String
                .valueOf(Paths.get("src/test/resources/OpenBrokerBO_TEST.lnk"));
        String winiumDriverPath = String
                .valueOf(Paths.get("src/test/resources/Winium.Desktop.Driver.exe"));

        DesktopOptions options = new DesktopOptions(); //Initiate Winium Desktop Options
        options.setApplicationPath(ApplicationPath); //Set outlook application path

        File drivePath = new File(winiumDriverPath); //Set winium driver path

        WiniumDriverService service = new WiniumDriverService
                .Builder()
                .usingDriverExecutable(drivePath)
                .usingPort(9999)
                .withVerbose(true)
                .withSilent(false)
                .buildDesktopService();
        service.start(); //Build and Start a Winium Driver service

        WiniumDriver driver = new WiniumDriver(new URL("http://localhost:9999"), options); //Start a winium driver (service,option)

        options.setDebugConnectToRunningApp(true);
        options.setLaunchDelay(-3);

        WebElement window =  driver.findElementByClassName("WindowsForms10.Window.8.app.0.1085a3f_r14_ad1");
        window.click();
//        String parentHandle = driver.getWindowHandle(); // Save parent window
        driver.findElementByName("Журналы").click();
        driver.findElementByName("Отложенные проводки").click();
        WebElement windowOp =  driver.findElementByName("Журналы - Отложенные проводки");
        windowOp.click();
//        String childHandle = driver.getWindowHandle();
      //  driver.switchTo().window(childHandle);
        driver.findElementByName("Обновить").click();
        WebElement wind = driver.findElementByName("Все");
        wind.click();
        wind.sendKeys("wow!!! it works!");
        Thread.sleep(1000);
        driver.findElementByName("Добавить").click();

        Thread.sleep(5000);
     //   driver.close();
      //  service.stop();
    }


    @Test
    public void WiniumDriverr() throws IOException, InterruptedException {

        String ApplicationPath = String
                .valueOf(Paths.get("src/test/resources/OpenBrokerBO_TEST.lnk"));
        String winiumDriverPath = String
                .valueOf(Paths.get("src/test/resources/Winium.Desktop.Driver.exe"));

        DesktopOptions options = new DesktopOptions(); //Initiate Winium Desktop Options
        options.setApplicationPath(ApplicationPath); //Set outlook application path
        ProcessBuilder service = new ProcessBuilder(winiumDriverPath);
        service.start();

        options.setDebugConnectToRunningApp(false);
        options.setLaunchDelay(2);
        WiniumDriver driver = new WiniumDriver(new URL("http://localhost:9999"), options); //Start a winium driver (service,option)
        Thread.sleep(5000);
        service.start();

        WebElement window =  driver.findElementByClassName("WindowsForms10.Window.8.app.0.1085a3f_r14_ad1");
        window.findElement(By.name("Журналы")).click();
        window.findElement(By.name("Отложенные проводки")).click();
        WebElement windowOp =  driver.findElementByClassName("WindowsForms10.Window.8.app.0.1085a3f_r14_ad1");
        windowOp.findElement(By.name("Обновить")).click();
        windowOp.findElement(By.name("Все")).sendKeys("wow!!! it works!");
        windowOp.findElement(By.name("Добавить")).click();

        Thread.sleep(5000);
        //   driver.close();
        //  service.stop();

    }


}
