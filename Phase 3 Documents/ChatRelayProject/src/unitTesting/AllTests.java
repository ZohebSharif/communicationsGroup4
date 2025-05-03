package unitTesting;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({
	 DBManagerTesting.class, ChatTest.class, MessageTesting.class, UserTest.class
})

public class AllTests {
}