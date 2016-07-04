import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
 * I would like to see all the tokens created for a Ruby program
 * So that I can understand how the tokenization phase of compiling works for Ruby
 * @author LRG37
 **/

public class RubyTokenizerTest {

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
	// Then I should see a button named Tokenize
	@Test
	public void testTokenizeButtonIsDisplayed() {
			
		// Check that a Button named Tokenize is displayed
		assertEquals("Tokenize", driver.findElement(By.xpath("/html/body/form/p[2]/input[1]")).getAttribute("value"));
	}
	
	// Given that I am on the main page
	// When I Enter an expression in the textarea
	// And Click Tokenize 
	// Then I should see the identifiers tokenized with :on_ident
	@Test
	public void testIdentifiersAreTokenized() {
		
		//Enter a = 10 into the code textArea and Click the Tokenize button
		enterCodeInTextAreaAndClickTokenize("a = 10");
		
		//Assert the tokenized code contains :on_ident for a
		assertTrue(driver.findElement(By.xpath("html/body/p[1]/code")).getText().contains(":on_ident, \"a\""));
	}
	
	// Given that I am on the main page
	// When I Enter an expression having spaces into the textarea
	// And Click Tokenize 
	// Then I should see the spaces tokenized with :on_sp
	@Test
	public void testSpacesAreTokenized() {
		
		// Find the TextArea and enter "c= 5" with a space and Click the Tokenize button
		enterCodeInTextAreaAndClickTokenize("c= 5");
		
		//Assert the tokenized code contains :on_sp for spaces
		assertTrue(driver.findElement(By.xpath("html/body/p[1]/code")).getText().contains(":on_sp, \" \""));
	}
	
	// Given that I am on the main page
	// When I Enter an expression not having spaces into the textarea
	// And Click Tokenize 
	// Then I should see the no spaces tokenized with :on_sp
	@Test
	public void testNoSpacesAreNotTokenized() {
		
		// Find the TextArea and enter "c=5" without spaces and Click the Tokenize button
		enterCodeInTextAreaAndClickTokenize("c=5");
		
		//Assert the tokenized does not code contains :on_sp for no spaces
		assertFalse(driver.findElement(By.xpath("html/body/p[1]/code")).getText().contains(":on_sp, \" \""));
	}
	
	// Given that I am on the main page
	// When I Enter statements with a newline between them into the textArea
	// And Click Tokenize 
	// Then I should see the newline tokenized with :on_nl
	@Test
	public void testNewLinesAreTokenized() {
		
		// Find the TextArea and enter "b = 1\nc = 5" with a newline between them and Click the Tokenize button
		enterCodeInTextAreaAndClickTokenize("b = 1\nc = 5");
		
		//Assert the tokenized code contains :on_nl for newines
		assertTrue(driver.findElement(By.xpath("html/body/p[1]/code")).getText().contains(":on_nl"));
	}
	
	// Given that I am on the main page
	// When I Enter statements with operators into the textArea
	// And Click Tokenize 
	// Then I should see the operators tokenized with :on_op
	@Test
	public void testOperatorsAreTokenized() {
		
		// Find the TextArea and enter "a=1+2" and Click the Tokenize button
		enterCodeInTextAreaAndClickTokenize("a=1+2");
		
		//Assert the tokenized code contains :on_nl for operators = and +
		assertTrue(driver.findElement(By.xpath("html/body/p[1]/code")).getText().contains(":on_op, \"=\""));
		assertTrue(driver.findElement(By.xpath("html/body/p[1]/code")).getText().contains(":on_op, \"+\""));
	}
	
	// Given that I am on the main page
	// When I Enter statements having puts into the textArea
	// And Click Tokenize 
	// Then I should see puts also tokenized with :on_ident
	@Test
	public void testPutsIsTokenized() {
		
		// Find the TextArea and enter "puts Hello World" and Click the Tokenize button
		enterCodeInTextAreaAndClickTokenize("puts \"Hello World\"");
		
		//Assert the tokenized code contains :on_nl for operators = and +
		assertTrue(driver.findElement(By.xpath("html/body/p[1]/code")).getText().contains(":on_ident, \"puts\""));
	}
	
	// Given that I am on the Tokenize page
	// When I look below the tokenized code
	// Then I should see a link labeled "Back"
	@Test
	public void testBackLinkIsDisplayed() {
		
		// Find the TextArea and enter "puts Hello World" and Click the Tokenize button
		enterCodeInTextAreaAndClickTokenize("puts \"Hello World\"");
		
		//Assert the link labeled Back is displayed
		assertTrue(driver.findElement(By.linkText("Back")).isDisplayed());
	}
	
	// Given that I am on the Tokenize page
	// When I Click the Back Link
	// Then I should be taken to the Main Page
	@Test
	public void testClickingBackRedirectsToMainPage(){
		//Setup ChromeDriver since HtmlUnitDriver and FirefoxDriver
		//Had problems resolving History.Back();
		System.setProperty("webdriver.chrome.driver", "D://chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("http://lit-bayou-7912.herokuapp.com/");
		
		// Find the TextArea and enter "puts Hello World" and Click the Tokenize button
		driver.findElement(By.id("code_code")).sendKeys("puts \"Hello World\"");
		driver.findElement(By.xpath("/html/body/form/p[2]/input[1]")).click();
		
		try {
			//Find the Back Link and Click
			driver.findElement(By.linkText("Back")).click();
			
			//Assert the current URl is the Main Pages URL
			assertEquals("http://lit-bayou-7912.herokuapp.com/",driver.getCurrentUrl());
		} catch (NoSuchElementException nseex) {
			fail();
		} 
	}

	//Reusable code for entering code into the textArea and clicking Tokenize
	private void enterCodeInTextAreaAndClickTokenize(String code) {
		
		//Find the textArea and enter the string code
		driver.findElement(By.id("code_code")).sendKeys(code);
		
		// Find and click the Tokenize button
		driver.findElement(By.xpath("/html/body/form/p[2]/input[1]")).click();
	}
}
