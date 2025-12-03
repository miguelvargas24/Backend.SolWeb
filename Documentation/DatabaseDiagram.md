# Diagrama de Base de Datos - Sistema Banco de Alimentos

## Estructura de Tablas y Relaciones

### 📋 Módulo de Registros (Maestros)

#### 1. **donors** (Donantes/Empresas)

```
idDonor (PK)
ruc (UNIQUE)
businessName
address
contactName
contactPhone
contactEmail
active
createdAt
updatedAt
```

#### 2. **beneficiaries** (Beneficiarios)

```
idBeneficiary (PK)
type (ENUM: PERSONA_NATURAL, ASOCIACION)
documentNumber (UNIQUE)
name
address
phone
email
representativeName
description
active
createdAt
updatedAt
```

#### 3. **food_items** (Catálogo de Alimentos)

```
idFoodItem (PK)
name (UNIQUE)
description
unitOfMeasure (ENUM: KG, LT, UN)
category (ENUM: GRANOS, ENLATADOS, LACTEOS, etc.)
active
createdAt
updatedAt
```

---

### ⚙️ Módulo de Operaciones

#### 4. **donations** (Donaciones - Entradas)

```
idDonation (PK)
donationCode (UNIQUE)
donor_id (FK → donors)
donationDate
received_by_user_id (FK → users)
observations
status (ENUM: RECEIVED, VALIDATED, CANCELLED)
createdAt
updatedAt
```

#### 5. **donation_details** (Detalles de Donación)

```
idDonationDetail (PK)
donation_id (FK → donations)
food_item_id (FK → food_items)
quantity
batchNumber
expirationDate
observations
```

#### 6. **distributions** (Distribuciones - Salidas)

```
idDistribution (PK)
distributionCode (UNIQUE)
beneficiary_id (FK → beneficiaries)
distributionDate
delivered_by_user_id (FK → users)
observations
status (ENUM: DELIVERED, VALIDATED, CANCELLED)
createdAt
updatedAt
```

#### 7. **distribution_details** (Detalles de Distribución)

```
idDistributionDetail (PK)
distribution_id (FK → distributions)
food_item_id (FK → food_items)
quantity
batchNumber
observations
```

---

### 📦 Módulo de Inventario

#### 8. **inventory** (Stock Actual)

```
idInventory (PK)
food_item_id (FK → food_items)
batchNumber
currentQuantity
initialQuantity
expirationDate
entryDate
lastMovementDate
donor_id (FK → donors)
location
active
createdAt
updatedAt

CONSTRAINT: UNIQUE(food_item_id, batchNumber)
```

---

### 🔒 Módulo de Seguridad (Existente)

#### 9. **users** (Usuarios)

```
idUser (PK)
name
email
password
active
profile_id (FK → profiles)
```

#### 10. **profiles** (Perfiles)

```
idProfile (PK)
name (UNIQUE)
description
```

#### 11. **profile_permissions** (Permisos de Perfiles)

```
idProfilePermission (PK)
profile_id (FK → profiles)
moduleName
canCreate
canRead
canUpdate
canDelete
```

---

## Diagrama de Relaciones

```
┌─────────────┐
│   DONORS    │
└──────┬──────┘
       │
       │ 1:N
       ▼
┌─────────────┐      1:N      ┌──────────────────┐
│  DONATIONS  │───────────────▶│ DONATION_DETAILS │
└──────┬──────┘                └────────┬─────────┘
       │                                │
       │ N:1                            │ N:1
       ▼                                ▼
┌─────────────┐                ┌──────────────┐
│    USERS    │                │  FOOD_ITEMS  │
└─────────────┘                └──────┬───────┘
                                      │
                                      │ 1:N
                                      ▼
                              ┌──────────────┐
                              │  INVENTORY   │
                              └──────────────┘
                                      ▲
                                      │ N:1
                                      │
┌──────────────┐      1:N      ┌─────┴────────────────┐
│DISTRIBUTIONS │───────────────▶│ DISTRIBUTION_DETAILS │
└──────┬───────┘                └──────────────────────┘
       │
       │ N:1
       ▼
┌──────────────┐
│ BENEFICIARIES│
└──────────────┘
```

---

## Flujo de Datos

### 1. **Entrada de Alimentos (Donación)**

```
Donation (registro principal)
  ↓
DonationDetail (cada alimento)
  ↓
Inventory.addStock() [SUMA al stock]
```

### 2. **Salida de Alimentos (Distribución)**

```
Distribution (registro principal)
  ↓
DistributionDetail (cada alimento)
  ↓
Inventory.subtractStock() [RESTA del stock]
```

### 3. **Consulta de Stock**

```
Inventory
  ├─ Por alimento (food_item_id)
  ├─ Por categoría (food_items.category)
  ├─ Por lote (batchNumber)
  ├─ Por fecha de vencimiento
  └─ Por cantidad disponible
```

---

## Índices Recomendados

Para optimizar el rendimiento:

```sql
-- Índices en donors
CREATE INDEX idx_donors_ruc ON donors(ruc);
CREATE INDEX idx_donors_active ON donors(active);

-- Índices en beneficiaries
CREATE INDEX idx_beneficiaries_document ON beneficiaries(documentNumber);
CREATE INDEX idx_beneficiaries_type ON beneficiaries(type);

-- Índices en food_items
CREATE INDEX idx_food_items_category ON food_items(category);
CREATE INDEX idx_food_items_active ON food_items(active);

-- Índices en donations
CREATE INDEX idx_donations_code ON donations(donationCode);
CREATE INDEX idx_donations_date ON donations(donationDate);
CREATE INDEX idx_donations_donor ON donations(donor_id);

-- Índices en distributions
CREATE INDEX idx_distributions_code ON distributions(distributionCode);
CREATE INDEX idx_distributions_date ON distributions(distributionDate);
CREATE INDEX idx_distributions_beneficiary ON distributions(beneficiary_id);

-- Índices en inventory
CREATE INDEX idx_inventory_food_batch ON inventory(food_item_id, batchNumber);
CREATE INDEX idx_inventory_expiration ON inventory(expirationDate);
CREATE INDEX idx_inventory_active ON inventory(active);
```

---

## Reglas de Negocio Implementadas

1. ✅ **Al crear una donación**: Se suma automáticamente al inventario
2. ✅ **Al crear una distribución**: Se resta automáticamente del inventario
3. ✅ **Validación de stock**: No se puede distribuir más de lo disponible
4. ✅ **Control por lote**: Cada lote se gestiona independientemente
5. ✅ **Trazabilidad**: Se registra qué donante proveyó cada lote
6. ✅ **Auditoría**: Todas las tablas tienen createdAt/updatedAt
7. ✅ **Soft delete**: Los registros se marcan como inactivos, no se eliminan
