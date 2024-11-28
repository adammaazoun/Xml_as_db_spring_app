package projetxml.equipsync.controllers;

import org.springframework.web.bind.annotation.*;
import projetxml.equipsync.Services.EquipmentService;
import projetxml.equipsync.entities.Equipment;


@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping("/add")
    public String addEquipment(@RequestBody Equipment equipment, @RequestParam String xsdSchema) {
        return equipmentService.insertEquipment(equipment, xsdSchema);
    }

    @GetMapping("/all")
    public String getAllEquipment() {
        return equipmentService.getAllEquipment();
    }

    @GetMapping("/{id}")
    public String getEquipmentById(@PathVariable int id) {
        return equipmentService.getEquipmentById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteEquipmentById(@PathVariable int id) {
        return equipmentService.deleteEquipmentById(id);
    }

    @PutMapping("/update")
    public String updateEquipment(@RequestBody Equipment equipment, @RequestParam String xsdSchema) {
        return equipmentService.updateEquipment(equipment, xsdSchema);
    }
}

