package Me.Zip.Zipme.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import Me.Zip.Zipme.Entity.UrlMapping;
import Me.Zip.Zipme.Service.UrlService;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class UrlController {

    @Autowired
    UrlService service;


    @GetMapping("/isRunning")
    public String isSystemRunning(){
      return "Server is up!";
    }

    @PostMapping("/zipme")
    public String zipme(@RequestParam String longUrl, @RequestParam(required = false) String alias) {
        
        String result = service.createShortUrl(longUrl, alias);

        if(result == null){
            return "Alias "+alias+" is already in use. Try another alias or go for default zipme.";
        }
        
        return "Short Url: https://zipme.onrender.com/ziped/" + result;
    }

    @GetMapping("/ziped/{code}")
    public void redirect(@PathVariable String code,
                         HttpServletResponse response) throws IOException {

        String longUrl = service.getLongUrl(code);

        if (longUrl == null) {
            response.sendError(404, "Short URL not found");
            return;
        }

        service.increaseClickCount(code);

        response.sendRedirect(longUrl);
    }

    @GetMapping("/info/{code}")
    public Object info(@PathVariable String code) {

        UrlMapping mapping = service.getInfo(code);

        if (mapping == null) {
            return "No data found";
        }

        return mapping;
    }
    
}
