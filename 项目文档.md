# credit-management 说明文档
## 1.功能说明
    该项目支持额度管理功能，包含初始化额度、新增额度、扣减额度、查询额度；（最大额度为99999999.99，最小额度为0）
    1.1 初始化额度 接收cardId，userId，cardCredit,cardType，进行额度初始化。
        注释cardId与cardCredit分表代表卡片id，与卡片初始化额度
        注释：考虑到后续有可能需要管理用户表查询用户信息，因此引入userId；
        注释：考虑到可能需要多种金额支持（如美元，人民币），因此引入cardType；
    1.2 新增额度 接收cardId，amount，并在当前cardCredit基础上+amount。
        注释：新增后的额度不能超过最大额度（99999999.99）
    1.3 减少额度 接收cardId，amount，并在当前cardCredit基础上-amount。
        注释：减少后的额度不能小于最小额度（0）
    1.4 查询额度 接收cardId，返回cardId及cardCredit。
## 2. 定时任务模拟用户操作
    目前每100ms调用一次，可以自行调整调用频率，考虑到现实中初始化是低频操作，因此模拟操作时，初始化概率为0.01%,增加、减少、查询均为33.33%
    具体代码位于源码的scheduler.CreditOperationScheduler
## 3. 测试用例（含集成测试，controller层单元测试，service层并发操作测试）
    3.1 集成测试
        3.1.1 testInitializeCredit
            模拟用户输入，执行额度初始化，并对比期望值与返回值是否相同，期望值与数据库查询值是否相同（其中期望值为用户输入的 cardId,cardCredit）。
        3.1.2 testReduceCredit
            模拟用户输入，执行减少额度操作，并对比期望值与返回值是否相同，期望值与数据库查询值是否相同（其中期望值cardCredit=oriCardCredit-amount，其中amount为用户输入的减少额度）。
        3.1.3 testAddCredit
            模拟用户输入，执行增加额度操作，并对比期望值与返回值是否相同，期望值与数据库查询值是否相同（其中期望值cardCredit=oriCardCredit+amount，其中amount为用户输入的增加额度）。
        3.1.4 testGetCredit
            模拟用户输入，执行查询额度操作，并对比期望值与返回值是否相同，期望值与数据库查询值是否相同（其中期望值为数据库已经提前准备好的数据cardId为"fe5c2481-691f-11ef-a07e-525400fcde48"，额度为100000.0）。
    3.2 controlle层单元测试（主要含参数合法性测试，mock掉service层处理）
        3.2.1 testInitializeCredit
            额度初始化测试用例：
            1）合法参数，判断判断controller层是否能正确识别合法参数并返回200，正确返回。
            2）非法参数， 判断controller层是否能正确识别非法参数并返回400 badRequest。
                2.1）初始化金额大于最大值，
                2.2）初始化金额小于最小值，
                2.3）cardId为空，
                2.4）userId为空
        3.2.2 testReduceCredit
            额度减少测试用例：
            1）合法参数，判断判断controller层是否能正确识别合法参数并返回200，正确返回。
            2）非法参数， 判断controller层是否能正确识别非法参数并返回400 badRequest。
                2.1）cardId为空
                2.2）减少额度大于最大值
                2.3）减少额度小于最小值
        3.2.3 testAddCredit
            额度增加测试用例：
            1）合法参数，判断判断controller层是否能正确识别合法参数并返回200，正确返回。
            2）非法参数， 判断controller层是否能正确识别非法参数并返回400 badRequest。
                2.1）cardId为空
                2.2）减少额度大于最大值
                2.3）减少额度小于最小值
        3.2.4 testGetCredit
            1）合法参数，判断判断controller层是否能正确识别合法参数并返回200，正确返回。
            2）非法参数， 判断controller层是否能正确识别非法参数并返回400 badRequest。
                2.1）cardId为空
    3.3 service层单元测试（主要含并发操作测试）
        3.3.1 testAddCredit
            采用10个线程的线程池，模拟100次对同一条数据的并发增加额度操作，查看期望值与实际值是否相等。
            注释：线程池中的线程数量可调整，模拟操作次数可调整（详见代码）。
            注释： 期望值cardCredit=oriCardCredit+successCount*amount,
            其中successCount为成功执行的次数，amount为每次操作增加的金额，
            注释：实际值为并发操作后数据库查询到的数据库查询到的cardCredit。
        3.3.2 testReduceCredit
        3.3.1 testAddCredit
            采用10个线程的线程池，模拟100次对同一条数据的并发减少额度操作，查看期望值与实际值是否相等。
            注释：线程池中的线程数量可调整，模拟操作次数可调整（详见代码）。
            注释： 期望值cardCredit=oriCardCredit-successCount*amount,
            其中successCount为成功执行的次数，amount为每次操作减少的金额，
            注释：实际值为并发操作后数据库查询到的数据库查询到的cardCredit。
