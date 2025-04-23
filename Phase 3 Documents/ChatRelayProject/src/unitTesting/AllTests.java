package unitTesting;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)

@SuiteClasses({
	PacketTesting.class, ServerTesting.class, ClientTesting.class
})

public class AllTests {
}