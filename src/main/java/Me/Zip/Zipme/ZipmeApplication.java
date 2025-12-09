package Me.Zip.Zipme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ZipmeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZipmeApplication.class, args);
	}

}
