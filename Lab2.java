package cn.tju.selenium;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
public class Lab2 {
	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "https://psych.liebes.top/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testScript() throws Exception {

		try {
			File writename = new File("output.txt");
			writename.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write("   学号                  原URL                                excel表中URL\r\n");
			out.flush();
			File src = new File("input.xlsx");
			FileInputStream fis = new FileInputStream(src);
			@SuppressWarnings("resource")
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			XSSFSheet sh1 = wb.getSheetAt(0);
			for (int i = 0; i < sh1.getPhysicalNumberOfRows(); i++) {
				driver.get(baseUrl + "/st");
				driver.findElement(By.id("username")).clear();
				driver.findElement(By.id("username")).sendKeys(
						sh1.getRow(i).getCell(0).getStringCellValue());
				driver.findElement(By.id("password")).clear();
				driver.findElement(By.id("password")).sendKeys(
						sh1.getRow(i).getCell(0).getStringCellValue()
								.substring(4));
				driver.findElement(By.id("submitButton")).click();
				if (sh1.getRow(i)
						.getCell(1)
						.getStringCellValue()
						.trim()
						.equals(driver
								.findElement(By.cssSelector("p.login-box-msg"))
								.getText().trim())) {
					System.out.println("Success-"
							+ sh1.getRow(i).getCell(0).getStringCellValue());
				} else {
					System.out.println("Failed-"
							+ sh1.getRow(i).getCell(0).getStringCellValue());
					out.write(sh1.getRow(i).getCell(0).getStringCellValue()
							+ "     "
							+ String.format(
									"%-40s",
									driver.findElement(
											By.cssSelector("p.login-box-msg"))
											.getText())
							+ String.format("%-40s", sh1.getRow(i).getCell(1)
									.getStringCellValue()) + "\r\n");
					out.flush();
				}

			}
			out.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
