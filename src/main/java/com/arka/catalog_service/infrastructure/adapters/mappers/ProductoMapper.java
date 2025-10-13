package com.arka.catalog_service.infrastructure.adapters.mappers;

import com.arka.catalog_service.domain.model.Productos;
import com.arka.catalog_service.domain.model.Proveedor;
import com.arka.catalog_service.infrastructure.adapters.entity.CategoriasEntity;
import com.arka.catalog_service.infrastructure.adapters.entity.ProductosEntity;
import com.arka.catalog_service.infrastructure.adapters.entity.ProveedorEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductosEntity toEntity(Productos producto) {
        if (producto == null) return null;
        ProductosEntity productosEntity = new ProductosEntity();
        productosEntity.setId(producto.getId());
        productosEntity.setNombre(producto.getNombre());
        productosEntity.setPrecio(producto.getPrecio());
        productosEntity.setStock(producto.getStock());
        productosEntity.setCaracteristicas(producto.getCaracteristicas());
        productosEntity.setMarca(producto.getMarca());
        if (producto.getProveedor()!= null){
            ProveedorEntity proveedor=new ProveedorEntity();
            proveedor.setId(producto.getProveedor());
            productosEntity.setProveedor(proveedor);
        }
        if (producto.getCategoria()!= null){
            CategoriasEntity categoriasEntity=new CategoriasEntity();
            categoriasEntity.setId(producto.getCategoria());
            productosEntity.setCategoria(categoriasEntity);
        }
        return productosEntity;
    }

    public Productos toModel(ProductosEntity productosEntity){
        if (productosEntity==null)return null;
        Productos producto=new Productos();
        producto.setId(productosEntity.getId());
        producto.setNombre(productosEntity.getNombre());
        producto.setPrecio(productosEntity.getPrecio());
        producto.setStock(productosEntity.getStock());
        producto.setCaracteristicas(productosEntity.getCaracteristicas());
        producto.setMarca(productosEntity.getMarca());
        producto.setCategoria(productosEntity.getCategoria().getId());
        producto.setProveedor(productosEntity.getProveedor().getId());

        return producto;
    }
}
