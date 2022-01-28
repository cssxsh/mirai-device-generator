# [Mirai Device Generator](https://github.com/cssxsh/mirai-device-generator)

> Mirai DeviceInfo 生成器

[![maven-central](https://img.shields.io/maven-central/v/xyz.cssxsh.mirai/mirai-device-generator)](https://search.maven.org/artifact/xyz.cssxsh.mirai/mirai-device-generator)

作为插件运行时会提供 `BotConfigurationAlterer` 服务，自动替换 `configuration.deviceInfo = generator::load`  
作为 `mirai-core` 库引用时, 请自行调用 `configuration.deviceInfo = generator::load`  
目前模拟的设备信息较少，有需求的可以到 issue 提需求，或者直接 pr

## board编号参考

### 骁龙系列

| name |  board  |
|:----:|:-------:|
| 888  | sm8350  |
| 835  | msm8998 |
| 820  | msm8996 |
| 750G | sm7225  |
| 655  | msm8953 |
| 652  | msm8976 |
| 625  | msm8953 |

### 天玑系列

| name  |  board  |
|:-----:|:-------:|
| 1000+ | mt6889z |
| 1000  | mt6889  |
| 1000L | mt6885z |
|  820  | mt6875  |
|  800  | mt6873v |
|  600  | mt6853  |