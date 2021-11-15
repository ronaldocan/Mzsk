## 工作流 是什么？
工作流（Workflow），是对工作流程及其各操作步骤之间业务规则的抽象、概括描述。工作流建模，即将工作流程中的工作如何前后组织在一起的逻辑和规则，在计算机中以恰当的模型表达并对其实施计算。工作流要解决的主要问题是：为实现某个业务目标，利用计算机在多个参与者之间按某种预定规则自动传递文档、信息或者任务。
## BPMN 是什么？
业务流程模型和标记法（BPMN, Business Process Model and Notation）是一套图形化表示法，用于以业务流程模型详细说明各种业务流程。

BPMN 元素：
- 自定义扩展
- 事件
- 顺序流
- 网关
- 任务
- 子流程与调用活动
- 事务与并发
- 数据对象

### 事件

事件（event）通常用于为流程生命周期中发生的事情建模。事件总是图形化为圆圈。在 BPMN 2.0 中，有两种主要的事件分类：捕获（catching）与抛出（throwing）事件。

1. 信号事件：

![](https://tkjohn.github.io/flowable-userguide/images/bpmn.signal.event.throw.png)

2.

## 相关链接
- [BPMN](https://zh.wikipedia.org/wiki/%E4%B8%9A%E5%8A%A1%E6%B5%81%E7%A8%8B%E6%A8%A1%E5%9E%8B%E5%92%8C%E6%A0%87%E8%AE%B0%E6%B3%95)
- [idea插件-Activiti BPMN Visualizer](https://plugins.jetbrains.com/plugin/15222-activiti-bpmn-visualizer)
- [activiti 入门基础](https://juejin.cn/post/7006639187755532295#heading-15)
- [工作流引擎开发想法参考](https://www.cnblogs.com/duck-and-duck/p/14436373.html#4830903)
