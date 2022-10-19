package lab5;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Paths;


public class ByteReader {

	public static void main(String[] args) throws URISyntaxException {

		//URI uri = ByteReader.class.getClassLoader().getResource("D:\\IdeaProjects\\CS209AMaven\\src\\main\\java\\lab5\\sample.txt").toURI();
		String filePath = Paths.get("D:\\IdeaProjects\\CS209AMaven\\src\\main\\java\\lab5\\sample.txt").toString();


		try (FileInputStream fis = new FileInputStream(filePath)){
			
			byte[] buffer = new byte[65535];

			int byteNum = fis.read(buffer);
			System.out.println("Total number of bytes being read: " + byteNum);

			System.out.print("File content in unsigned int: ");
			for(int i = 0; i < byteNum; i++){
				// byte to unsigned int (rather than signed)
				System.out.printf("%d ", Byte.toUnsignedInt(buffer[i]));
			}
			System.out.println();

			System.out.print("File content in hexdecimal: ");
			for(int i = 0; i < byteNum; i++){
				// lowercase hex
				System.out.printf("%02x ",buffer[i]);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("The pathname does not exist.");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Failed or interrupted when doing the I/O operations");
			e.printStackTrace();
		}
		
	}

}
