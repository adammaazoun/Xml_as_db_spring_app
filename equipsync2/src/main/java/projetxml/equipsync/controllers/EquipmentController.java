package projetxml.equipsync.controllers;

import org.springframework.web.bind.annotation.*;
import projetxml.equipsync.Services.EquipmentService;
import projetxml.equipsync.entities.Equipment;

import java.util.List;


@RestController
@RequestMapping("/equipments")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping("/insert")
    public String addEquipment(@RequestBody Equipment equipment) throws Exception {
        return equipmentService.insertEquipment(equipment);
    }

    @GetMapping("/all")
    public List<Equipment> getAllEquipment() {
        return equipmentService.getAllEquipment();
    }

    @GetMapping("/{id}")
    public Equipment getEquipmentById(@PathVariable String id) {
        return equipmentService.getEquipmentById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteEquipmentById(@PathVariable String id) {
        return equipmentService.deleteEquipmentById(id);
    }

    @PutMapping("/update")
    public String updateEquipment(@RequestBody Equipment equipment, @RequestParam String xsdSchema) {
        return equipmentService.updateEquipment(equipment).toString();
    }
}

