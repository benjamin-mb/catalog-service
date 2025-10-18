package com.arka.catalog_service.infrastructure.controllers;

import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.DTO.CurrencyProduct;
import com.arka.catalog_service.domain.useCase.GetterProductForDifferentCurrencyUseCase;
import com.arka.catalog_service.domain.useCase.GetterProductUseCase;
import com.arka.catalog_service.domain.useCase.PostProductUseCase;
import com.arka.catalog_service.domain.useCase.UpdateDeleteProductoUseCase;
import com.arka.catalog_service.infrastructure.adapters.Component.CurrenciesService;
import com.arka.catalog_service.infrastructure.DTO.CurrencyRates;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductController {

    private final PostProductUseCase productUseCasePost;
    private final UpdateDeleteProductoUseCase productoUseCasePutDelete;
    private final GetterProductUseCase productUseCaseGetters;
    private final GetterProductForDifferentCurrencyUseCase currencyUseCase;
    private final CurrenciesService currenciesService;

    public ProductController(PostProductUseCase productUseCasePost, UpdateDeleteProductoUseCase productoUseCasePutDelete, GetterProductUseCase productUseCaseGetters, GetterProductForDifferentCurrencyUseCase currencyUseCase, CurrenciesService currenciesService) {
        this.productUseCasePost = productUseCasePost;
        this.productoUseCasePutDelete = productoUseCasePutDelete;
        this.productUseCaseGetters = productUseCaseGetters;
        this.currencyUseCase = currencyUseCase;
        this.currenciesService = currenciesService;
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

    //Getters para CURRENCIES
    @GetMapping("/currencies/{currency}/id/{id}")
    public ResponseEntity<CurrencyProduct> getByIdWithCurrency(@PathVariable String currency,
                                                               @PathVariable Integer id) {
        Double rate = getCurrencyRate(currency);
        return ResponseEntity.ok(currencyUseCase.getById(id, rate));
    }

    @GetMapping("/currencies/{currency}/nombre/{nombre}")
    public ResponseEntity<CurrencyProduct> getByNombreWithCurrency(@PathVariable String currency,
                                                                   @PathVariable String nombre) {
        Double rate = getCurrencyRate(currency);
        return ResponseEntity.ok(currencyUseCase.getByNombre(nombre, rate));
    }

    @GetMapping("/currencies/{currency}/marca/{marca}")
    public ResponseEntity<List<CurrencyProduct>> getAllByMarcaWithCurrency(@PathVariable String currency,
                                                                           @PathVariable String marca) {
        Double rate = getCurrencyRate(currency);
        return ResponseEntity.ok(currencyUseCase.getByMarca(marca, rate));
    }

    @GetMapping("/currencies/{currency}/categoria/{categoria}")
    public ResponseEntity<List<CurrencyProduct>> getAllByCategoriaWithCurrency(@PathVariable String currency,
                                                                               @PathVariable Integer categoria) {
        Double rate = getCurrencyRate(currency);
        return ResponseEntity.ok(currencyUseCase.getByCategoria(categoria, rate));
    }

    @GetMapping("/currencies/{currency}")
    public ResponseEntity<List<CurrencyProduct>> getAllWithCurrency(@PathVariable String currency) {
        Double rate = getCurrencyRate(currency);
        return ResponseEntity.ok(currencyUseCase.getAll(rate));
    }

    private Double getCurrencyRate(String currency) {
        if ("COP".equalsIgnoreCase(currency)) {
            return 1.0;
        }

        CurrencyRates rates = currenciesService.getCurrencies();

        return switch (currency.toUpperCase()) {
            case "USD" -> rates.getUSD();
            case "CLP" -> rates.getCLP();
            case "PEN" -> rates.getPEN();
            default -> 1.0;
        };

    }
}
