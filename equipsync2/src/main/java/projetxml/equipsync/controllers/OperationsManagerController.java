package projetxml.equipsync.controllers;

import org.springframework.web.bind.annotation.*;
import projetxml.equipsync.entities.OperationsManager;


@RestController
@RequestMapping("/operations-manager")
public class OperationsManagerController {

    private final projetxml.equipsync.services.OperationManagerService operationsManagerService;

    public OperationsManagerController(projetxml.equipsync.services.OperationManagerService operationsManagerService) {
        this.operationsManagerService = operationsManagerService;
    }

    @PostMapping("/add")
    public String addOperationsManager(@RequestBody OperationsManager manager) {
        return operationsManagerService.insertOperationsManager(manager);
    }

    @GetMapping("/all")
    public String getAllOperationsManagers() {
        return operationsManagerService.getAllOperationsManagers();
    }

    @GetMapping("/{id}")
    public String getOperationsManagerById(@PathVariable int id) {
        return operationsManagerService.getOperationsManagerById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteOperationsManagerById(@PathVariable int id) {
        return operationsManagerService.deleteOperationsManagerById(id);
    }

    @PutMapping("/update")
    public String updateOperationsManager(@RequestBody OperationsManager manager) {
        return operationsManagerService.updateOperationsManager(manager);
    }
}

