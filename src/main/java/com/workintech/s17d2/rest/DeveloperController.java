package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DeveloperController {
    public Map<Integer, Developer> developers;
    private Taxable taxable;

    @Autowired
    public DeveloperController(Taxable taxable){
        this.taxable = taxable;
    }

    @PostConstruct
    public void init(){
        developers = new HashMap<>();
    }
    @GetMapping("/developers")
    public List<Developer> getDevelopers(){
        return developers.values().stream().toList();
    }
    @GetMapping("/developers/{id}")
    public Developer getDeveloper(@PathVariable int id){
        return developers.get(id);
    }
    @PostMapping("/developers")
    public ResponseEntity<Developer> addDeveloper(@RequestBody Developer developer){
        double salary = developer.getSalary();
        switch (developer.getExperience()) {
            case JUNIOR:
                salary = salary - salary * taxable.getSimpleTaxRate();
            case MID:
                salary = salary - salary - taxable.getMiddleTaxRate();
            case SENIOR:
                salary = salary - salary * taxable.getUpperTaxRate();
        }
        developers.put(developer.getId(), new Developer(developer.getId(), developer.getName(), salary, developer.getExperience()));
        return new ResponseEntity<>(developer, HttpStatus.CREATED);
    }
    @PutMapping("/developers/{id}")
    public void updateDeveloper(@PathVariable int id, @RequestBody Developer developer){
        developers.put(id, developer);
    }
    @DeleteMapping("/developers/{id}")
    public void deleteDeveloper(@PathVariable int id){
        developers.remove(id);
    }

}
