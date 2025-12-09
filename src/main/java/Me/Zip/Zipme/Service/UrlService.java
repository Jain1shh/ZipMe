package Me.Zip.Zipme.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import Me.Zip.Zipme.Entity.UrlMapping;
import Me.Zip.Zipme.Repository.UrlRepository;
import Me.Zip.Zipme.Util.Base62;

@Service
public class UrlService {
    @Autowired
    UrlRepository repo;

    

    public String createShortUrl(String longUrl, String alias){
  
        if (alias != null && !alias.isBlank()) {

            if (repo.existsByShortCode(alias)) {
                return null;
            }

            UrlMapping mapping = new UrlMapping();
            mapping.setLongUrl(longUrl);
            mapping.setShortCode(alias);
            repo.save(mapping);

            return alias;
        }

        UrlMapping mapping = new UrlMapping();
        mapping.setLongUrl(longUrl);
        mapping.setShortCode("temp");
        repo.save(mapping);               

        String code = Base62.encode(mapping.getId());
        mapping.setShortCode(code);
        repo.save(mapping);               

        return code;

    }

    @Cacheable(value = "longUrls", key = "#shortCode")
    public String getLongUrl(String shortCode) {
        System.out.println("🔥 HITTING DB in getLongUrl for: " + shortCode);
        return repo.findByShortCode(shortCode)
                .map(UrlMapping::getLongUrl)
                .orElse(null);
    }

    @CacheEvict(value = "urlInfos", key = "#shortCode")
    public void increaseClickCount(String shortCode) {
        System.out.println("📊 HITTING DB in getInfo for: " + shortCode);
        repo.findByShortCode(shortCode).ifPresent(mapping -> {
            mapping.setClickCount(mapping.getClickCount() + 1);
            repo.save(mapping);
        });
    }

    @Cacheable(value = "urlInfos", key = "#shortCode")
    public UrlMapping getInfo(String shortCode) {
        return repo.findByShortCode(shortCode).orElse(null);
    }
}
