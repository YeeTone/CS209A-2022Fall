package lab3;

import org.junit.Before;
import org.junit.Test;
import org.junit.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;



public class Practice3Test {

    private final static String a1 = "1 4 2 3 4 5 2 5 2 3 4 5 6 3 4 2 3 4 5 4 4 4 5 6 7 4 1 1 0";

    private final static ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void test(){

        String res = """
                Please input the function No:
                1 - Get even numbers
                2 - Get odd numbers
                3 - Get prime numbers
                4 - Get prime numbers that are bigger than 5
                0 - Quit
                Input size of the list:
                Input elements of the list:
                Filter results:
                [2, 4]
                Please input the function No:
                1 - Get even numbers
                2 - Get odd numbers
                3 - Get prime numbers
                4 - Get prime numbers that are bigger than 5
                0 - Quit
                Input size of the list:
                Input elements of the list:
                Filter results:
                [3, 5]
                Please input the function No:
                1 - Get even numbers
                2 - Get odd numbers
                3 - Get prime numbers
                4 - Get prime numbers that are bigger than 5
                0 - Quit
                Input size of the list:
                Input elements of the list:
                Filter results:
                [2, 3, 5]
                Please input the function No:
                1 - Get even numbers
                2 - Get odd numbers
                3 - Get prime numbers
                4 - Get prime numbers that are bigger than 5
                0 - Quit
                Input size of the list:
                Input elements of the list:
                Filter results:
                [7]
                Please input the function No:
                1 - Get even numbers
                2 - Get odd numbers
                3 - Get prime numbers
                4 - Get prime numbers that are bigger than 5
                0 - Quit
                Input size of the list:
                Input elements of the list:
                Filter results:
                []
                Please input the function No:
                1 - Get even numbers
                2 - Get odd numbers
                3 - Get prime numbers
                4 - Get prime numbers that are bigger than 5
                0 - Quit"""
                ;

        System.setIn(new ByteArrayInputStream(a1.getBytes()));
        Practice3Answer.main(null);
        assertEquals(res.replace("\n", "")
                .replace("\r", ""),outContent.toString().replace("\n", "")
                .replace("\r", ""));
    }
}
