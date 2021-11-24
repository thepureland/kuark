rootProject.name = "kuark"

include("db")

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
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-discovery")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-discovery")?.name = "kuark-ability-distributed-discovery"
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
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-client")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-client")?.name = "kuark-ability-distributed-client"
include("kuark-ability:kuark-ability-web")
include("kuark-ability:kuark-ability-web:kuark-ability-web-common")
findProject(":kuark-ability:kuark-ability-web:kuark-ability-web-common")?.name = "kuark-ability-web-common"
include("kuark-ability:kuark-ability-web:kuark-ability-web-ktor")
findProject(":kuark-ability:kuark-ability-web:kuark-ability-web-ktor")?.name = "kuark-ability-web-ktor"
include("kuark-ability:kuark-ability-web:kuark-ability-web-web")
findProject(":kuark-ability:kuark-ability-web:kuark-ability-web-web")?.name = "kuark-ability-web-web"
include("kuark-ability:kuark-ability-web:kuark-ability-web-webflux")
findProject(":kuark-ability:kuark-ability-web:kuark-ability-web-webflux")?.name = "kuark-ability-web-webflux"
include("kuark-ability:kuark-ability-web:kuark-ability-web-springmvc")
findProject(":kuark-ability:kuark-ability-web:kuark-ability-web-springmvc")?.name = "kuark-ability-web-springmvc"
include("kuark-ability:kuark-ability-mq")
findProject(":kuark-ability:kuark-ability-mq")?.name = "kuark-ability-mq"
include("kuark-ability:kuark-ability-workflow")
findProject(":kuark-ability:kuark-ability-workflow")?.name = "kuark-ability-workflow"
include("kuark-ability:kuark-ability-rules")
findProject(":kuark-ability:kuark-ability-rules")?.name = "kuark-ability-rules"
include("kuark-ability:kuark-ability-sys")
findProject(":kuark-ability:kuark-ability-sys")?.name = "kuark-ability-sys"
include("kuark-ability:kuark-ability-sys:kuark-ability-sys-provider")
findProject(":kuark-ability:kuark-ability-sys:kuark-ability-sys-provider")?.name = "kuark-ability-sys-provider"
include("kuark-ability:kuark-ability-sys:kuark-ability-sys-common")
findProject(":kuark-ability:kuark-ability-sys:kuark-ability-sys-common")?.name = "kuark-ability-sys-common"
findProject(":kuark-ability-sys:kuark-ability-sys-api")?.name = "kuark-ability-sys-api"
include("kuark-ability:kuark-ability-sys:kuark-ability-sys-api")
findProject(":kuark-ability:kuark-ability-sys:kuark-ability-sys-api")?.name = "kuark-ability-sys-api"
include("kuark-ability:kuark-ability-notify")
findProject(":kuark-ability:kuark-ability-notify")?.name = "kuark-ability-notify"
include("kuark-ability:kuark-ability-notify:kuark-ability-notify-provider")
findProject(":kuark-ability:kuark-ability-notify:kuark-ability-notify-provider")?.name = "kuark-ability-notify-provider"
include("kuark-ability:kuark-ability-notify:kuark-ability-notify-common")
findProject(":kuark-ability:kuark-ability-notify:kuark-ability-notify-common")?.name = "kuark-ability-notify-common"
include("kuark-ability:kuark-ability-notify:kuark-ability-notify-api")
findProject(":kuark-ability:kuark-ability-notify:kuark-ability-notify-api")?.name = "kuark-ability-notify-api"

include("kuark-ui")
include("kuark-ui:kuark-ui-jfx")
findProject(":kuark-ui:kuark-ui-jfx")?.name = "kuark-ui-jfx"

include("kuark-tools")

include("kuark-test")
include("kuark-test:kuark-test-server")
findProject(":kuark-test:kuark-test-server")?.name = "kuark-test-server"
include("kuark-test:kuark-test-common")
findProject(":kuark-test:kuark-test-common")?.name = "kuark-test-common"
include("kuark-test:kuark-test-service")
findProject(":kuark-test:kuark-test-service")?.name = "kuark-test-service"
include("kuark-test:kuark-test-server:kuark-test-server-eureka")
findProject(":kuark-test:kuark-test-server:kuark-test-server-eureka")?.name = "kuark-test-server-eureka"