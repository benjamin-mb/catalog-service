# catalog-service

# 📦 Microservicio de Catalog Service - ARKA

Microservicio de gestión de catálogo de productos y categorías para el sistema ARKA.

---

## 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Arquitectura](#arquitectura)
- [Tecnologías](#tecnologías)
- [Requisitos Previos](#requisitos-previos)
- [Instalación](#instalación)
- [Configuración](#configuración)
- [Endpoints API](#endpoints-api)
- [Integración con RabbitMQ](#integración-con-rabbitmq)
- [Conversión de Moneda](#conversión-de-moneda)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Testing](#testing)
- [Manejo de Errores](#manejo-de-errores)

---

## 🎯 Descripción

**catalog-service** es el microservicio encargado de gestionar el catálogo de productos del sistema ARKA. Proporciona funcionalidades para:

- ✅ Gestión de categorías de productos
- ✅ Gestión de productos (CRUD completo)
- ✅ Consulta de productos con conversión de moneda (COP, USD, CLP, PEN)
- ✅ Reducción automática de stock al crear órdenes (RabbitMQ)
- ✅ Incremento automático de stock al cancelar órdenes (RabbitMQ)
- ✅ Notificaciones de stock bajo (RabbitMQ)
- ✅ Cache de tasas de cambio (Caffeine)

---

## 🏗️ Arquitectura

Este microservicio implementa **Arquitectura Limpia (Clean Architecture)** debido a su alta complejidad y múltiples dependencias externas.

### ¿Por qué Clean Architecture para este servicio?

Este microservicio fue diseñado con Clean Architecture debido a:

1. **Alta complejidad del dominio**: Gestión de productos, categorías, stock, y conversión de monedas
2. **Múltiples dependencias externas**:
   - 🐰 RabbitMQ para mensajería asíncrona (órdenes)
   - 💱 API externa para conversión de divisas
   - 💾 Cache (Caffeine) para optimización
   - 🗄️ Base de datos MySQL
   - 🔗 Validaciones con usuario-service (proveedores)
3. **Independencia del dominio**: La lógica de negocio no debe depender de frameworks o infraestructura
4. **Testabilidad**: Facilita pruebas unitarias aisladas del dominio

### Capas de la Arquitectura:

```
catalog-service/
│
├── 📦 domain/                         # CAPA DE DOMINIO (Reglas de Negocio)
│   │                                  # ⚠️ NO depende de infraestructura
│   ├── model/                         # Entidades de dominio puras
│   │   ├── Categorias.java
│   │   ├── Productos.java
│   │   ├── Proveedor.java
│   │   └── OrdenItem.java
│   │
│   ├── gateway/                       # Interfaces (Puertos de salida)
│   │   ├── CategoriaGateway.java     # Contrato para persistencia
│   │   ├── ProductoGateway.java      # Contrato para productos
│   │   ├── ProvedorGateway.java      # Contrato para proveedores
│   │   └── ProductoPublisherGateway.java  # Contrato para mensajería
│   │
│   └── useCase/                       # Casos de Uso (Lógica de negocio)
│       ├── PostProductUseCase.java
│       ├── GetterProductUseCase.java
│       ├── ReduceStockProductUseCase.java
│       ├── IncreaseStockWhenOrderCancelledUseCase.java
│       ├── GetterProductForDifferentCurrencyUseCase.java
│       └── ...
│
├── 🔧 infrastructure/                 # CAPA DE INFRAESTRUCTURA (Detalles técnicos)
│   │                                  # Implementa los contratos del dominio
│   ├── adapters/
│   │   ├── entity/                   # Entidades JPA (persistencia)
│   │   │   ├── CategoriasEntity.java
│   │   │   ├── ProductosEntity.java
│   │   │   └── ProveedorEntity.java
│   │   │
│   │   ├── repository/               # Implementación de Gateways
│   │   │   ├── CategoriasRepositoryImplements.java
│   │   │   ├── ProductosRepositoryImplements.java
│   │   │   ├── ProductoPublisherAdapter.java
│   │   │   └── ProveedoresRepositoryImplements.java
│   │   │
│   │   ├── mappers/                  # Conversión Domain ↔ Entity
│   │   │   ├── CategoriaMapper.java
│   │   │   └── ProductoMapper.java
│   │   │
│   │   ├── service/                  # Servicios de aplicación
│   │   │   ├── ReduceStockService.java
│   │   │   └── IncreaseStockService.java
│   │   │
│   │   └── Component/                # Componentes de infraestructura
│   │       └── CurrenciesService.java  # Cliente API externa divisas
│   │
│   ├── controllers/                  # Controladores REST (API)
│   │   ├── CategoriaController.java
│   │   └── ProductController.java
│   │
│   ├── config/                       # Configuraciones técnicas
│   │   ├── RabbitMQConfig.java       # Config RabbitMQ
│   │   ├── CacheConfig.java          # Config Caffeine
│   │   └── RestClientConfig.java     # Config cliente HTTP
│   │
│   └── messages/                     # Mensajería RabbitMQ
│       ├── HandleOrdersCreated.java       # Listener: Órdenes creadas
│       ├── HandleOrdersCancelled.java     # Listener: Órdenes canceladas
│       └── PublisherProducto.java         # Publisher: Stock bajo
│
└── 🔌 applicationConfig/              # CAPA DE APLICACIÓN
    └── Config.java                    # Inyección de dependencias
```

### 🎯 Principios de Clean Architecture Aplicados:

1. **Independencia de Frameworks**: El dominio no conoce Spring, JPA, o RabbitMQ
2. **Testabilidad**: Los casos de uso se pueden probar sin infraestructura
3. **Independencia de la Base de Datos**: Podemos cambiar MySQL sin tocar el dominio
4. **Independencia de APIs Externas**: La API de divisas puede cambiar sin afectar el dominio
5. **Regla de Dependencia**: Las dependencias apuntan SIEMPRE hacia el dominio

### 🔄 Flujo de Dependencias:

```
Controllers → Use Cases → Gateways (interfaces)
                              ↑
                              |
                    Repositories (implementan)
```

**El dominio (centro) NO conoce la infraestructura (exterior)**

---

## 🛠️ Tecnologías

| Tecnología | Versión | Uso |
|-----------|---------|-----|
| **Java** | 21 | Lenguaje de programación |
| **Spring Boot** | 3.5.5 | Framework principal |
| **Spring Data JPA** | 3.5.5 | Persistencia de datos |
| **MySQL** | 8.0+ | Base de datos |
| **RabbitMQ** | Latest | Mensajería asíncrona |
| **Caffeine** | Latest | Cache en memoria |
| **Lombok** | Latest | Reducción de boilerplate |
| **SpringDoc OpenAPI** | 2.7.0 | Documentación API (Swagger) |
| **Eureka Client** | 2025.0.0 | Service Discovery |
| **Spring Retry** | Latest | Reintentos automáticos |

---

## 📦 Requisitos Previos

Antes de ejecutar este microservicio, asegúrate de tener:

1. ✅ **Java 21** o superior instalado
2. ✅ **Maven 3.8+** instalado
3. ✅ **MySQL 8.0+** corriendo en `localhost:3306`
4. ✅ **RabbitMQ** corriendo en `localhost:5672`
5. ✅ **Base de datos `arka`** creada en MySQL
6. ✅ **Eureka Server** corriendo (opcional pero recomendado)
7. ✅ **usuario-service** corriendo (para validar proveedores)

---

## 🚀 Instalación

### 1. Clonar el repositorio
```bash
git clone <repository-url>
cd catalog-service
```

### 2. Configurar base de datos
```sql
CREATE DATABASE IF NOT EXISTS arka;
USE arka;

-- Las tablas se crean automáticamente con JPA
```

### 3. Configurar RabbitMQ
```bash
# Crear usuario y permisos
rabbitmqctl add_user arka arka123
rabbitmqctl set_user_tags arka administrator
rabbitmqctl set_permissions -p / arka ".*" ".*" ".*"
```

### 4. Instalar dependencias
```bash
mvn clean install
```

### 5. Ejecutar el servicio
```bash
mvn spring-boot:run
```

El servicio estará disponible en: `http://localhost:8082`

---

## ⚙️ Configuración

### application.yml

```yaml
server:
  port: 8082

spring:
  application:
    name: catalogo-service

  datasource:
    url: jdbc:mysql://localhost:3306/arka?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    username: root
    password: tu_password
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: validate  # Cambiar a create  # Cambiar a 'update' o 'validate' en producción
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect

  rabbitmq:
    host: localhost
    port: 5672
    username: arka
    password: arka123

eureka:
  client:
    service-url:
      defaultZone: http://admin:admin123@localhost:8761/eureka/
```

### Variables de Entorno (Recomendado para producción)

```properties
DB_HOST=localhost
DB_PORT=3306
DB_NAME=arka
DB_USER=root
DB_PASSWORD=your_password

RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USER=arka
RABBITMQ_PASSWORD=arka123

EUREKA_URL=http://localhost:8761/eureka/
```

---

## 📡 Endpoints API

### 🏷️ Categorías

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| `GET` | `/api/categorias` | Listar todas las categorías | 🌐 Pública |
| `GET` | `/api/categorias/{id}` | Obtener categoría por ID | 🌐 Pública |
| `GET` | `/api/categorias/nombre/{nombre}` | Buscar por nombre | 🌐 Pública |
| `GET` | `/api/categorias/tipo/{tipo}` | Filtrar por tipo | 🌐 Pública |
| `POST` | `/api/categorias` | Crear categoría | 👑 Admin |
| `PUT` | `/api/categorias/{id}` | Actualizar categoría | 👑 Admin |
| `DELETE` | `/api/categorias/{id}` | Eliminar categoría | 👑 Admin |

#### Ejemplo: Crear Categoría
```bash
POST /api/categorias
Content-Type: application/json

{
  "nombre": "Electrónica",
  "caracteristicas": "Productos electrónicos y tecnológicos",
  "tipo": "tecnologia"
}
```

#### Respuesta:
```json
{
  "id": 1,
  "nombre": "Electrónica",
  "caracteristicas": "Productos electrónicos y tecnológicos",
  "tipo": "tecnologia",
  "productos": []
}
```

---

### 🛍️ Productos

| Método | Endpoint | Descripción | Autenticación |
|--------|----------|-------------|---------------|
| `GET` | `/api/productos` | Listar todos los productos | 🌐 Pública |
| `GET` | `/api/productos/id/{id}` | Obtener producto por ID | 🌐 Pública |
| `GET` | `/api/productos/nombre/{nombre}` | Buscar por nombre | 🌐 Pública |
| `GET` | `/api/productos/marca/{marca}` | Filtrar por marca | 🌐 Pública |
| `GET` | `/api/productos/categoria/{id}` | Filtrar por categoría | 🌐 Pública |
| `POST` | `/api/productos` | Crear producto | 👑 Admin |
| `PUT` | `/api/productos/{id}` | Actualizar producto | 👑 Admin |
| `DELETE` | `/api/productos/{id}` | Eliminar producto | 👑 Admin |

#### Ejemplo: Crear Producto
```bash
POST /api/productos
Content-Type: application/json

{
  "nombre": "Laptop HP Pavilion 15",
  "precio": 2500000,
  "stock": 15,
  "caracteristicas": "Intel Core i7, 16GB RAM, 512GB SSD",
  "marca": "HP",
  "categoria": 1,
  "proveedor": 1
}
```

#### Respuesta:
```json
{
  "id": 1,
  "nombre": "Laptop HP Pavilion 15",
  "precio": 2500000,
  "stock": 15,
  "caracteristicas": "Intel Core i7, 16GB RAM, 512GB SSD",
  "marca": "HP",
  "categoria": 1,
  "proveedor": 1
}
```

---

### 💱 Productos con Conversión de Moneda

Todos los endpoints GET de productos tienen su equivalente con conversión de moneda:

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/productos/currencies/{currency}` | Todos los productos en otra moneda |
| `GET` | `/api/productos/currencies/{currency}/id/{id}` | Producto por ID con conversión |
| `GET` | `/api/productos/currencies/{currency}/nombre/{nombre}` | Por nombre con conversión |
| `GET` | `/api/productos/currencies/{currency}/marca/{marca}` | Por marca con conversión |
| `GET` | `/api/productos/currencies/{currency}/categoria/{id}` | Por categoría con conversión |

**Monedas soportadas:** `COP` (default), `USD`, `CLP`, `PEN`

#### Ejemplo: Productos en USD
```bash
GET /api/productos/currencies/USD
```

#### Respuesta:
```json
[
  {
    "id": 1,
    "nombre": "Laptop HP Pavilion 15",
    "precio": 625,
    "stock": 15,
    "caracteristicas": "Intel Core i7, 16GB RAM, 512GB SSD",
    "marca": "HP",
    "categoria": 1,
    "proveedor": 1
  }
]
```

---

## 🐰 Integración con RabbitMQ

### Eventos que ESCUCHA:

#### 1️⃣ Orden Creada (`order.created`)
**Queue:** `order.created.inventory`  
**Exchange:** `orders.exchange`  
**Routing Key:** `order.created`

**Acción:** Reduce el stock de los productos de la orden

**Payload esperado:**
```json
{
  "items": [
    {
      "idProducto": 1,
      "cantidad": 2
    },
    {
      "idProducto": 3,
      "cantidad": 1
    }
  ]
}
```

**Lógica:**
- Valida que haya stock suficiente
- Reduce el stock de cada producto
- Si el stock queda < 5, publica evento de `stock.low`
- Si falla, envía a DLQ (Dead Letter Queue)

---

#### 2️⃣ Orden Cancelada (`cancelled.order`)
**Queue:** `order.cancelled.inventory`  
**Exchange:** `cancelled.exchange`  
**Routing Key:** `cancelled.order`

**Acción:** Incrementa el stock de los productos (rollback)

**Payload esperado:**
```json
{
  "items": [
    {
      "idProducto": 1,
      "cantidad": 2
    }
  ]
}
```

---

### Eventos que PUBLICA:

#### 3️⃣ Stock Bajo (`stock.low`)
**Exchange:** `notifications.exchange`  
**Routing Key:** `stock.low`

**Cuándo:** Cuando el stock de un producto queda < 5 unidades después de una venta

**Payload:**
```json
{
  "producto_id": 1,
  "nombre_producto": "Laptop HP Pavilion 15",
  "stock_Actual": 3,
  "proveedor_id": 1
}
```

---

### Configuración de Reintentos:

- **Intentos máximos:** 3
- **Intervalo inicial:** 2 segundos
- **Multiplicador:** 2.0
- **Intervalo máximo:** 10 segundos

Si todos los reintentos fallan → **Dead Letter Queue (DLQ)**

---

## 💱 Conversión de Moneda

### API Externa Utilizada:
**ExchangeRate-API:** `https://v6.exchangerate-api.com/v6/{API_KEY}/latest/COP`

### Monedas Soportadas:
- 🇨🇴 **COP** (Colombian Peso) - Moneda base
- 🇺🇸 **USD** (US Dollar)
- 🇨🇱 **CLP** (Chilean Peso)
- 🇵🇪 **PEN** (Peruvian Sol)

### Cache de Tasas de Cambio:
- **Motor:** Caffeine Cache
- **Duración:** 24 horas
- **Estrategia:** Lazy loading (carga bajo demanda)
- **Beneficio:** Reduce llamadas a API externa y mejora rendimiento

### Implementación:
```java
@Cacheable(value = "currencies", key = "'rates'")
public CurrencyRates getCurrencies() {
    // Llamada a API externa solo si no está en cache
}
```

---

## 📁 Estructura del Proyecto

```
src/main/java/com/arka/catalog_service/
│
├── 📦 domain/
│   ├── model/
│   │   ├── Categorias.java
│   │   ├── Productos.java
│   │   ├── Proveedor.java
│   │   ├── OrdenItem.java
│   │   ├── DTO/
│   │   │   ├── CategoriaCreateDto.java
│   │   │   ├── ProductoCreateDto.java
│   │   │   └── CurrencyProduct.java
│   │   └── GlobalExceptions/
│   │       ├── CategoriaNotFoundExceptions.java
│   │       ├── ProductNotFoundException.java
│   │       └── GoblalExceptionHandler.java
│   │
│   ├── gateway/
│   │   ├── CategoriaGateway.java
│   │   ├── ProductoGateway.java
│   │   ├── ProvedorGateway.java
│   │   └── ProductoPublisherGateway.java
│   │
│   └── useCase/
│       ├── PostCategoriaUseCase.java
│       ├── PostProductUseCase.java
│       ├── GetterCategoriaUseCase.java
│       ├── GetterProductUseCase.java
│       ├── GetterProductForDifferentCurrencyUseCase.java
│       ├── UpdateDeleteCategoriaUseCase.java
│       ├── UpdateDeleteProductoUseCase.java
│       ├── ReduceStockProductUseCase.java
│       ├── IncreaseStockWhenOrderCancelledUseCase.java
│       ├── WrapperReduceStockUseCase.java
│       └── WrapperIncreaseStockUseCase.java
│
├── 🔧 infrastructure/
│   ├── adapters/
│   │   ├── entity/
│   │   │   ├── CategoriasEntity.java
│   │   │   ├── ProductosEntity.java
│   │   │   └── ProveedorEntity.java
│   │   │
│   │   ├── repository/
│   │   │   ├── CategoriasJpaRepository.java
│   │   │   ├── CategoriasRepositoryImplements.java
│   │   │   ├── ProductosJpaRepository.java
│   │   │   ├── ProductosRepositoryImplements.java
│   │   │   ├── ProvedooresJpaRepository.java
│   │   │   ├── ProveedoresRepositoryImplements.java
│   │   │   └── ProductoPublisherAdapter.java
│   │   │
│   │   ├── mappers/
│   │   │   ├── CategoriaMapper.java
│   │   │   └── ProductoMapper.java
│   │   │
│   │   ├── service/
│   │   │   ├── ReduceStockService.java
│   │   │   └── IncreaseStockService.java
│   │   │
│   │   └── Component/
│   │       └── CurrenciesService.java
│   │
│   ├── controllers/
│   │   ├── CategoriaController.java
│   │   └── ProductController.java
│   │
│   ├── config/
│   │   ├── RabbitMQConfig.java
│   │   ├── CacheConfig.java
│   │   └── RestClientConfig.java
│   │
│   ├── messages/
│   │   ├── HandleOrdersCreated.java
│   │   ├── HandleOrdersCancelled.java
│   │   └── PublisherProducto.java
│   │
│   └── DTO/
│       ├── CurrencyRates.java
│       ├── OrdenCompleta.java
│       ├── OrdenItemDto.java
│       ├── ProductsRunningLowStock.java
│       └── mapper/
│           └── MapperOrdenItemDto.java
│
└── 🔌 applicationConfig/
    └── Config.java
```

---

## 🧪 Testing

### Testing Manual con Swagger
Accede a: `http://localhost:8082/swagger-ui.html`

### Ejemplos de Testing:

#### 1. Crear Categoría
```bash
curl -X POST http://localhost:8082/api/categorias \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Electrónica",
    "caracteristicas": "Productos electrónicos",
    "tipo": "tecnologia"
  }'
```

#### 2. Crear Producto
```bash
curl -X POST http://localhost:8082/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Laptop Dell",
    "precio": 3000000,
    "stock": 10,
    "caracteristicas": "Intel i7, 16GB RAM",
    "marca": "Dell",
    "categoria": 1,
    "proveedor": 1
  }'
```

#### 3. Consultar con Moneda
```bash
curl -X GET http://localhost:8082/api/productos/currencies/USD
```

#### 4. Simular Orden (RabbitMQ)
Puedes usar RabbitMQ Management UI (`http://localhost:15672`) para publicar manualmente:

**Exchange:** `orders.exchange`  
**Routing Key:** `order.created`  
**Payload:**
```json
{
  "items": [
    {"idProducto": 1, "cantidad": 2}
  ]
}
```

---

## ❌ Manejo de Errores

### Excepciones del Dominio:

| Excepción | HTTP Status | Descripción |
|-----------|-------------|-------------|
| `CategoriaNotFoundExceptions` | 404 | Categoría no encontrada |
| `ProductNotFoundException` | 404 | Producto no encontrado |
| `IllegalArgumentException` | 400 | Validación fallida (stock negativo, precio inválido, etc.) |
| `EntityNotFoundException` | 404 | Entidad no encontrada |

### Ejemplo de Respuesta de Error:
```json
{
  "timeStamp": "2025-10-24T12:30:00",
  "status": "NOT_FOUND",
  "error": "Not Found",
  "message": "Product not found with id: 999",
  "path": "/api/productos/id/999"
}
```

### Validaciones de Negocio:

#### Productos:
- ❌ Stock no puede ser negativo
- ❌ Precio debe ser mayor a 0
- ❌ Nombre no puede estar duplicado
- ❌ Categoría debe existir
- ❌ Proveedor debe existir
- ❌ Características no pueden estar vacías

#### Categorías:
- ❌ Nombre no puede estar duplicado
- ❌ Tipo no puede estar vacío
- ❌ Características no pueden estar vacías

---

## 🔗 Integración con Gateway

Este microservicio se integra con el **API Gateway** de ARKA:

### Rutas Públicas (sin autenticación):
- `GET /arka/categorias/**`
- `GET /arka/productos/**`

### Rutas Admin (requieren token de administrador):
- `POST /arka/categorias`
- `PUT /arka/categorias/{id}`
- `DELETE /arka/categorias/{id}`
- `POST /arka/productos`
- `PUT /arka/productos/{id}`
- `DELETE /arka/productos/{id}`

**Gateway URL:** `http://localhost:8090`

---

## 📊 Modelo de Datos

### Tabla: categorias
```sql
CREATE TABLE categorias (
  id_categoria INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL UNIQUE,
  caracteristicas TEXT,
  tipo VARCHAR(50)
);
```

### Tabla: productos
```sql
CREATE TABLE productos (
  id_producto INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(70) NOT NULL UNIQUE,
  precio INT NOT NULL,
  stock INT NOT NULL,
  caracteristicas TEXT,
  marca VARCHAR(255) NOT NULL,
  id_categoria INT NOT NULL,
  id_proveedor INT NOT NULL,
  FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria),
  FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor)
);
```

---

## 🚀 Despliegue

### Docker (Próximamente)
```bash
docker build -t catalog-service .
docker run -p 8082:8082 catalog-service
```

### Variables de Entorno Requeridas:
- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `RABBITMQ_HOST`
- `RABBITMQ_PORT`
- `RABBITMQ_USER`
- `RABBITMQ_PASSWORD`
  
## 📝 Notas Adicionales

### Dependencias con Otros Servicios:
- **usuario-service**: Validación de proveedores
- **orders-service**: Eventos de órdenes (RabbitMQ)
- **notifications-service**: Notificaciones de stock bajo

### Performance:
- Cache de tasas de cambio (24h)
- Índices en nombre y categoría
- Lazy loading de relaciones JPA

### Seguridad:
- Endpoints protegidos por Gateway
- Validación de datos en casos de uso
- Sanitización de inputs

---

**🎯 catalog-service v1.0.0**  
*Microservicio de catálogo con Clean Architecture*
