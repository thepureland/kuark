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
include("kuark-biz")
include("kuark-biz:kuark-biz-model")
findProject(":kuark-biz:kuark-biz-model")?.name = "kuark-biz-model"
include("kuark-biz:kuark-biz-iservice")
findProject(":kuark-biz:kuark-biz-iservice")?.name = "kuark-biz-iservice"
include("kuark-biz:kuark-biz-service")
findProject(":kuark-biz:kuark-biz-service")?.name = "kuark-biz-service"
include("kuark-biz:kuark-biz-web")
findProject(":kuark-biz:kuark-biz-web")?.name = "kuark-biz-web"
include("kuark-biz:kuark-biz-data")
findProject(":kuark-biz:kuark-biz-data")?.name = "kuark-biz-data"

