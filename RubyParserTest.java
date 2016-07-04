import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * As a Ruby developer,
 * I would like to see the AST(parse tree) for a Ruby program
 * So that I can understand how the parsing phase of compiling works for Ruby
 * @author LRG37
 **/

public class RubyParserTest {

static WebDriver driver = new HtmlUnitDriver();
	
	// Start at the home page for the online Ruby compilation visualizer for each test
	@Before
	public void setUp() throws Exception {
		driver.get("http://lit-bayou-7912.herokuapp.com/");
	}
	
	// Given that I am on the main page
	// When I view the screen
	// Then I should see a Label named Code
	@Test
	public void testCodeLabelIsDisplayed() {
		
		// Check that a label named Code is displayed
		assertEquals("Code",driver.findElement(By.tagName("label")).getText());
	}
	
	// Given that I am on the main page
	// When I view the screen
	// Then I should see a TextArea to type my code
	@Test
	public void testTextAreaIsDisplayed() {
			
		//Check that a TextArea is displayed
		assertTrue(driver.findElement(By.id("code_code")).isDisplayed());
	}
	
	// Given that I am on the main page
	// When I view the screen
	// Then I should see a button named Parse
	@Test
	public void testParseButtonIsDisplayed() {
			
		// Check that a Button named Parse is displayed
		assertEquals("Parse", driver.findElement(By.xpath("/html/body/form/p[2]/input[2]")).getAttribute("value"));
	}
	
	// Given that I am on the main page
	// When I Enter an expression with whitespaces in the textArea
	// And Click Parse 
	// Then I should not see the whitespace characters parsed
	@Test
	public void testWhiteSpacesAreNotParsed() {
		
		//Enter "a=10\nb=5" into the code textArea and Click the Parse button
		enterCodeInTextAreaAndClickParse("a = 10+1\nb");
		
		//Assert the parse tree does not contains nl for newline
		assertFalse(driver.findElement(By.xpath("/html/body/p[1]/code")).getText().contains("nl"));
		
		//Assert the parse tree does not contains sp for spaces
		assertFalse(driver.findElement(By.xpath("/html/body/p[1]/code")).getText().contains("sp"));
	}
	
	// Given that I am on the main page
	// When I Enter an expression with operators in the textArea
	// And Click Parse 
	// Then I should see the operators parsed
	@Test
	public void testOperatorsAreParsed() {
		
		//Enter "a=10+11" into the code textArea and Click the Parse button
		enterCodeInTextAreaAndClickParse("a=10+11");
		
		//Assert the parse tree contains +
		assertTrue(driver.findElement(By.xpath("/html/body/p[2]")).getText().contains("+"));
	}
	
	// Given that I am on the main page
	// When I Enter an expression with identifiers in the textArea
	// And Click Parse 
	// Then I should see the identifiers parsed
	@Test
	public void testIdentifiersAreParsed() {
		
		//Enter "a=10+11" into the code textArea and Click the Parse button
		enterCodeInTextAreaAndClickParse("a=10+11");
		
		//Assert the parse tree contains @ident
		assertTrue(driver.findElement(By.xpath("/html/body/p[2]")).getText().contains("@ident"));
	}
	
	// Given that I am on the main page
	// When I Enter a puts statement in the textArea
	// And Click Parse 
	// Then I should see puts parsed
	@Test
	public void testPutsIsParsed() {
		
		//Enter "puts "HelloWorld"" into the code textArea and Click the Parse button
		enterCodeInTextAreaAndClickParse("puts \"HelloWorld\"");
		
		//Assert the parse tree contains puts
		assertTrue(driver.findElement(By.xpath("/html/body/p[2]")).getText().contains("puts"));
	}
	
	// Given that I am on the Parse page
	// When I look below the Parse Tree
	// Then I should see a link labeled "Back"
	@Test
	public void testBackLinkIsDisplayed() {
		
		// Find the TextArea and enter "puts Hello World" and Click the Parse button
		enterCodeInTextAreaAndClickParse("puts \"Hello World\"");
		
		//Assert the link labeled Back is displayed
		assertTrue(driver.findElement(By.linkText("Back")).isDisplayed());
	}
	
	// Given that I am on the Parse page
	// When I Click the Back Link
	// Then I should be taken to the Main Page
	@Test
	public void testClickingBackRedirectsToMainPage(){
		//Used ChromeDriver since HtmlUnitDriver and FirefoxDriver
		//Had problems resolving History.Back();
		//So ensure that the local path of your chromedriver.exe is set here to run this test alone
		System.setProperty("webdriver.chrome.driver", "D://chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("http://lit-bayou-7912.herokuapp.com/");
		
		// Find the TextArea and enter "puts Hello World" and Click the Parse button
		driver.findElement(By.id("code_code")).sendKeys("puts \"Hello World\"");
		driver.findElement(By.xpath("/html/body/form/p[2]/input[2]")).click();
		
		try {
			//Find the Back Link and Click
			driver.findElement(By.linkText("Back")).click();
			
			//Assert the current URl is the Main Pages URL
			assertEquals("http://lit-bayou-7912.herokuapp.com/",driver.getCurrentUrl());
		} catch (NoSuchElementException nseex) {
			fail();
		} 
	}
	
	//Reusable code for entering code into the textArea and clicking Parse
	private void enterCodeInTextAreaAndClickParse(String code) {
		
		//Find the textArea and enter the string code
		driver.findElement(By.id("code_code")).sendKeys(code);
		
		// Find and click the Parse button
		driver.findElement(By.xpath("/html/body/form/p[2]/input[2]")).click();
	}
}
