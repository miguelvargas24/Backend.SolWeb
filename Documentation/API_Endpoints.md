# API Endpoints - Sistema Banco de Alimentos

**Última actualización:** 3 de Diciembre, 2025

## Base URL

```
http://localhost:8080/api
```

---

## 📋 Módulo de Registros (Maestros)

### Donantes (Donors)

| Método | Endpoint                                          | Descripción                        |
| ------ | ------------------------------------------------- | ---------------------------------- |
| GET    | `/donors`                                         | Obtener todos los donantes         |
| GET    | `/donors/paginated?page=0&size=10&sortBy=idDonor` | Obtener donantes activos paginados |
| GET    | `/donors/{id}`                                    | Obtener donante por ID             |
| GET    | `/donors/ruc/{ruc}`                               | Obtener donante por RUC            |
| GET    | `/donors/search?businessName={name}`              | Buscar donantes por razón social   |
| POST   | `/donors`                                         | Crear nuevo donante                |
| PUT    | `/donors/{id}`                                    | Actualizar donante                 |
| DELETE | `/donors/{id}`                                    | Desactivar donante                 |

**Ejemplo de body (POST/PUT):**

```json
{
  "ruc": "20123456789",
  "businessName": "Plaza Vea S.A.",
  "address": "Av. Principal 123, Lima",
  "contactName": "Juan Pérez",
  "contactPhone": "987654321",
  "contactEmail": "contacto@plazavea.com"
}
```

---

### Beneficiarios (Beneficiaries)

| Método | Endpoint                                   | Descripción                                    |
| ------ | ------------------------------------------ | ---------------------------------------------- |
| GET    | `/beneficiaries`                           | Obtener todos los beneficiarios                |
| GET    | `/beneficiaries/paginated?page=0&size=10`  | Obtener beneficiarios activos paginados        |
| GET    | `/beneficiaries/{id}`                      | Obtener beneficiario por ID                    |
| GET    | `/beneficiaries/document/{documentNumber}` | Obtener beneficiario por documento             |
| GET    | `/beneficiaries/search?name={name}`        | Buscar beneficiarios por nombre                |
| GET    | `/beneficiaries/type/{type}`               | Obtener por tipo (PERSONA_NATURAL, ASOCIACION) |
| POST   | `/beneficiaries`                           | Crear nuevo beneficiario                       |
| PUT    | `/beneficiaries/{id}`                      | Actualizar beneficiario                        |
| DELETE | `/beneficiaries/{id}`                      | Desactivar beneficiario                        |

**Ejemplo de body (POST/PUT):**

```json
{
  "type": "PERSONA_NATURAL",
  "documentNumber": "12345678",
  "name": "María García López",
  "address": "Jr. Los Olivos 456",
  "phone": "912345678",
  "email": "maria@email.com",
  "description": "Madre soltera con 3 hijos"
}
```

---

### Catálogo de Alimentos (Food Items)

| Método | Endpoint                               | Descripción                                     |
| ------ | -------------------------------------- | ----------------------------------------------- |
| GET    | `/food-items`                          | Obtener todos los alimentos                     |
| GET    | `/food-items/paginated?page=0&size=10` | Obtener alimentos activos paginados             |
| GET    | `/food-items/{id}`                     | Obtener alimento por ID                         |
| GET    | `/food-items/name/{name}`              | Obtener alimento por nombre                     |
| GET    | `/food-items/search?name={name}`       | Buscar alimentos por nombre                     |
| GET    | `/food-items/category/{category}`      | Obtener por categoría (GRANOS, ENLATADOS, etc.) |
| POST   | `/food-items`                          | Crear nuevo alimento                            |
| PUT    | `/food-items/{id}`                     | Actualizar alimento                             |
| DELETE | `/food-items/{id}`                     | Desactivar alimento                             |

**Ejemplo de body (POST/PUT):**

```json
{
  "name": "Arroz",
  "description": "Arroz pilado superior",
  "unitOfMeasure": "KG",
  "category": "GRANOS"
}
```

**Categorías disponibles:**

- GRANOS
- ENLATADOS
- LACTEOS
- ACEITES
- CARNES
- VERDURAS
- FRUTAS
- BEBIDAS
- OTROS

**Unidades de medida:**

- KG (Kilogramo)
- LT (Litro)
- UN (Unidad)

---

## ⚙️ Módulo de Operaciones

### Donaciones (Donations)

