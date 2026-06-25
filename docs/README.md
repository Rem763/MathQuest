# 数字闯关

一款基于 JavaFX 的像素风数学闯关桌面游戏，使用本地 JSON 进行数据配置与存档，包含地图选关、回合战斗、结算和继续游戏等完整流程。

## 项目特点

- JavaFX 桌面应用，适合 Windows 环境运行
- 本地 JSON 存档，轻量、易调试
- 像素风 UI、`player/idle.png` 与 `slime/idle.png` 等角色素材、战斗背景图
- 地图选关、普通 / 困难难度切换
- 回合制答题战斗、倒计时、战斗动画
- 战斗结算、星级奖励、HP 继承
- 新游戏 / 继续游戏流程

## 主要功能

### 主菜单
- 新游戏
- 继续游戏

### 地图系统
- 世界地图节点展示
- 关卡选择
- 普通 / 困难难度切换
- 年级与关卡推进

### 战斗系统
- 四选一数学题答题
- 整数加减乘除题目
- 普通模式 10 秒倒计时
- 困难模式 3 秒倒计时
- 正确 / 错误 / 超时反馈
- 玩家与怪物像素图动画
- 逃跑
- 战斗结束结算

### 结算系统
- 胜利 / 失败结算
- 星级与金币奖励
- 下一关、返回地图、复活

### 存档系统
- 本地 JSON 自动保存
- 继续游戏恢复进度
- 记录玩家 HP、金币、关卡、星级、难度等信息

## 技术栈

- Java 17
- JavaFX 21
- Maven
- JSON 本地存档

## 运行要求

- JDK 17 或更高版本
- Maven 3.8+
- Windows 10/11（推荐）

## 启动方式

```bash
mvn javafx:run
```

## 存档位置

程序会在用户目录下生成存档文件：

```text
.quest-save.json
```

## 资源说明

- `src/main/resources/fxml/`：界面布局
- `src/main/resources/css/style.css`：像素风样式
- `src/main/resources/sprites/`：像素角色与背景图（如 `player/idle.png`、`slime/idle.png`）
- `src/main/resources/data/`：题目、怪物、关卡等数据

## 当前实现说明

本项目采用“JavaFX + 本地 JSON”的单机架构，不依赖 Spring Boot 或 MySQL。后续如果需要扩展云存档或排行榜，可以再演进为前后端分离架构。
