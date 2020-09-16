rootProject.name = "kuark"

include("kuark-base")

include("kuark-context")

include("kuark-ability")
include("kuark-ability:kuark-ability-auth")
findProject(":kuark-ability:kuark-ability-auth")?.name = "kuark-ability-auth"
include("kuark-ability:kuark-ability-cache")
findProject(":kuark-ability:kuark-ability-cache")?.name = "kuark-ability-cache"
include("kuark-ability:kuark-ability-data")
include("kuark-ability:kuark-ability-data:kuark-ability-data-rdb")
findProject(":kuark-ability:kuark-ability-data:kuark-ability-data-rdb")?.name = "kuark-ability-data-rdb"
include("kuark-ability:kuark-ability-data:kuark-ability-data-redis")
findProject(":kuark-ability:kuark-ability-data:kuark-ability-data-redis")?.name = "kuark-ability-data-redis"
include("kuark-ability:kuark-ability-data:kuark-ability-data-mongo")
findProject(":kuark-ability:kuark-ability-data:kuark-ability-data-mongo")?.name = "kuark-ability-data-mongo"
include("kuark-ability:kuark-ability-distributed")
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-registry")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-registry")?.name = "kuark-ability-distributed-registry"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-gateway")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-gateway")?.name = "kuark-ability-distributed-gateway"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-tx")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-tx")?.name = "kuark-ability-distributed-tx"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-breaker")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-breaker")?.name = "kuark-ability-distributed-breaker"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-caller")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-caller")?.name = "kuark-ability-distributed-caller"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-loadbalance")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-loadbalance")?.name = "kuark-ability-distributed-loadbalance"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-schedule")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-schedule")?.name = "kuark-ability-distributed-schedule"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-lock")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-lock")?.name = "kuark-ability-distributed-lock"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-monitor")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-monitor")?.name = "kuark-ability-distributed-monitor"
include("kuark-ability:kuark-ability-web")
include("kuark-ability:kuark-ability-web:kuark-ability-web-session")
findProject(":kuark-ability:kuark-ability-web:kuark-ability-web-session")?.name = "kuark-ability-web-session"
include("kuark-ability:kuark-ability-web:kuark-ability-web-ktor")
findProject(":kuark-ability:kuark-ability-web:kuark-ability-web-ktor")?.name = "kuark-ability-web-ktor"

include("kuark-service")
include("kuark-service:kuark-service-sys")
findProject(":kuark-service:kuark-service-sys")?.name = "kuark-service-sys"
include("kuark-service:kuark-service-geo")
findProject(":kuark-service:kuark-service-geo")?.name = "kuark-service-geo"
include("kuark-service:kuark-service-user")
findProject(":kuark-service:kuark-service-user")?.name = "kuark-service-user"
include("kuark-service:kuark-service-auth")
findProject(":kuark-service:kuark-service-auth")?.name = "kuark-service-auth"
include("kuark-service:kuark-service-msg")
findProject(":kuark-service:kuark-service-msg")?.name = "kuark-service-msg"

include("kuark-ui")
include("kuark-ui:kuark-ui-jfx")
findProject(":kuark-ui:kuark-ui-jfx")?.name = "kuark-ui-jfx"

include("kuark-tools")

include("kuark-test")
