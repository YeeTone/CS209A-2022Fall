package lab5;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class ByteWriter {

	public static void main(String[] args) throws URISyntaxException {

		try (FileOutputStream fos = new FileOutputStream("bytewriter_output.txt")){

			String s = "计算机系统";
			byte[] bytes = s.getBytes();

			fos.write(bytes);
			fos.flush();//fos.close();
		} catch (FileNotFoundException e) {
			System.out.println("The pathname does not exist.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Failed or interrupted when doing the I/O operations");
			e.printStackTrace();
		}

	}

}