| Método | Endpoint                                                        | Descripción                                         |
| ------ | --------------------------------------------------------------- | --------------------------------------------------- |
| GET    | `/donations?page=0&size=10`                                     | Obtener todas las donaciones paginadas              |
| GET    | `/donations/{id}`                                               | Obtener donación por ID                             |
| GET    | `/donations/code/{code}`                                        | Obtener donación por código                         |
| GET    | `/donations/date-range?startDate=2025-01-01&endDate=2025-12-31` | Obtener por rango de fechas                         |
| GET    | `/donations/donor/{donorId}`                                    | Obtener donaciones de un donante                    |
| GET    | `/donations/status/{status}`                                    | Obtener por estado (RECEIVED, VALIDATED, CANCELLED) |
| GET    | `/donations/{id}/details`                                       | Obtener detalles de una donación                    |
| POST   | `/donations`                                                    | Crear nueva donación (actualiza inventario)         |
| PATCH  | `/donations/{id}/status?status=VALIDATED`                       | Actualizar estado                                   |

**Ejemplo de body (POST):**

```json
{
  "donor": {
    "idDonor": 1
  },
  "donationDate": "2025-12-02",
  "receivedBy": {
    "idUser": 1
  },
  "observations": "Donación mensual",
  "details": [
    {
      "foodItem": {
        "idFoodItem": 1
      },
      "quantity": 50.0,
      "batchNumber": "LOT-2025-001",
      "expirationDate": "2026-12-31",
      "observations": "En buen estado"
    }
  ]
}
```

---

### Distribuciones (Distributions)

| Método | Endpoint                                                            | Descripción                                          |
| ------ | ------------------------------------------------------------------- | ---------------------------------------------------- |
| GET    | `/distributions?page=0&size=10`                                     | Obtener todas las distribuciones paginadas           |
| GET    | `/distributions/{id}`                                               | Obtener distribución por ID                          |
| GET    | `/distributions/code/{code}`                                        | Obtener distribución por código                      |
| GET    | `/distributions/date-range?startDate=2025-01-01&endDate=2025-12-31` | Obtener por rango de fechas                          |
| GET    | `/distributions/beneficiary/{beneficiaryId}`                        | Obtener distribuciones a un beneficiario             |
| GET    | `/distributions/status/{status}`                                    | Obtener por estado (DELIVERED, VALIDATED, CANCELLED) |
| GET    | `/distributions/{id}/details`                                       | Obtener detalles de una distribución                 |
| POST   | `/distributions`                                                    | Crear nueva distribución (actualiza inventario)      |
| PATCH  | `/distributions/{id}/status?status=VALIDATED`                       | Actualizar estado                                    |

**Ejemplo de body (POST):**

```json
{
  "beneficiary": {
    "idBeneficiary": 1
  },
  "distributionDate": "2025-12-02",
  "deliveredBy": {
    "idUser": 1
  },
  "observations": "Entrega semanal",
  "details": [
    {
      "foodItem": {
        "idFoodItem": 1
      },
      "quantity": 10.0,
      "batchNumber": "LOT-2025-001",
      "observations": "Entrega completa"
    }
  ]
}
```

---

## 📦 Módulo de Inventario

### Inventario (Inventory)

| Método | Endpoint                                                | Descripción                                        |
| ------ | ------------------------------------------------------- | -------------------------------------------------- |
| GET    | `/inventory`                                            | Obtener todo el inventario                         |
| GET    | `/inventory/active?page=0&size=10`                      | Obtener inventario activo ordenado por vencimiento |
| GET    | `/inventory/{id}`                                       | Obtener registro de inventario por ID              |
| GET    | `/inventory/food-item/{foodItemId}`                     | Obtener todos los lotes de un alimento             |
| GET    | `/inventory/food-item/{foodItemId}/batch/{batchNumber}` | Obtener lote específico                            |
| GET    | `/inventory/expired?date=2025-12-02`                    | Obtener alimentos vencidos                         |
| GET    | `/inventory/category/{category}`                        | Obtener por categoría                              |
| GET    | `/inventory/ordered-by-quantity`                        | Obtener ordenado por cantidad disponible           |
| PUT    | `/inventory/{id}`                                       | Actualizar información de inventario               |

**Ejemplo de respuesta:**

```json
{
  "idInventory": 1,
  "foodItem": {
    "idFoodItem": 1,
    "name": "Arroz",
    "category": "GRANOS"
  },
  "batchNumber": "LOT-2025-001",
  "currentQuantity": 40.0,
  "initialQuantity": 50.0,
  "expirationDate": "2026-12-31",
  "entryDate": "2025-12-02",
  "lastMovementDate": "2025-12-02T10:30:00",
  "donor": {
    "idDonor": 1,
    "businessName": "Plaza Vea S.A."
  },
  "location": "Estante A3",
  "active": true
}
```

