package projetxml.equipsync.controllers;

import org.springframework.web.bind.annotation.*;
import projetxml.equipsync.Services.OperationManagerService;
import projetxml.equipsync.entities.OperationsManager;


@RestController
@RequestMapping("/operations-manager")
public class OperationsManagerController {

    private final OperationManagerService operationsManagerService;

    public OperationsManagerController(OperationManagerService operationsManagerService) {
        this.operationsManagerService = operationsManagerService;
    }

    @PostMapping("/add")
    public String addOperationsManager(@RequestBody OperationsManager manager) {
        return operationsManagerService.insertOperationsManager(manager);
    }

    @GetMapping("/all")
    public String getAllOperationsManagers() {
        return operationsManagerService.getAllOperationsManagers().toString();
    }

    @GetMapping("/{id}")
    public OperationsManager getOperationsManagerById(@PathVariable String id) {
        return operationsManagerService.getOperationsManagerById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteOperationsManagerById(@PathVariable String id) {
        return operationsManagerService.deleteOperationsManagerById(id);
    }

    @PutMapping("/update")
    public String updateOperationsManager(@RequestBody OperationsManager manager) {
        return operationsManagerService.updateOperationsManager(manager);
    }
}

