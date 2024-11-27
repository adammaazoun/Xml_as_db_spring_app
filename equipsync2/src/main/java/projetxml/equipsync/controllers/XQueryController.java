package projetxml.equipsync.controllers;

import projetxml.equipsync.Services.BaseXService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basex")
public class XQueryController {
    private final BaseXService baseXService;

    public XQueryController(BaseXService baseXService) {
        this.baseXService = baseXService;
    }



    @GetMapping("/query")
    public String executeQuery(@RequestParam String query) {
        try {
            return baseXService.executeXQuery(query);
        } catch (Exception e) {
            return "Error executing query: " + e.getMessage();
        }
    }
}
