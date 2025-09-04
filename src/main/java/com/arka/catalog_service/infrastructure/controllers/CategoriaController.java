package com.arka.catalog_service.infrastructure.controllers;

import com.arka.catalog_service.domain.model.Categorias;
import com.arka.catalog_service.domain.useCase.GetterCategoriaUseCase;
import com.arka.catalog_service.domain.useCase.PostCategoriaUseCase;
import com.arka.catalog_service.domain.useCase.UpdateDeleteCategoriaUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final GetterCategoriaUseCase getterCategoriaUseCase;
    private final PostCategoriaUseCase postCategoriaUseCase;
    private final UpdateDeleteCategoriaUseCase updateDeleteCategoriaUseCase;


    public CategoriaController(
            GetterCategoriaUseCase getterCategoriaUseCase,
            PostCategoriaUseCase postCategoriaUseCase,
            UpdateDeleteCategoriaUseCase updateDeleteCategoriaUseCase) {
        this.getterCategoriaUseCase = getterCategoriaUseCase;
        this.postCategoriaUseCase = postCategoriaUseCase;
        this.updateDeleteCategoriaUseCase = updateDeleteCategoriaUseCase;
    }

    @GetMapping
    public ResponseEntity<List<Categorias>> getAll() {
        return ResponseEntity.ok(getterCategoriaUseCase.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categorias> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(getterCategoriaUseCase.getById(id));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Categorias> getByName(@PathVariable String nombre) {
        return ResponseEntity.ok(getterCategoriaUseCase.getByName(nombre));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Categorias>> getAllByTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(getterCategoriaUseCase.getAllByTipo(tipo));
    }

    @PostMapping
    public ResponseEntity<Categorias> create(@RequestBody Categorias categoria) {
        Categorias created = postCategoriaUseCase.create(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categorias> update(@PathVariable Integer id, @RequestBody Categorias categoria) {
        categoria.setId(id);
        return ResponseEntity.ok(updateDeleteCategoriaUseCase.update(categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Categorias> delete(@PathVariable Integer id) {
        return ResponseEntity.ok(updateDeleteCategoriaUseCase.deleteById(id));
    }
}
