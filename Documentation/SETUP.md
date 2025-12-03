# Guía de Configuración - Backend

## Requisitos Previos

- **Java**: JDK 21
- **Maven**: 3.8 o superior
- **Base de Datos**: PostgreSQL 14+ (o la que uses)
- **IDE**: IntelliJ IDEA, Eclipse, o VS Code con extensiones Java

---

## Configuración Inicial

### 1. Clonar el Repositorio

```bash
cd backend
```

### 2. Configurar la Base de Datos

**Crear archivo `application.properties`:**

1. Copia el archivo de ejemplo:
   ```bash
   cd src/main/resources
   cp application.properties.example application.properties
   ```

2. Edita `application.properties` con tus datos locales:
   ```properties
   # Configuración de Base de Datos LOCAL
   spring.datasource.url=jdbc:postgresql://localhost:5432/banco_alimentos
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=TU_PASSWORD
   
   # JPA
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   
   # Puerto del servidor (opcional)
   server.port=8080
   ```

3. **⚠️ IMPORTANTE:** El archivo `application.properties` está en `.gitignore` y **NO se subirá a Git**. Esto protege tus credenciales.

### 3. Crear la Base de Datos

```sql
CREATE DATABASE banco_alimentos;
```

### 4. Instalar Dependencias

```bash
# Windows
mvnw.cmd clean install

# Linux/Mac
./mvnw clean install
```

### 5. Ejecutar el Proyecto

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

El backend estará disponible en: `http://localhost:8080`

---

## Estructura de Archivos de Configuración

```
src/main/resources/
├── application.properties.example  ✅ SE SUBE A GIT (plantilla)
├── application.properties          ❌ NO SE SUBE (tu configuración local)
└── db/
    └── migration/                  ✅ SE SUBEN (migraciones versionadas)
        └── local/                  ❌ NO SE SUBEN (scripts locales)
```

---

## Datos Iniciales

El sistema crea automáticamente al iniciar:

- **3 Perfiles**: Administrador, Sub Administrador, Trabajador
- **3 Usuarios de prueba**:
  - `admin@bancoalimentos.com` / `admin123`
  - `subadmin@bancoalimentos.com` / `subadmin123`
  - `trabajador@bancoalimentos.com` / `trabajador123`

---

## Comandos Útiles

### Limpiar y compilar
```bash
mvnw.cmd clean compile
```

### Ejecutar tests
```bash
mvnw.cmd test
```

### Crear JAR para producción
```bash
mvnw.cmd clean package
```

### Ver dependencias
```bash
mvnw.cmd dependency:tree
```

---

## Solución de Problemas Comunes

### Error de conexión a base de datos
- Verifica que PostgreSQL esté corriendo
- Verifica credenciales en `application.properties`
- Verifica que la base de datos exista

### Error de puerto ocupado
- Cambia el puerto en `application.properties`:
  ```properties
  server.port=8081
  ```

### Limpiar caché de Maven
```bash
mvnw.cmd clean
```

---

## Endpoints Principales

- API Base: `http://localhost:8080/api`
- Login: `POST http://localhost:8080/api/auth/login`
- Swagger (si está configurado): `http://localhost:8080/swagger-ui.html`

---

## Notas Importantes

1. **Nunca subas** `application.properties` con credenciales reales
2. **Siempre usa** `application.properties.example` como plantilla
3. Las **migraciones de DB** en `/src/main/resources/db/migration/` SÍ se suben
4. Los **logs** no se suben a Git
5. La carpeta **target/** no se sube (archivos compilados)

---

## Contacto

Para dudas sobre configuración, consulta:
- [Documentación API](./Documentation/API_Endpoints.md)
- [Flujo del Sistema](./Documentation/FLUJO_SISTEMA.md)