---

## 🔒 Módulo de Seguridad (Existente)

### Autenticación

| Método | Endpoint      | Descripción    |
| ------ | ------------- | -------------- |
| POST   | `/auth/login` | Iniciar sesión |

**Body:**

```json
{
  "email": "admin@example.com",
  "password": "password123"
}
```

---

### Usuarios

| Método | Endpoint      | Descripción                |
| ------ | ------------- | -------------------------- |
| GET    | `/users`      | Obtener todos los usuarios |
| GET    | `/users/{id}` | Obtener usuario por ID     |
| POST   | `/users`      | Crear nuevo usuario        |
| PUT    | `/users/{id}` | Actualizar usuario         |
| DELETE | `/users/{id}` | Eliminar usuario           |

---

### Perfiles

| Método | Endpoint         | Descripción                |
| ------ | ---------------- | -------------------------- |
| GET    | `/profiles`      | Obtener todos los perfiles |
| GET    | `/profiles/{id}` | Obtener perfil por ID      |
| POST   | `/profiles`      | Crear nuevo perfil         |
| PUT    | `/profiles/{id}` | Actualizar perfil          |
| DELETE | `/profiles/{id}` | Eliminar perfil            |

---

## 🔄 Lógica de Negocio Automática

### Al crear una Donación:

1. ✅ Se genera código único (DON-2025-0001)
2. ✅ Se validan donante y usuario
3. ✅ Se procesan los detalles
4. ✅ **Se SUMA al inventario automáticamente**

### Al crear una Distribución:

1. ✅ Se genera código único (DIS-2025-0001)
2. ✅ Se validan beneficiario y usuario
3. ✅ Se valida disponibilidad de stock
4. ✅ **Se RESTA del inventario automáticamente**
5. ❌ Falla si no hay suficiente stock

---

## 📊 Códigos de Respuesta HTTP

| Código | Descripción                                |
| ------ | ------------------------------------------ |
| 200    | OK - Operación exitosa                     |
| 201    | Created - Recurso creado exitosamente      |
| 204    | No Content - Eliminación exitosa           |
| 400    | Bad Request - Error en los datos enviados  |
| 404    | Not Found - Recurso no encontrado          |
| 500    | Internal Server Error - Error del servidor |

---

## 🧪 Pruebas con Postman/Insomnia

### Flujo de Prueba Completo:

1. **Crear Donante** (POST `/donors`)
2. **Crear Alimento** (POST `/food-items`)
3. **Crear Usuario** (si no existe)
4. **Crear Donación** (POST `/donations`) → Verifica que el inventario se actualice
5. **Consultar Inventario** (GET `/inventory/active`)
6. **Crear Beneficiario** (POST `/beneficiaries`)
7. **Crear Distribución** (POST `/distributions`) → Verifica que el inventario se descuente
8. **Consultar Inventario** nuevamente para ver el cambio

---

## 📝 Notas Importantes

- Todos los endpoints tienen CORS habilitado (`@CrossOrigin(origins = "*")`)
- Las fechas deben enviarse en formato ISO: `YYYY-MM-DD`
- Las cantidades usan BigDecimal para precisión decimal
- Los códigos de donación/distribución se generan automáticamente
- El inventario se actualiza de forma transaccional (todo o nada)

---

## ✨ Mejoras de Código Implementadas

### Transaccionalidad

- `@Transactional` solo se aplica en la **capa de servicio**, no en controladores
- Los controladores delegan toda la lógica de negocio a los servicios
- Las operaciones de inventario son atómicas (se completan o fallan completamente)

### Código Limpio

- Sin emojis en mensajes de sistema
- Sin anotaciones completamente calificadas innecesarias
- Importaciones limpias y optimizadas
- System.out.println usado solo para logs de inicialización (considerar migrar a SLF4J)

### Validaciones

- Validación de existencia de entidades relacionadas antes de crear registros
- Generación automática de códigos únicos para donaciones y distribuciones
- Validación de stock disponible antes de distribuciones

---

## 🔧 Recomendaciones de Desarrollo

1. **Logging**: Migrar de `System.out.println` a un framework de logging apropiado (SLF4J + Logback)
2. **Validación**: Implementar Bean Validation con anotaciones `@Valid` en los DTOs
3. **Exception Handling**: Crear un `@ControllerAdvice` global para manejo de excepciones
4. **Seguridad**: Implementar Spring Security con JWT para autenticación y autorización
5. **Testing**: Agregar tests unitarios y de integración para servicios críticos
