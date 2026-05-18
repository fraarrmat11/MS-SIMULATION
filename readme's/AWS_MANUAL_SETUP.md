# Guia Manual De Setup En AWS

Esta guia explica como preparar AWS manualmente para desplegar MS-SIMULATION en ECS Fargate.

La arquitectura objetivo es:

```text
GitHub Actions
  -> construye imagenes Docker
  -> sube imagenes a Amazon ECR
  -> despliega nuevas task definitions en Amazon ECS Fargate

ECS Fargate
  -> ms-time-service-2026-atmy, puerto de contenedor 8081
  -> ms-map-service-2026-atmy, puerto de contenedor 8080

Dependencias compartidas
  -> Amazon RDS PostgreSQL
  -> CloudAMQP RabbitMQ
  -> AWS Systems Manager Parameter Store
  -> Amazon CloudWatch Logs
```

Esta guia asume que los recursos de AWS se crean manualmente desde la consola de AWS, salvo que se indique algun comando como opcion.

## 1. Elegir Region Y Nombres

Elige una unica region de AWS para todo el despliegue, por ejemplo:

```text
eu-west-1
```

Usa nombres consistentes:

```text
ECS cluster: junior-workshop-2026-vlc
ECR repository: ms-time-2026-atmy
ECR repository: ms-map-2026-atmy
ECS service: ms-time-service-2026-atmy
ECS service: ms-map-service-2026-atmy
Task definition family: ms-time
Task definition family: ms-map
CloudWatch log group: /ecs/ms-time-2026-atmy
CloudWatch log group: /ecs/ms-map-2026-atmy
```

Por que: usar nombres predecibles hace que el workflow de GitHub Actions y las task definitions sean mas faciles de leer y menos propensos a errores.

## 2. Preparar Networking

Necesitas una VPC con al menos dos subnets.

Configuracion recomendada:

- Subnets publicas para un Application Load Balancer opcional.
- Subnets privadas para las tareas de ECS Fargate y RDS.
- NAT Gateway o VPC endpoints para que las tareas de ECS puedan descargar imagenes de ECR y escribir logs en CloudWatch.

Configuracion mas simple y de menor coste:

- Las tareas de ECS pueden ejecutarse en subnets publicas con IP publica asignada.
- Restringe el trafico entrante con security groups.
- Mantiene RDS privado y accesible solo desde las tareas de ECS.

Por que: las tareas de Fargate necesitan acceso de red para descargar imagenes, escribir logs, llegar a RDS y llegar a CloudAMQP. RDS no deberia estar abierto publicamente; deberia aceptar trafico solo desde las tareas de aplicacion.

## 3. Crear Security Groups

Crea estos security groups:

```text
ms-simulation-ecs-tasks-sg
ms-simulation-rds-sg
opcional: ms-simulation-alb-sg
```

### Security Group De ECS Tasks

Outbound:

- Permitir HTTPS `443` para llegar a APIs de AWS, ECR, CloudWatch y CloudAMQP sobre TLS.
- Permitir PostgreSQL `5432` hacia el security group de RDS.
- Permitir RabbitMQ TLS `5671` hacia CloudAMQP.

Inbound:

- Si usas ALB, permitir trafico solo desde el security group del ALB hacia:
  - `8081` para `ms-time`
  - `8080` para `ms-map`
- Si no usas ALB, abre solo los puertos realmente necesarios y restringe las IPs de origen.

### Security Group De RDS

Inbound:

- Permitir PostgreSQL `5432` solo desde `ms-simulation-ecs-tasks-sg`.

Por que: esto mantiene la base de datos aislada de internet, pero permite que los contenedores se conecten a ella.

## 4. Crear Amazon RDS PostgreSQL

Crea una instancia RDS PostgreSQL.

Configuracion basica sugerida:

```text
Engine: PostgreSQL
Public access: No
Database name: ms_simulation
Port: 5432
Security group: ms-simulation-rds-sg
```

Guarda estos valores para mas adelante:

```text
RDS endpoint
Database name
Database username
Database password
```

Por que: `ms-time` y `ms-map` ya usan PostgreSQL mediante Spring Data JPA. RDS nos da una base de datos gestionada sin ejecutar PostgreSQL dentro de los contenedores de aplicacion.

## 5. Crear CloudAMQP RabbitMQ

Crea una instancia RabbitMQ en CloudAMQP.

Desde CloudAMQP, recoge:

```text
Host
Port, normalmente 5671 para amqps/TLS
Username
Password
Virtual host
```

Usa TLS siempre que sea posible:

```text
RABBITMQ_PORT=5671
RABBITMQ_SSL_ENABLED=true
```

