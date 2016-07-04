import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * As a Ruby developer,
 * I would like to see the bytecode for a Ruby program
 * So that I can understand how the final compilation phase of compiling works for Ruby
 * @author LRG37
 **/

public class RubyCompilerTest {

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
	// Then I should see a button named Compile
	@Test
	public void testParseButtonIsDisplayed() {
			
		// Check that a Button named Compile is displayed
		assertEquals("Compile", driver.findElement(By.xpath("/html/body/form/p[2]/input[3]")).getAttribute("value"));
	}
	
	// Given that I am on the main page
	// When I Enter a puts statement into the textArea
	// And Click Compile 
	// Then I should see the complied bytecode for puts
	@Test
	public void testPutsIsCompiled() {
		
		//Enter puts "Hello" into the code textArea and Click the Compile button
		enterCodeInTextAreaAndClickCompile("puts \"Hello\"");
		
		//Assert the bytecode contains the putstring YARV
		assertTrue(driver.findElement(By.xpath("html/body/p[1]/code")).getText().contains("putstring"));
	}
	
	// Given that I am on the main page
	// When I Enter an expression with addition
	// And Click Compile 
	// Then I should see the complied bytecode for +
	@Test
	public void testPlusOperatorIsCompiled() {
		
		//Enter c=3+4 into the code textArea and Click the Compile button
		enterCodeInTextAreaAndClickCompile("c=3+4");
		
		//Assert the bytecode contains the opt_plus YARV
		assertTrue(driver.findElement(By.xpath("html/body/p[1]/code")).getText().contains("opt_plus"));
	}
	
	// Given that I am on the main page
	// When I Enter an expression adding two operands
	// And Click Compile 
	// Then I should see the complied bytecode for the operands
	@Test
	public void testPlusOperandsAreCompiled() {
		
		//Enter c=3+4 into the code textArea and Click the Compile button
		enterCodeInTextAreaAndClickCompile("c=3+4");
		
		//Assert the bytecode contains the putobject YARVs for both operands
		assertTrue(driver.findElement(By.xpath("html/body/p[1]/code")).getText().contains("putobject 3"));
		assertTrue(driver.findElement(By.xpath("html/body/p[1]/code")).getText().contains("putobject 4"));
	}
	
	// Given that I am on the main page
	// When I Enter an expression with minus
	// And Click Compile 
	// Then I should see the complied bytecode for minus
	@Test
	public void testMinusOperatorIsCompiled() {
		
		//Enter c=3-4 into the code textArea and Click the Compile button
		enterCodeInTextAreaAndClickCompile("c=3-4");
		
		//Assert the bytecode contains the opt_minus YARV
		assertTrue(driver.findElement(By.xpath("html/body/p[1]/code")).getText().contains("opt_minus"));
	}
	
	// Given that I am on the main page
	// When I Enter an expression with division
	// And Click Compile 
	// Then I should see the complied bytecode for division
	@Test
	public void testDivisionOperatorIsCompiled() {
		
		//Enter c=4/4 into the code textArea and Click the Compile button
		enterCodeInTextAreaAndClickCompile("c=4/4");
		
		//Assert the bytecode contains the opt_div YARV
		assertTrue(driver.findElement(By.xpath("html/body/p[1]/code")).getText().contains("opt_div"));
	}
	
	// Given that I am on the main page
	// When I Enter an expression with multiplication
	// And Click Compile 
	// Then I should see the complied bytecode for multiplication
	@Test
	public void testMultiplicationOperatorIsCompiled() {
		
		//Enter c=4*4 into the code textArea and Click the Compile button
		enterCodeInTextAreaAndClickCompile("c=4*4");
		
		//Assert the bytecode contains the opt_mult YARV
		assertTrue(driver.findElement(By.xpath("html/body/p[1]/code")).getText().contains("opt_mult"));
	}
	
	// Given that I am on the Compile page
	// When I look below the Compiled Code
	// Then I should see a link labeled "Back"
	@Test
	public void testBackLinkIsDisplayed() {
		
		// Find the TextArea and enter "puts Hello World" and Click the Compile button
		enterCodeInTextAreaAndClickCompile("puts \"Hello World\"");
		
		//Assert the link labeled Back is displayed
		assertTrue(driver.findElement(By.linkText("Back")).isDisplayed());
	}
	
	// Given that I am on the Compile page
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
		
		// Find the TextArea and enter "puts Hello World" and Click the Compile button
		driver.findElement(By.id("code_code")).sendKeys("puts \"Hello World\"");
		driver.findElement(By.xpath("/html/body/form/p[2]/input[3]")).click();
		
		try {
			//Find the Back Link and Click
			driver.findElement(By.linkText("Back")).click();
			
			//Assert the current URl is the Main Pages URL
			assertEquals("http://lit-bayou-7912.herokuapp.com/",driver.getCurrentUrl());
		} catch (NoSuchElementException nseex) {
			fail();
		} 
	}
	
	//Reusable code for entering code into the textArea and clicking Compile
	private void enterCodeInTextAreaAndClickCompile(String code) {
		
		//Find the textArea and enter the string code
		driver.findElement(By.id("code_code")).sendKeys(code);
		
		// Find and click the Parse button
		driver.findElement(By.xpath("/html/body/form/p[2]/input[3]")).click();
	}

}
