# Mirai机器人插件 mirai-plugin-midjourney

## 介绍
本项目为 [Mirai](https://github.com/mamoe/mirai) 的机器人插件，对接 [NekoAPI](http://nekoapi.com/) 提供的 Midjourney API，实现 QQ 机器人的AI绘画功能，当前版本 `v0.1.0`。

本插件基于 Mirai 2.15 版本开发，支持 2.14 版本，暂未测试其他 Mirai 版本，如有其他版本请自行测试。

## 免责声明
注意，本项目仅限于学术研究，请勿用于非法用途。不建议向公众提供服务，当然也不鼓励，不支持一切商业使用。生成的图像属于 Midjourney ，任何由于此项目导致的法律问题本人概不负责。


## 使用方法
### 1 添加插件
如果你要添加该插件，请将该插件放在 Mirai 根目录下的 `/plugins` 文件夹中,并运行一次 Mirai，生成插件的 Config 文件。

### 2 编辑配置文件
目前版本暂不支持在线更新Config。在 Mirai 成功运行后，请关闭 Mirai 主程序。

在 Mirai 的根目录下找到 `/config/top.joyer.mirai-plugin-midjourney` 文件夹，在该文件夹下为本插件的配置文件。

| config.yml |  help_config   |and More...|
|:-----------|:--------------:|--------:|
| 插件主要配置文件   | 插件自带Help指令配置文件 |and More...|

### config.yml(必须配置)
| 配置项      | 类型     |                       描述                        |
|:---------|:-------|:-----------------------------------------------:|
| bot_qq   | long   |       被允许运行此插件的机器人企鹅号（必须是数字企鹅号），**必须配置**        |
| api_key  | String | 从 [NekoAPI](http://nekoapi.com/) 获取的密钥，**必须配置** |
| command_help | bool   |               插件自带Help指令的开关,默认打开                |

```yaml
bot_qq: 123456
api_key: 'sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx'
command_help: true
```
### help_config.yml (选择配置)
| 配置项      | 类型     |      描述       |
|:---------|:-------|:-------------:|
| help_text   | String | 用户执行`/?`显示的文本 |
后期会改善在配置文件中配置的便利性
```yaml
help_text: "Bot指令列表：\n\nAI绘画：\n◆ /image <关键词>  # 新的AI绘图\n◆ /change v <数字1-4> <可选：绘图ID> # 从四宫格绘图中的第{数字}幅图为底生成新的绘图\n◆ /change u <数字1-4> <可选：绘图ID> # 放大四宫格绘图中的第{数字}幅图\n◆ /change r <可选：任务ID> # 重新绘制四宫格绘图\n\n在 image 指令中添加 --niji <数字1-5> 参数，数字越大二次元浓度越高\n在 change 指令中如果不填写绘图 ID ，默认为该用户上一次生成的四宫格绘图"
```

### 3 开始使用
重新运行 Mirai 主程序，现在可以开始使用了。

## 功能
* 由用户唤起的绘图请求，并在绘图结束后获得四宫格的图像
* 由用户唤起的绘图修改请求，并在绘图结束后获得四宫格的图像或单图像

<p style="font-size: 20px;text-align: center"> <strong>⚠ 绘图指令受到 PG-13 分级的限制，如果关键词超出这个限制将不会生成图像，并弹出警告 <del>（某些瑟瑟的人呐，小心机器人封号）</del></strong> </p>

### 四宫格图像

![1695580369782414.jpg](img%2F1695580369782414.jpg)

上图可见为四宫格图像，从左到右从上到下被定义为图1、2、3、4。

### 单图像

![1695584776767642.jpg](img%2F1695584776767642.jpg)

上图为单图像，经过放大四宫格图像操作得到（例如上图是/change u 1）。

### 指令
| 指令用法                          | 描述                       |
|:------------------------------|:-------------------------|
 | /image <关键词>                  | 新的AI绘图                   |
 | /change v <数字1-4> <可选：绘图ID>   | 从四宫格绘图中的第{数字}幅图为底生成新的绘图  |
 | /change u <数字1-4> <可选：绘图ID>   | 放大四宫格绘图中的第{数字}幅图         |
 | /change r <可选：任务ID>           | 重新绘制四宫格绘图                |

在上述指令中有一个可选参数`绘图ID`，`绘图ID`在每次绘图生成时被附带生成。如果在输入指令时未输入该ID，插件会尝试使用上一次用户生成的绘图的ID，未找到时将不会执行请求。

## 出现问题
如果有任何问题、建议、Bug提交，请在 [Issues](https://github.com/JoyerLiu/mirai-plugin-midjourney/issues) 中提出。

## 开源协议
本项目基于 ![AGPLv3 协议](LICENSE) 开源。