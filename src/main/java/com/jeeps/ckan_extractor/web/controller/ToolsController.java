package com.jeeps.ckan_extractor.web.controller;

import com.jeeps.ckan_extractor.service.CkanPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.jeeps.ckan_extractor.core.CkanSemanticCreator.SERIALIZATION_FORMATS;

@Controller
public class ToolsController {
    @Autowired
    private CkanPackageService ckanPackageService;

    @RequestMapping("/tools/converter")
    public String rdfConverter(Model model) {
        List<String> originUrls = ckanPackageService.getOriginUrls();
        model.addAttribute("ckanUrls", originUrls);

        // Packages count
        List<Integer> ckanPackageCount = new ArrayList<>();
        List<Long> resourcesCount = new ArrayList<>();
        originUrls.forEach(url -> {
            Integer originUrlCount = ckanPackageService.countDistinctByOriginUrl(url);
            ckanPackageCount.add(originUrlCount);

            // Resources count
            // TODO: Too resource intensive
            /*Collection<CkanPackage> ckanPackages = ckanPackageService.findAllByOriginUrl(url);
            Long count = ckanPackages.stream()
                    .map(CkanPackage::getResources)
                    .mapToLong(Collection::size)
                    .sum();
            resourcesCount.add(count);*/
        });
        model.addAttribute("urlCount", ckanPackageCount);
        /*model.addAttribute("resourcesCount", resourcesCount);*/

        // Serialization formats
        model.addAttribute("formats", SERIALIZATION_FORMATS);

        // Create a random socket for each client
        Random random = new Random();
        String socketUri = "socket-" + random.nextInt(1000);
        model.addAttribute("socketUri", socketUri);
        return "admin";
    }
}