## 4. 数据库定义
    CREATE DATABASE IF NOT EXISTS credit_management;
    -- 选择数据库
    USE credit_management;
    -- 创建 额度表
    CREATE TABLE IF NOT EXISTS card_credit (
    card_id CHAR(36) NOT NULL PRIMARY KEY COMMENT '卡号，唯一标识信用卡',
    user_id CHAR(36) NOT NULL COMMENT ' 客户ID，用于后续关联到客户表,目前暂不使用',               --
    card_credit DOUBLE(10,2) NOT NULL COMMENT '卡片额度',
    card_type ENUM('USD','RMB') NOT NULL COMMENT '卡片金额类型（如USD:美元、RMB:人民币）',
    card_status ENUM('ACTIVE', 'CLOSED', 'SUSPENDED') NOT NULL COMMENT '卡片状态',

    create_time DATETIME NOT NULL COMMENT '建卡时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'

);
## 5.项目结构
    项目基于spring boot(3.0.2)，java(17),mysql(8.0.39 community server)完成，数据访问层采用mybatis(3.5.11)。
    项目结构如下：
    flink-kafka-demo/
    ├── src/
    │   ├── main/
    │   │   ├── java/
    │   │   │   └── coding/
    │   │   │       └── creditmanagement/  # 项目包名
    │   │   │               ├── controller/   # 控制器层，负责处理 HTTP 请求
    │   │   │               │   └── CardCreditController.java #接收额度初始化，增加，减少，查询请求
    │   │   │               ├── service/      # 业务逻辑层，包含核心业务逻辑
    │   │   │               │   └── CardCreditService.java
    │   │   │               ├── mapper/        #数据访问层，使用 MyBatis进行数据库操作
    │   │   │               │   └── CardCreditMapper.java
    │   │   │               ├── entity/          #  实体层，定义数据库中的实体对象
    │   │   │               │   └── CardCredit.java
    │   │   │               ├── dto/       # service层返回及接收数据定义
    │   │   │               │   └── CreditAddDto.java #额度增加接收参数
    │   │   │               │   └── CreditInfoDto.java #额度信息返回参数
    │   │   │               │   └── CreditInitDto.java #额度初始化接收参数
    │   │   │               │   └── CreditReduceDto.java #额度减少接收参数
    │   │   │               ├── consts/       # 常量
    │   │   │               │   └── CardConstant.java
    │   │   │               ├── enums/       # 枚举类
    │   │   │               │   └── CardType.java #卡片类型枚举
    │   │   │               │   └── CardStatus.java #卡片状态枚举
    │   │   │               ├── exception/       # 全局异常捕获处理
    │   │   │               │   └── GlobalExceptionHandler.java
    │   │   │               ├── response/       # 封装返回数据（含状态码，状态，消息，返回数据）
    │   │   │               │   └── MyResponse.java
    │   │   │               ├── scheduler/       # 定时任务，模拟用户操作
    │   │   │               │   └── CreditOperationScheduler.java
    │   │   │               ├── CardManagementApplication  # Spring主程序入口
    │   │   ├── resources/
    │   │       ├── application.yml  # 项目配置文件
    │   │       ├── mabatis-generator-configuration.yml  # mybatis代码生成器配置文件
    │   │       ├── mapper/          # MyBatis Mapper XML 文件
    │   │       │   └── CardCreditMapper.xml
    │   │       └── sql/          # sql操作相关文件
    │   │           └── ddl.sql #数据库创建及建表文件
    │   │           └── dml.sql #表更新文件（通过脚本造数【10万条】，并插入卡片额度表）
    │   ├── test/                    # 测试类
    │       └── java/
    │           └── coding/
    │               └── creditmanagement/
    │                   └── CreditManagementApplicationTests.java  # 集成测试类 
    │                       ├── controller/ # controller层单元测试
    │                       │    └── CardCreditControllerTest.java  #主要进行参数校验ces
    │                       └── service/ # service层单元测试
    │                           └── CardCreditServiceTest.java #主要进行并发操作测试
    ├── pom.xml                      # Maven 依赖文件
    └── README.md                    # 项目文档