Por que: la aplicacion ya habla RabbitMQ. CloudAMQP nos permite mantener el contrato RabbitMQ sin operar un servidor RabbitMQ propio.

## 6. Guardar Configuracion Runtime En Parameter Store

Crea parametros en AWS Systems Manager Parameter Store.

Usa esta convencion de nombres:

```text
/ms-simulation/prod/db/url
/ms-simulation/prod/db/username
/ms-simulation/prod/db/password
/ms-simulation/prod/rabbitmq/host
/ms-simulation/prod/rabbitmq/username
/ms-simulation/prod/rabbitmq/password
/ms-simulation/prod/rabbitmq/vhost
```

Tipos de parametro sugeridos:

| Parametro | Tipo sugerido |
| --- | --- |
| `/ms-simulation/prod/db/url` | `String` o `SecureString` |
| `/ms-simulation/prod/db/username` | `SecureString` |
| `/ms-simulation/prod/db/password` | `SecureString` |
| `/ms-simulation/prod/rabbitmq/host` | `String` o `SecureString` |
| `/ms-simulation/prod/rabbitmq/username` | `SecureString` |
| `/ms-simulation/prod/rabbitmq/password` | `SecureString` |
| `/ms-simulation/prod/rabbitmq/vhost` | `String` o `SecureString` |

Valores esperados:

```text
/ms-simulation/prod/db/url = jdbc:postgresql://<rds-endpoint>:5432/<database-name>
/ms-simulation/prod/db/username = <database-user>
/ms-simulation/prod/db/password = <database-password>
/ms-simulation/prod/rabbitmq/host = <cloudamqp-host>
/ms-simulation/prod/rabbitmq/username = <cloudamqp-user>
/ms-simulation/prod/rabbitmq/password = <cloudamqp-password>
/ms-simulation/prod/rabbitmq/vhost = <cloudamqp-vhost>
```

Por que: ECS puede inyectar estos parametros dentro de los contenedores sin commitear secretos en el repositorio ni imprimirlos en los logs del pipeline.

## 7. Crear Repositorios ECR

Crea dos repositorios privados en ECR:

```text
ms-time-2026-atmy
ms-map-2026-atmy
```

Por que: ECR almacena las imagenes Docker que ejecutara ECS Fargate. Mantener un repositorio por modulo deja mas clara la propiedad de cada imagen y el despliegue.

## 8. Crear CloudWatch Log Groups

Crea estos log groups:

```text
/ecs/ms-time-2026-atmy
/ecs/ms-map-2026-atmy
```

Retencion sugerida:

```text
7 dias para un entorno workshop/dev
30 dias o mas para entornos mas duraderos
```

Por que: ECS envia `stdout` y `stderr` de los contenedores a CloudWatch Logs. Sin logs, diagnosticar fallos de despliegue es mucho mas dificil.

## 9. Crear Roles IAM Para ECS

Necesitas al menos estos roles IAM:

```text
ecsTaskExecutionRole
ms-time-task-role
ms-map-task-role
```

### ecsTaskExecutionRole

Este rol lo usa ECS para arrancar la tarea.

Necesita permisos para:

- Descargar imagenes desde ECR.
- Escribir logs en CloudWatch.
- Leer valores de SSM/Secrets Manager referenciados por la task definition.
- Desencriptar parametros `SecureString` si se usa una KMS key gestionada por el cliente.

Adjunta la policy gestionada por AWS:

```text
AmazonECSTaskExecutionRolePolicy
```

Anade permisos extra para parametros SSM si hace falta:

```json
{
  "Effect": "Allow",
  "Action": [
    "ssm:GetParameters",
    "ssm:GetParameter"
  ],
  "Resource": "arn:aws:ssm:<aws-region>:<aws-account-id>:parameter/ms-simulation/prod/*"
}
```

Si usas `SecureString` con una KMS key gestionada por el cliente, permite tambien:

```json
{
  "Effect": "Allow",
  "Action": "kms:Decrypt",
  "Resource": "<kms-key-arn>"
}
```

Por que: el execution role no es la identidad de la aplicacion. Es la identidad que usa ECS para preparar el runtime del contenedor.

### ms-time-task-role Y ms-map-task-role

Estos roles los usan los contenedores de aplicacion.

Con el codigo actual, la aplicacion no llama directamente a APIs de AWS, asi que estos roles pueden empezar con permisos minimos o sin permisos personalizados.

Por que: los task roles deben estar separados del execution role. Si mas adelante la aplicacion necesita llamar APIs de AWS, se anaden permisos solo al modulo que los necesite.

## 10. Configurar IAM OIDC Para GitHub Actions

Crea un identity provider OIDC para GitHub en IAM:

