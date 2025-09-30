package com.arka.catalog_service.infrastructure.controllers;

import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.useCase.GetterProductUseCase;
import com.arka.catalog_service.domain.useCase.PostProductUseCase;
import com.arka.catalog_service.domain.useCase.UpdateDeleteProductoUseCase;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCheckpointRestore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductController {

    private final PostProductUseCase productUseCasePost;
    private final UpdateDeleteProductoUseCase productoUseCasePutDelete;
    private final GetterProductUseCase productUseCaseGetters;

    public ProductController(PostProductUseCase productUseCasePost, UpdateDeleteProductoUseCase productoUseCasePutDelete, GetterProductUseCase productUseCaseGetters) {
        this.productUseCasePost = productUseCasePost;
        this.productoUseCasePutDelete = productoUseCasePutDelete;
        this.productUseCaseGetters = productUseCaseGetters;
    }

    @PostMapping
    public ResponseEntity<Productos>create(@RequestBody Productos producto){
        return ResponseEntity.ok(productUseCasePost.create(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Productos>put(@RequestBody Productos producto){
        return ResponseEntity.ok(productoUseCasePutDelete.update(producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Productos>delete(@PathVariable Integer id){
        return ResponseEntity.ok(productoUseCasePutDelete.delete(id));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {

           Productos producto=productUseCaseGetters.getById(id);
           return ResponseEntity.ok(producto);

    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Productos> getByName(@PathVariable String nombre) {
        return ResponseEntity.ok(productUseCaseGetters.getByNombre(nombre));
    }

    @GetMapping("/marca/{marca}")
    public ResponseEntity<List<Productos>> getAllByMarca(@PathVariable String marca) {
        return ResponseEntity.ok(productUseCaseGetters.getByMarca(marca));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Productos>> getAllByCategoria(@PathVariable Integer categoria) {
        return ResponseEntity.ok(productUseCaseGetters.getByCategoria(categoria));
    }

    @GetMapping
    public ResponseEntity<List<Productos>>GetAll(){
        return ResponseEntity.ok(productUseCaseGetters.getAll());
    }

}
