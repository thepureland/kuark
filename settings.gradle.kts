rootProject.name = "kuark"
include("kuark-base")
include("kuark-config")
include("kuark-data")
include("kuark-data:kuark-data-jdbc")
findProject(":kuark-data:kuark-data-jdbc")?.name = "kuark-data-jdbc"
include("kuark-data:kuark-data-redis")
findProject(":kuark-data:kuark-data-redis")?.name = "kuark-data-redis"
include("kuark-data:kuark-data-mongo")
findProject(":kuark-data:kuark-data-mongo")?.name = "kuark-data-mongo"
include("kuark-cache")
include("kuark-auth")
include("kuark-session")
include("kuark-service")
include("kuark-web")
include("kuark-sys")
include("kuark-sys:kuark-sys-model")
findProject(":kuark-sys:kuark-sys-model")?.name = "kuark-sys-model"
include("kuark-sys:kuark-sys-iservice")
findProject(":kuark-sys:kuark-sys-iservice")?.name = "kuark-sys-iservice"
include("kuark-sys:kuark-sys-service")
findProject(":kuark-sys:kuark-sys-service")?.name = "kuark-sys-service"
include("kuark-sys:kuark-sys-web")
findProject(":kuark-sys:kuark-sys-web")?.name = "kuark-sys-web"
include("kuark-sys:kuark-sys-data")
findProject(":kuark-sys:kuark-sys-data")?.name = "kuark-sys-data"
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