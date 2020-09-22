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
include("kuark-ability:kuark-ability-data:kuark-ability-data-memdb")
findProject(":kuark-ability:kuark-ability-data:kuark-ability-data-memdb")?.name = "kuark-ability-data-memdb"
include("kuark-ability:kuark-ability-data:kuark-ability-data-docdb")
findProject(":kuark-ability:kuark-ability-data:kuark-ability-data-docdb")?.name = "kuark-ability-data-docdb"
include("kuark-ability:kuark-ability-distributed")
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-registry")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-registry")?.name = "kuark-ability-distributed-registry"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-gateway")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-gateway")?.name = "kuark-ability-distributed-gateway"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-tx")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-tx")?.name = "kuark-ability-distributed-tx"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-breaker")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-breaker")?.name = "kuark-ability-distributed-breaker"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-loadbalance")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-loadbalance")?.name = "kuark-ability-distributed-loadbalance"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-schedule")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-schedule")?.name = "kuark-ability-distributed-schedule"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-lock")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-lock")?.name = "kuark-ability-distributed-lock"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-monitor")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-monitor")?.name = "kuark-ability-distributed-monitor"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-stream")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-stream")?.name = "kuark-ability-distributed-stream"
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-bus")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-bus")?.name = "kuark-ability-distributed-bus"
include("kuark-ability:kuark-ability-web")
include("kuark-ability:kuark-ability-web:kuark-ability-web-session")
findProject(":kuark-ability:kuark-ability-web:kuark-ability-web-session")?.name = "kuark-ability-web-session"
include("kuark-ability:kuark-ability-web:kuark-ability-web-ktor")
findProject(":kuark-ability:kuark-ability-web:kuark-ability-web-ktor")?.name = "kuark-ability-web-ktor"
include("kuark-ability:kuark-ability-mq")
findProject(":kuark-ability:kuark-ability-mq")?.name = "kuark-ability-mq"

include("kuark-service")
include("kuark-service:kuark-service-sys")
include("kuark-service:kuark-service-sys:kuark-service-sys-provider")
findProject(":kuark-service:kuark-service-sys:kuark-service-sys-provider")?.name = "kuark-service-sys-provider"
include("kuark-service:kuark-service-sys:kuark-service-sys-consumer")
findProject(":kuark-service:kuark-service-sys:kuark-service-sys-consumer")?.name = "kuark-service-sys-consumer"
include("kuark-service:kuark-service-geo")
include("kuark-service:kuark-service-geo:kuark-service-geo-provider")
findProject(":kuark-service:kuark-service-geo:kuark-service-geo-provider")?.name = "kuark-service-geo-provider"
include("kuark-service:kuark-service-geo:kuark-service-geo-consumer")
findProject(":kuark-service:kuark-service-geo:kuark-service-geo-consumer")?.name = "kuark-service-geo-consumer"
include("kuark-service:kuark-service-user")
include("kuark-service:kuark-service-user:kuark-service-user-provider")
findProject(":kuark-service:kuark-service-user:kuark-service-user-provider")?.name = "kuark-service-user-provider"
include("kuark-service:kuark-service-user:kuark-service-user-consumer")
findProject(":kuark-service:kuark-service-user:kuark-service-user-consumer")?.name = "kuark-service-user-consumer"
include("kuark-service:kuark-service-msg")
include("kuark-service:kuark-service-msg:kuark-service-msg-provider")
findProject(":kuark-service:kuark-service-msg:kuark-service-msg-provider")?.name = "kuark-service-msg-provider"
include("kuark-service:kuark-service-msg:kuark-service-msg-consumer")
findProject(":kuark-service:kuark-service-msg:kuark-service-msg-consumer")?.name = "kuark-service-msg-consumer"

include("kuark-ui")
include("kuark-ui:kuark-ui-jfx")
findProject(":kuark-ui:kuark-ui-jfx")?.name = "kuark-ui-jfx"

include("kuark-tools")

include("kuark-test")