```text
Provider URL: https://token.actions.githubusercontent.com
Audience: sts.amazonaws.com
```

Crea un rol IAM, por ejemplo:

```text
github-actions-ms-simulation-deploy-role
```

Patron de trust policy:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Federated": "arn:aws:iam::<aws-account-id>:oidc-provider/token.actions.githubusercontent.com"
      },
      "Action": "sts:AssumeRoleWithWebIdentity",
      "Condition": {
        "StringEquals": {
          "token.actions.githubusercontent.com:aud": "sts.amazonaws.com"
        },
        "StringLike": {
          "token.actions.githubusercontent.com:sub": "repo:<github-owner>/<github-repo>:ref:refs/heads/main"
        }
      }
    }
  ]
}
```

Concede a este rol solo los permisos necesarios para desplegar:

- Subir imagenes a los repositorios ECR `ms-time-2026-atmy` y `ms-map-2026-atmy`.
- Registrar ECS task definitions.
- Actualizar `ms-time-service-2026-atmy` y `ms-map-service-2026-atmy`.
- Leer estado actual de tasks y services.
- Pasar los roles de ECS a ECS.

Por que: OIDC evita guardar access keys largas de AWS en GitHub. GitHub obtiene credenciales temporales de AWS solo cuando se ejecuta el workflow.

## 11. Crear El ECS Cluster

Crea un cluster ECS:

```text
junior-workshop-2026-vlc
```

Usa capacidad Fargate.

Por que: un solo cluster puede alojar ambos servicios manteniendo cada servicio desplegado y escalado de forma independiente.

## 12. Registrar ECS Task Definitions

Usa las plantillas ya incluidas en este repositorio:

```text
deployment/ecs/ms-time-task-definition.json
deployment/ecs/ms-map-task-definition.json
```

Antes de registrarlas, reemplaza los placeholders:

```text
<aws-account-id>
<aws-region>
```

Confirma tambien que estos roles existen con esos nombres en tu cuenta AWS:

```text
ecsTaskExecutionRole
ms-time-task-role
ms-map-task-role
```

Comportamiento esperado de las task definitions:

- `ms-time` ejecuta la imagen `ms-time` y escucha en `8081`.
- `ms-map` ejecuta la imagen `ms-map` y escucha en `8080`.
- Ambos usan `SPRING_PROFILES_ACTIVE=prod`.
- Ambos leen configuracion de base de datos y RabbitMQ desde Parameter Store.
- Ambos envian logs a CloudWatch.

Por que: las task definitions son el contrato runtime de ECS. Definen imagen, CPU, memoria, puertos, variables de entorno, secretos y logging.

## 13. Crear ECS Services

Crea dos servicios ECS dentro de `junior-workshop-2026-vlc`:

```text
ms-time-service-2026-atmy
ms-map-service-2026-atmy
```

Configuracion sugerida:

```text
Launch type: Fargate
Desired tasks: 1
Task definition: la task definition del modulo correspondiente
Network mode: awsvpc
Security group: ms-simulation-ecs-tasks-sg
Subnets: subnets de aplicacion seleccionadas
Public IP: depende de tu decision de networking
```

Por que: un ECS service mantiene ejecutandose el numero deseado de tasks. Si una task se detiene, ECS arranca otra de reemplazo.

## 14. Decidir Si Usar Application Load Balancer

Usa un ALB si usuarios o servicios externos necesitan acceder por HTTP a:

```text
ms-time:8081
ms-map:8080
```

Configuracion tipica de ALB:

```text
Listener: HTTP 80 o HTTPS 443
Target group: ms-time-target-group -> container port 8081
Target group: ms-map-target-group -> container port 8080
```

Posible routing:

```text
/time/* -> ms-time-service-2026-atmy
/map/*  -> ms-map-service-2026-atmy
```

Usa path routing solo si los endpoints de la aplicacion soportan esa forma. Si no, usa listeners separados, puertos separados, hostnames separados o manten el acceso interno.

Por que: el trafico publico normalmente deberia entrar por un load balancer, no directamente contra las tasks. El ALB tambien aporta health checks y un punto de acceso estable.

## 15. Configurar Variables Del Repositorio En GitHub

Crea estas repository variables en GitHub:

```text
AWS_REGION=<aws-region>
AWS_ROLE_TO_ASSUME=arn:aws:iam::<aws-account-id>:role/github-actions-ms-simulation-deploy-role
ECR_REPOSITORY_MS_TIME=ms-time-2026-atmy
ECR_REPOSITORY_MS_MAP=ms-map-2026-atmy
ECS_CLUSTER=junior-workshop-2026-vlc
ECS_SERVICE_MS_TIME=ms-time-service-2026-atmy
ECS_SERVICE_MS_MAP=ms-map-service-2026-atmy
ECS_TASK_DEFINITION_MS_TIME=deployment/ecs/ms-time-task-definition.json
ECS_TASK_DEFINITION_MS_MAP=deployment/ecs/ms-map-task-definition.json
```

Por que: estos valores no son secretos de aplicacion, pero cambian segun la cuenta AWS y el entorno. Las repository variables mantienen el workflow reutilizable.

El workflow inicial se ejecuta manualmente con `workflow_dispatch`.
Cuando el entorno AWS ya este preparado y validado, se puede anadir el trigger de push a `main`.

Por que: durante la preparacion inicial todavia faltaran recursos AWS. Un trigger manual evita fallos automaticos en cada push hasta que ECR, ECS, IAM/OIDC y Parameter Store esten listos.

## 16. Configurar Secrets En GitHub

Con OIDC, GitHub no deberia necesitar access keys largas de AWS.

Evita:

```text
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
```

Usa GitHub secrets solo para valores que no puedan almacenarse de forma segura en AWS o expresarse como repository variables.

Por que: evitar claves estaticas de AWS reduce el riesgo de filtracion de credenciales.

## 17. Orden Recomendado Para El Primer Despliegue

Orden recomendado:

1. Crear RDS.
2. Crear CloudAMQP.
3. Crear valores en Parameter Store.
4. Crear repositorios ECR.
5. Crear CloudWatch log groups.
6. Crear roles IAM.
7. Crear ECS cluster.
8. Construir y subir las primeras imagenes Docker.
9. Registrar task definitions.
10. Crear ECS services.
11. Verificar logs y conectividad.
12. Ejecutar manualmente el despliegue con GitHub Actions.
13. Cuando el despliegue manual sea estable, activar el trigger automatico sobre `main` si se desea.

Por que: los ECS services necesitan task definitions validas; las task definitions necesitan repositorios de imagenes y parametros; y el arranque de la aplicacion necesita RDS y RabbitMQ disponibles.

## 18. Checklist De Validacion

Despues del despliegue, comprueba:

- ECR contiene imagenes para `ms-time` y `ms-map`.
- ECS cluster tiene dos servicios.
- `ms-time-service-2026-atmy` tiene desired count `1` y running count `1`.
- `ms-map-service-2026-atmy` tiene desired count `1` y running count `1`.
- CloudWatch Logs muestra ambas aplicaciones Spring Boot arrancando con perfil `prod`.
- Liquibase se ejecuta correctamente para ambos modulos.
- RDS acepta conexiones solo desde el security group de las tareas ECS.
- CloudAMQP es accesible desde las tareas ECS.
- `ms-time` responde en el puerto `8081`.
- `ms-map` responde en el puerto `8080`.
- `POST /tick/{days}` sigue publicando `time.advanced.v1`.

## 19. Fallos Comunes

### La Task Se Detiene Inmediatamente

Revisa CloudWatch Logs.

Causas probables:

- Faltan valores en Parameter Store.
- El ECS task execution role no puede leer parametros SSM.
- El endpoint o credenciales de RDS son incorrectos.
- El host, vhost o credenciales de CloudAMQP son incorrectos.
- Un security group bloquea RDS o CloudAMQP.

### No Se Puede Descargar La Imagen

Comprueba:

- El repositorio ECR existe.
- El tag de imagen existe.
- El task execution role tiene permisos de ECR.
- La subnet privada tiene NAT Gateway o VPC endpoints para ECR.

### No Aparecen Logs

Comprueba:

- El CloudWatch log group existe.
- El task execution role puede escribir logs.
- `awslogs-region` coincide con la region del despliegue.

## 20. Referencias Oficiales

- Amazon ECR private repositories: https://docs.aws.amazon.com/AmazonECR/latest/userguide/Repositories.html
- Creating an ECR private repository: https://docs.aws.amazon.com/AmazonECR/latest/userguide/repository-create.html
- Amazon ECS task definition parameters for Fargate: https://docs.aws.amazon.com/AmazonECS/latest/userguide/task_definition_parameters.html
- Amazon ECS services: https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ecs_services.html
- Amazon ECS task execution IAM role: https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task_execution_IAM_role.html
- Passing sensitive data to ECS containers: https://docs.aws.amazon.com/AmazonECS/latest/developerguide/specifying-sensitive-data.html
- AWS Systems Manager Parameter Store: https://docs.aws.amazon.com/systems-manager/latest/userguide/systems-manager-parameter-store.html
- AWS IAM OIDC federation: https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_providers_oidc.html
- GitHub OIDC with AWS: https://docs.github.com/en/actions/how-tos/secure-your-work/security-harden-deployments/oidc-in-aws
