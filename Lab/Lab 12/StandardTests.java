package lab12;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS) // check the difference for variable b by uncommenting this annotation
class StandardTests {

    static int a = 100;
    int b = 1;
    String s = "hi";

    @BeforeAll
    static void initAll() {
        System.out.println("--Before all--");
        a = 200;
    }

    @BeforeEach
    void init() {
        System.out.println("--Before each--");
        s = "hello";
    }

    @Test
    void succeedingTest1() {
        System.out.printf("good test 1: a=%d, b=%d, s=%s\n", a, b, s);
        a = 300;
        b = 2;
        s = "bye";
    }

    @Test
    void succeedingTest2() {
        System.out.printf("good test 2: a=%d, b=%d, s=%s\n", a, b, s);
    }

    @Test
    void failingTest() {
        System.out.println("failed test starts");
        assertTrue(false);
        assertTrue(true);
        System.out.println("failed test ends");
    }


    @Test
    void abortedTest() {
        System.out.println("Aborted test starts");
        assumeTrue("abc".contains("Z"));
        assumeTrue(true);
        System.out.println("Aborted test ends");
    }


    @Test
    @Disabled()
    void skippedTest() {
        // not executed
    }

    @AfterEach
    void tearDown() {
        System.out.println("--After each--");
        b *= 3;
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("--After all--");
    }

}