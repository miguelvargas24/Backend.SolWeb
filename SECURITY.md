# 🔒 Configuración de Seguridad - Backend

## ⚠️ IMPORTANTE: Protección de Datos Sensibles

Este proyecto utiliza `.gitignore` para proteger información sensible. **NUNCA** subas al repositorio:

- ✅ Credenciales de bases de datos
- ✅ Contraseñas
- ✅ Claves API
- ✅ Certificados SSL
- ✅ Datos de usuarios reales
- ✅ Archivos de configuración local

---

## 📋 Configuración Inicial

### 1. Configurar Base de Datos

1. **Copia el archivo de ejemplo:**
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

2. **Edita `application.properties`** con tus credenciales locales:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/TU_BASE_DE_DATOS
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=TU_CONTRASEÑA
   ```

3. **El archivo `application.properties` está en `.gitignore`** - No se subirá a GitHub

---

## 🗄️ Bases de Datos Soportadas

### PostgreSQL (Por defecto)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/BankAliment
spring.datasource.username=postgres
spring.datasource.password=TuPassword
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

### SQL Server (Alternativa)
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=BankAliment;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=TuPassword
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.jpa.database-platform=org.hibernate.dialect.SQLServerDialect
```

---

## 📁 Archivos Ignorados por Git

El `.gitignore` está configurado para excluir:

### 🔐 Configuraciones Sensibles
- `application-*.properties` (excepto `.example`)
- `.env` y variables de entorno
- Archivos de configuración local

### 🗄️ Base de Datos
- Archivos `.sql`, `.dump`, `.backup`
- Bases de datos embebidas (H2, SQLite)
- Migraciones locales
- Scripts de datos de prueba

### 📝 Logs y Temporales
- Todos los archivos `.log`
- Archivos temporales (`.tmp`, `.temp`)
- Caché

### 📤 Archivos Subidos
- Directorios `/uploads/`, `/files/`, `/media/`
- Imágenes y documentos subidos por usuarios

### 🔑 Certificados y Claves
- Certificados SSL (`.pem`, `.key`, `.crt`)
- Keystores (`.jks`, `.p12`, `.pfx`)
- Archivos de secretos

### 📊 Reportes y Documentación
- Reportes de cobertura
- Documentación generada automáticamente

---

## 🚀 Ejecutar el Proyecto

```bash
# Compilar
./mvnw clean install

# Ejecutar
./mvnw spring-boot:run
```

---

## 🛡️ Mejores Prácticas

1. **Nunca hagas commit de credenciales reales**
2. **Usa variables de entorno para producción**
3. **Mantén `application.properties.example` actualizado**
4. **Revisa el `.gitignore` antes de hacer commit**
5. **Usa diferentes perfiles para diferentes entornos:**
   - `application-dev.properties` (desarrollo)
   - `application-prod.properties` (producción)
   - `application-local.properties` (local)

---

## 🔍 Verificar Archivos Antes de Commit

Antes de hacer commit, verifica que no estés subiendo archivos sensibles:

```bash
# Ver archivos que se van a subir
git status

# Ver diferencias
git diff

# Verificar que application.properties NO aparezca
git ls-files | grep application.properties
```

Si `application.properties` aparece, significa que está siendo rastreado. Elimínalo del índice:

```bash
git rm --cached src/main/resources/application.properties
```

---

## 📞 Soporte

Si tienes dudas sobre la configuración, consulta:
- `application.properties.example` - Plantilla de configuración
- `.gitignore` - Archivos excluidos del repositorio
