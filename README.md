# pasteleria-backend

Backend de la plataforma de venta de pasteles de **Innovatech Chile**, desarrollado para la
Evaluación Final Transversal (EFT) de ISY1101 — Introducción a Herramientas DevOps, DuocUC.

Stack: **Spring Boot 3.4.4 / Java 17 / Maven**, base de datos **MySQL 8.0** (patrón sidecar en el
mismo Task de ECS, igual que en el proyecto EP3 previo).

## Endpoints

### Pasteles (catálogo)

| Método | Ruta                | Descripción                                  |
|--------|---------------------|-----------------------------------------------|
| POST   | `/api/pasteles`     | Crea un pastel (nombre, descripción, precio, fotoUrl, stock) |
| GET    | `/api/pasteles`     | Lista todos los pasteles (pantalla principal) |
| GET    | `/api/pasteles/{id}`| Obtiene un pastel por id                      |
| PUT    | `/api/pasteles/{id}`| Actualiza un pastel                           |
| DELETE | `/api/pasteles/{id}`| Elimina un pastel                             |

Body de ejemplo para `POST /api/pasteles`:

```json
{
  "nombre": "Torta de Chocolate",
  "descripcion": "Bizcocho de chocolate con ganache y frutos rojos",
  "precio": 15990,
  "fotoUrl": "https://example.com/imagenes/torta-chocolate.jpg",
  "stock": 10
}
```

### Ventas

| Método | Ruta          | Descripción                                            |
|--------|---------------|----------------------------------------------------------|
| POST   | `/api/ventas` | Registra una venta (pastelId + cantidad), descuenta stock |
| GET    | `/api/ventas` | Lista el historial de ventas (más reciente primero, con fecha) |

Body de ejemplo para `POST /api/ventas`:

```json
{
  "pastelId": 1,
  "cantidad": 3
}
```

### Salud (para el ALB)

`GET /actuator/health` — usado como health check del Target Group en ECS.

## Ejecutar localmente

```bash
mvn spring-boot:run
```

Por defecto usa `127.0.0.1:3306` como base de datos. Para levantar todo junto (backend + frontend +
MySQL), usar el `docker-compose.yml` en la raíz del proyecto.

## Variables de entorno

| Variable      | Default            | Descripción                    |
|---------------|---------------------|---------------------------------|
| `DB_HOST`     | `127.0.0.1`         | Host de MySQL                   |
| `DB_PORT`     | `3306`              | Puerto de MySQL                 |
| `DB_NAME`     | `pasteleria_db`     | Nombre de la base de datos       |
| `DB_USER`     | `pasteleria_user`   | Usuario de la base de datos      |
| `DB_PASSWORD` | `pasteleria_pass`   | Password de la base de datos     |

> **Nota sobre secretos e IAM (para el informe):** en `ecs/task-definition.json` las credenciales de
> MySQL van como `environment` en texto plano solo para simplificar el demo académico. Para la
> justificación de "mínimo privilegio" en el informe, se recomienda documentar cómo se migrarían a
> `secrets` (`valueFrom`) apuntando a **AWS Secrets Manager** o **SSM Parameter Store**, con un rol de
> ejecución de ECS (`executionRoleArn`) que solo tenga permiso `secretsmanager:GetSecretValue` sobre
> ese secreto puntual, no acceso amplio a todos los secretos de la cuenta.

## CI/CD

El workflow `.github/workflows/deploy.yml` se dispara en push a la rama `deploy` y ejecuta:
`test` → `build` (Docker multietapa) → `push` a Amazon ECR (`pasteleria_backend`) → render de
task definition → `deploy` a ECS (servicio `pasteleria-backend-service` en el clúster
`pasteleria_cluster`).

### GitHub Secrets requeridos

- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_SESSION_TOKEN`
- `AWS_ACCOUNT_ID`

## Seguridad básica aplicada

- Imagen final `eclipse-temurin:17-jre-alpine` (minimalista, sin JDK completo)
- Build multietapa: el JDK/Maven de compilación no viaja a la imagen final
- Usuario no-root (`spring:spring`) dentro del contenedor
- Solo el puerto 8083 expuesto
- Security Group de ECS solo permite tráfico desde el Security Group del ALB
