rootProject.name = "kuark"
include("kuark-base")
include("kuark-context")
include("kuark-data")
include("kuark-data:kuark-data-jdbc")
findProject(":kuark-data:kuark-data-jdbc")?.name = "kuark-data-jdbc"
include("kuark-data:kuark-data-redis")
findProject(":kuark-data:kuark-data-redis")?.name = "kuark-data-redis"
include("kuark-data:kuark-data-mongo")
findProject(":kuark-data:kuark-data-mongo")?.name = "kuark-data-mongo"
include("kuark-cache")
include("kuark-auth")
include("kuark-resource")
include("kuark-resource:kuark-resource-sys")
findProject(":kuark-resource:kuark-resource-sys")?.name = "kuark-resource-sys"
include("kuark-resource:kuark-resource-geo")
findProject(":kuark-resource:kuark-resource-geo")?.name = "kuark-resource-geo"
include("kuark-resource:kuark-resource-user")
findProject(":kuark-resource:kuark-resource-user")?.name = "kuark-resource-user"
include("kuark-resource:kuark-resource-auth")
findProject(":kuark-resource:kuark-resource-auth")?.name = "kuark-resource-auth"
include("kuark-resource:kuark-resource-msg")
findProject(":kuark-resource:kuark-resource-msg")?.name = "kuark-resource-msg"
include("kuark-distributed")
include("kuark-distributed:kuark-distributed-registry")
findProject(":kuark-distributed:kuark-distributed-registry")?.name = "kuark-distributed-registry"
include("kuark-distributed:kuark-distributed-gateway")
findProject(":kuark-distributed:kuark-distributed-gateway")?.name = "kuark-distributed-gateway"
include("kuark-distributed:kuark-distributed-tx")
findProject(":kuark-distributed:kuark-distributed-tx")?.name = "kuark-distributed-tx"
include("kuark-distributed:kuark-distributed-breaker")
findProject(":kuark-distributed:kuark-distributed-breaker")?.name = "kuark-distributed-breaker"
include("kuark-distributed:kuark-distributed-caller")
findProject(":kuark-distributed:kuark-distributed-caller")?.name = "kuark-distributed-caller"
include("kuark-distributed:kuark-distributed-loadbalance")
findProject(":kuark-distributed:kuark-distributed-loadbalance")?.name = "kuark-distributed-loadbalance"
include("kuark-distributed:kuark-distributed-schedule")
findProject(":kuark-distributed:kuark-distributed-schedule")?.name = "kuark-distributed-schedule"
include("kuark-distributed:kuark-distributed-lock")
findProject(":kuark-distributed:kuark-distributed-lock")?.name = "kuark-distributed-lock"
include("kuark-distributed:kuark-distributed-monitor")
findProject(":kuark-distributed:kuark-distributed-monitor")?.name = "kuark-distributed-monitor"
include("kuark-tools")
include("kuark-ui")
include("kuark-ui:kuark-ui-jfx")
findProject(":kuark-ui:kuark-ui-jfx")?.name = "kuark-ui-jfx"
include("kuark-web")
include("kuark-web:kuark-web-session")
findProject(":kuark-web:kuark-web-session")?.name = "kuark-web-session"
include("kuark-web:kuark-web-ktor")
findProject(":kuark-web:kuark-web-ktor")?.name = "kuark-web-ktor"
include("kuark-test")
