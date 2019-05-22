# sqool-internal-service


此项目用于对内服务（如货架，ERP服务等等）。

通过 git submodule 引入了对sqool-common-service的依赖。

## 本地环境

### 拉取本项目
- git clone https://gitlab.com/spark_shen/sqool-internal-service.git

### 拉取sqool-common-service
- git submodule sync --recursive
- git submodule update --init --recursive
- cd sqool-common-service
- git checkout master
- git fetch && git rebase

### 生成 Eclipse 项目
- ./gradlew eclipse

### 本地打包
- ./gradlew build

### 本地数据库
- 本地安装好docker以及docker-compose
- docker-compose up -d
- 此时本地5432端口会起数据库服务，启动app时会自动连上并生成相应的表
- 用浏览器访问https://localhost:5555，用 pgadmin@abuqool.com/Passw0rd登录，可以进入便捷的数据库查询工具 PGAdmin。 
- 第一次访问时，点击添加新的服务器:
    - 名字 localhost
    - 服务器地址 postgres
    - 用户名 postgres
    - 密码 Passw0rd
    - 确认
- 展开左边服务器连接后，菜单选择查询工具，可执行SQL


### 本地起服务 (8001端口)
- java -jar ./build/libs/sqool-erp-service-1.1.0.jar

### debug模式
- java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 -jar ./build/libs/sqool-erp-service-1.1.0.jar