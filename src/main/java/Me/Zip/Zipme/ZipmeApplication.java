package Me.Zip.Zipme;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class ZipmeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZipmeApplication.class, args);
	}

	@Bean
    public CommandLineRunner checkCacheManager(CacheManager cacheManager) {
        return args -> System.out.println("💡 Using CacheManager: " + cacheManager.getClass().getName());
    }

}
