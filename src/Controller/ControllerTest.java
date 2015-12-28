/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;





/**
 *
 * @author hallm8
 */
public class ControllerTest {
    
    public void runTest(){

        WebDriver driver = new ChromeDriver();
        driver.get("http://google.com");

    }
    
}
