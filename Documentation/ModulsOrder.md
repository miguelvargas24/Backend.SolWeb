# Arquitectura de Módulos - Sistema Banco de Alimentos

**Última actualización:** 3 de Diciembre, 2025

## Visión General

Esta estructura cubre el flujo principal: **Entrada (Donación) → Almacenamiento → Salida (Entrega)**.

---

## Propuesta de Arquitectura de Módulos para el MVP

### 1. Módulo: Gestión de Registros (Maestros)

Este módulo centraliza la data estática necesaria antes de poder operar (quién dona, qué se dona y a quién se entrega).

#### Sub-módulo: Registro de Donantes (Empresas)

- **Función:** CRUD (Crear, Leer, Actualizar, Borrar) de las empresas aliadas (ej. Plaza Vea, KFC)
- **Datos clave:** RUC, Razón Social, Dirección, Contacto

#### Sub-módulo: Registro de Beneficiarios

- **Función:** Gestión unificada de los receptores de ayuda
- **Detalle:** Debe permitir registrar tanto **Personas Naturales** (madres solteras, adultos mayores) como **Asociaciones** (comedores, albergues)

#### Sub-módulo: Catálogo de Alimentos

- **Función:** Definir los tipos de alimentos que el sistema acepta (ej. Arroz, Aceite, Atún) para estandarizar los nombres y pesos
- **Objetivo:** Evitar que un usuario escriba "Arroz costeño" y otro "Arroz bolsa"

---

### 2. Módulo: Operaciones (Core del Negocio)

Aquí ocurren las transacciones diarias del banco de alimentos.

#### Sub-módulo: Recepción de Donaciones (Entradas)

- **Función:** Registrar el ingreso de alimentos al almacén
- **Flujo:**
  1. Seleccionar Empresa Donante
  2. Seleccionar Alimentos del Catálogo
  3. Ingresar Cantidad/Peso
  4. Guardar
- **Acción automática:** Al guardar, debe sumar al stock del inventario

#### Sub-módulo: Distribución de Alimentos (Salidas)

- **Función:** Registrar la entrega de alimentos a los beneficiarios
- **Flujo:**
  1. Seleccionar Beneficiario
  2. Seleccionar Lote/Alimento
  3. Ingresar Cantidad a entregar
  4. Guardar
- **Acción automática:** Al guardar, debe restar del stock del inventario

---

### 3. Módulo: Inventario (Independiente)

Este módulo es principalmente de consulta y monitoreo, vital para la transparencia.

#### Sin sub-módulos (Vista Directa)

- **Función:** Visualización en tiempo real del **Stock Actual**
- **Características:**
  - Tabla que muestra: **Producto | Cantidad Total | Fecha de última entrada**
  - Debe permitir filtrar por tipo de alimento para ver disponibilidad rápida antes de una entrega

---

### 4. Módulo: Reportes (Independiente)

Necesario para la toma de decisiones y transparencia administrativa.

#### Sin sub-módulos (Panel de Reportes)

- **Reporte de Movimientos:** Historial de entradas y salidas por rango de fechas
- **Reporte de Beneficiarios:** Listado de quiénes han recibido ayuda y qué cantidad

---

## Resumen Visual del Menú Lateral (Sidebar)

Para la interfaz en Angular, el menú quedaría así:

```
📊 Dashboard (Resumen general)
🔒 Seguridad (Módulo actual: Usuarios/Perfiles)
📋 Registros ← (Módulo Padre)
   ├── Donantes
   ├── Beneficiarios
   └── Alimentos
⚙️ Operaciones ← (Módulo Padre)
   ├── Recepción (Donaciones)
   └── Distribución (Entregas)
📦 Inventario ← (Módulo Independiente)
📈 Reportes ← (Módulo Independiente)
```

---

## ¿Por qué esta estructura?

Esta organización separa las responsabilidades lógicamente según la arquitectura de capas:

1. **Módulo de Registros:** Alimenta la base de datos con información maestra
2. **Módulo de Operaciones:** Maneja la lógica de negocio transaccional
3. **Módulo de Inventario:** Consulta y control de stock en tiempo real
4. **Módulo de Reportes:** Análisis y transparencia administrativa

Esta separación facilita:

- ✅ Mantenimiento y escalabilidad del código
- ✅ Asignación de permisos por módulo
- ✅ Desarrollo paralelo por equipos
- ✅ Testing independiente de cada componente
