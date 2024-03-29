rootProject.name = "kuark"

include("db")

include("kuark-base")

include("kuark-context")

include("kuark-ability")
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
include("kuark-ability:kuark-ability-distributed:kuark-ability-distributed-session")
findProject(":kuark-ability:kuark-ability-distributed:kuark-ability-distributed-session")?.name = "kuark-ability-distributed-session"
include("kuark-ability:kuark-ability-web")
include("kuark-ability:kuark-ability-web:kuark-ability-web-common")
findProject(":kuark-ability:kuark-ability-web:kuark-ability-web-common")?.name = "kuark-ability-web-common"
include("kuark-ability:kuark-ability-web:kuark-ability-web-ktor")
findProject(":kuark-ability:kuark-ability-web:kuark-ability-web-ktor")?.name = "kuark-ability-web-ktor"
include("kuark-ability:kuark-ability-web:kuark-ability-web-webflux")
findProject(":kuark-ability:kuark-ability-web:kuark-ability-web-webflux")?.name = "kuark-ability-web-webflux"
include("kuark-ability:kuark-ability-web:kuark-ability-web-springmvc")
findProject(":kuark-ability:kuark-ability-web:kuark-ability-web-springmvc")?.name = "kuark-ability-web-springmvc"
include("kuark-ability:kuark-ability-mq")
findProject(":kuark-ability:kuark-ability-mq")?.name = "kuark-ability-mq"
include("kuark-ability:kuark-ability-rules")
findProject(":kuark-ability:kuark-ability-rules")?.name = "kuark-ability-rules"
include("kuark-ability:kuark-ability-ui")
findProject(":kuark-ability:kuark-ability-ui")?.name = "kuark-ability-ui"
include("kuark-ability:kuark-ability-ui:kuark-ability-ui-jfx")
findProject(":kuark-ability:kuark-ability-ui:kuark-ability-ui-jfx")?.name = "kuark-ability-ui-jfx"

include("kuark-service")
include("kuark-service:kuark-service-sys")
findProject(":kuark-service:kuark-service-sys")?.name = "kuark-service-sys"
include("kuark-service:kuark-service-sys:kuark-service-sys-consumer")
findProject(":kuark-service:kuark-service-sys:kuark-service-sys-consumer")?.name = "kuark-service-sys-consumer"
include("kuark-service:kuark-service-sys:kuark-service-sys-common")
findProject(":kuark-service:kuark-service-sys:kuark-service-sys-common")?.name = "kuark-service-sys-common"
include("kuark-service:kuark-service-sys:kuark-service-sys-provider")
findProject(":kuark-service:kuark-service-sys:kuark-service-sys-provider")?.name = "kuark-service-sys-provider"
include("kuark-service:kuark-service-workflow")
findProject(":kuark-service:kuark-service-workflow")?.name = "kuark-service-workflow"
include("kuark-service:kuark-service-workflow:kuark-service-workflow-consumer")
findProject(":kuark-service:kuark-service-workflow:kuark-service-workflow-consumer")?.name = "kuark-service-workflow-consumer"
include("kuark-service:kuark-service-workflow:kuark-service-workflow-common")
findProject(":kuark-service:kuark-service-workflow:kuark-service-workflow-common")?.name = "kuark-service-workflow-common"
include("kuark-service:kuark-service-workflow:kuark-service-workflow-provider")
findProject(":kuark-service:kuark-service-workflow:kuark-service-workflow-provider")?.name = "kuark-service-workflow-provider"
include("kuark-service:kuark-service-user")
findProject(":kuark-service:kuark-service-user")?.name = "kuark-service-user"
include("kuark-service:kuark-service-user:kuark-service-user-common")
findProject(":kuark-service:kuark-service-user:kuark-service-user-common")?.name = "kuark-service-user-common"
include("kuark-service:kuark-service-user:kuark-service-user-consumer")
findProject(":kuark-service:kuark-service-user:kuark-service-user-consumer")?.name = "kuark-service-user-consumer"
include("kuark-service:kuark-service-user:kuark-service-user-provider")
findProject(":kuark-service:kuark-service-user:kuark-service-user-provider")?.name = "kuark-service-user-provider"
include("kuark-service:kuark-service-msg")
findProject(":kuark-service:kuark-service-msg")?.name = "kuark-service-msg"
include("kuark-service:kuark-service-msg:kuark-service-msg-consumer")
findProject(":kuark-service:kuark-service-msg:kuark-service-msg-consumer")?.name = "kuark-service-msg-consumer"
include("kuark-service:kuark-service-msg:kuark-service-msg-common")
findProject(":kuark-service:kuark-service-msg:kuark-service-msg-common")?.name = "kuark-service-msg-common"
include("kuark-service:kuark-service-msg:kuark-service-msg-provider")
findProject(":kuark-service:kuark-service-msg:kuark-service-msg-provider")?.name = "kuark-service-msg-provider"
include("kuark-service:kuark-service-geo")
findProject(":kuark-service:kuark-service-geo")?.name = "kuark-service-geo"
include("kuark-service:kuark-service-geo:kuark-service-geo-consumer")
findProject(":kuark-service:kuark-service-geo:kuark-service-geo-consumer")?.name = "kuark-service-geo-consumer"
include("kuark-service:kuark-service-geo:kuark-service-geo-common")
findProject(":kuark-service:kuark-service-geo:kuark-service-geo-common")?.name = "kuark-service-geo-common"
include("kuark-service:kuark-service-geo:kuark-service-geo-provider")
findProject(":kuark-service:kuark-service-geo:kuark-service-geo-provider")?.name = "kuark-service-geo-provider"

include("kuark-test")
include("kuark-test:kuark-test-server")
findProject(":kuark-test:kuark-test-server")?.name = "kuark-test-server"
include("kuark-test:kuark-test-common")
findProject(":kuark-test:kuark-test-common")?.name = "kuark-test-common"
include("kuark-test:kuark-test-service")
findProject(":kuark-test:kuark-test-service")?.name = "kuark-test-service"
include("kuark-test:kuark-test-server:kuark-test-server-eureka")
findProject(":kuark-test:kuark-test-server:kuark-test-server-eureka")?.name = "kuark-test-server-eureka"

include("kuark-demo")
include("kuark-demo:kuark-demo-console-single")
findProject(":kuark-demo:kuark-demo-console-single")?.name = "kuark-demo-console-single"
include("kuark-demo:kuark-demo-tools")
findProject(":kuark-demo:kuark-demo-tools")?.name = "kuark-demo-tools"
