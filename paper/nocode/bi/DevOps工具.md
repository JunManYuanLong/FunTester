# DevOps工具



DevOps是IT行业的转折点。它是根据将“ 开发”和“运营 ”人员和流程整合在一起以形成稳定的运营环境的理念进行概念化的。这有助于提高操作速度并减少错误，从而优化成本，改善资源管理并增强最终产品。

DevOps鼓励开发和运营人员之间的沟通，自动化和协作，以提高最终输出的速度和质量。DevOps在各个阶段使用工具，因此自动化有助于更快，更好地输出质量。可以对工具进行分类，以进行配置管理，应用程序部署，版本控制，监视和测试以及构建系统。

在DevOps中，主要阶段是

* 持续集成
* 持续交付
* 持续部署

即使可以在三个阶段很多工具是通用的，但在交付阶段中仍需要一些特定的工具。因此，没有在特定阶段可以使用的特定工具。以下是在DevOps流程中可以使用的一些最佳工具的列表：

# 源代码存储库：

DevOps中的源代码存储库至关重要。在此检查了由编码团队编写的各种版本的代码，以使彼此的工作不会重叠。源代码存储库构成了持续集成的主要组成部分。

## Git

它是DevOps的核心组件，是开源软件。它用于版本控制，有助于维护开发人员代码库的版本。版本控制的好处是您可以选择对软件进行版本控制，共享，备份以及与其他开发人员的代码合并。使用Git，可以轻松跟踪对代码所做的更改。代码完成后，编码器将提交并将其存储在本地存储库中。在编码器推送代码后，它将存储在Git仓库中。进行更改时，可以使用Git进行拉取和更新。

## TFS

Microsoft Team Foundation Server（TFS）具有一个称为Team Foundation版本控制的版本控制，用于源代码管理。它也可以用于报告，项目管理，测试，构建自动化和发布管理。

## Subversion

也称为SVN，它是Apache Foundation开发的版本和源代码控制工具。它更多地用于Linux和其他Unix变体，是代码存储库的集中式中心。

# 构建服务器

在此执行代码执行过程。存储在源代码存储库中的代码使用各种自动化工具进行编译，然后转换为可执行代码。

## Jenkins

Jenkins是著名的开源自动化工具，用于DevOps的持续集成阶段。它集成了Git，SVN等源代码存储库。当编码人员提交代码时，Jenkins会检测到源代码存储库中发生的更改。它构建一个新的版本系统并将其部署在测试服务器中。整个代码创建完成后，Jenkins CI管道在服务器上运行代码并检查错误。如果代码在测试中失败，则会通知相关管理员。

## SonarQube

此开源工具用于管理代码质量，例如体系结构和设计，单元测试，重复，编码规则，注释，错误和复杂性。它的好处之一是它的可扩展性。

# 配置管理

这涉及服务器或环境的配置。

## Ansible

此开源自动化平台可帮助进行配置管理，任务自动化，运维自动化和应用程序部署。它不像Puppet和Chef中那样使用远程主机或代理。它需要在要管理的所有系统上安装的SSH。它有助于创建一组计算机并对其进行配置。所有命令均从中央位置发出以执行任务。它使用YAML编写的简单语法。如果要安装新版本的软件，请在清单中列出节点的IP地址，并编写一本手册以安装新版本。从控制机运行命令将新版本将安装在所有节点上。

## Puppet

此基础结构即代码（IAC）工具是一种开源软件配置工具。来自不同主机的配置存储在Puppet Master中。主机或Puppet代理通过SSL连接。当需要进行更改时，Puppet代理将连接到Puppet Master。Facter工具将Puppet Agent的完整详细信息提交给Puppet Master。利用此信息，Puppet Master可以决定如何应用配置。

## Chef

用于简化配置和维护服务器的任务。它有助于与基于云的平台集成。就像在Ansible一样，用户编写脚本来描述要执行的操作，例如配置和应用程序管理。然后可以将它们组合在一起。Chef会正确配置所有资源，并检查是否有错误。

# 虚拟基础架构

虚拟基础架构具有API，这些API使DevOps团队可以使用配置管理工具创建新计算机。云供应商提供了这些平台销售服务（PaaS）。通过将自动化工具与虚拟基础架构相结合，可以自动配置服务器。同样，可以在虚拟基础架构上测试新编写的代码并构建环境。

## Native DevOps

Native DevOps 是一个面向移动研发领域，通过自动化流程让业务交付（构建、测试、发布）更快、更稳定的平台； Native DevOps为团队提供了“一站式研发平台”、“两套经典研发模式”； 极速研发模式：适用于业务功能简单，无需协同管理的轻量级客户端； 并行研发模式：适用于业务功能复杂，需要多人多角色协同、并行研发，流程化过程管理的客户端。

## Azure DevOps

此Microsoft产品具有Azure板，Azure仓库，Azure管道，Azure测试计划和Azure工件。借助Azure Repos，您可以拥有无​​限的云托管私有Git源代码存储库。Azure Pipelines用于持续集成和持续交付。Azure测试计划用于测试管理。Azure Artifacts是将工件添加到CI / CD管道。Azure董事会将计划，跟踪和讨论各个团队之间的工作。

## Amazon Web Services

此云服务具有AWS CodePipeline，AWS CodeBuild，AWS CodeDeploy和AWS CodeStar。AWS CodePipeline用于CI / CD流程，以构建，部署和测试代码。AWS CodeBuild会编译和测试源代码。它同时处理多个构建。AWS CodeDeploy自动执行代码部署以启用更快的新版本。AWS CodeStar提供了用于部署应用程序的统一用户界面。

# 测试自动化

DevOps流程中的测试自动化还不是最后阶段。自动化测试直接在构建阶段就完成了，因此在准备好部署代码时，它就没有错误。但是，除非您拥有广泛的自动化测试工具，否则您可能需要人工干预，在该工具中您相当有信心无需进行手动测试即可部署代码。

这里请参考之前的文章；
- [如何选择正确的自动化测试工具](https://mp.weixin.qq.com/s/_Ee78UW9CxRpV5MoTrfgCQ)
- [如何选择API测试工具](https://mp.weixin.qq.com/s/m2TNJDiqAAWYV9L6UP-29w)


---
* **郑重声明**：文章首发于公众号“FunTester”，禁止第三方（腾讯云除外）转载、发表。

## 技术类文章精选

- [Linux性能监控软件netdata中文汉化版](https://mp.weixin.qq.com/s/fdXtK-5WwKnxjLZdyg6-nA)
- [性能测试框架第二版](https://mp.weixin.qq.com/s/JPyGQ2DRC6EVBmZkxAoVWA)
- [如何在Linux命令行界面愉快进行性能测试](https://mp.weixin.qq.com/s/fwGqBe1SpA2V0lPfAOd04Q)
- [图解HTTP脑图](https://mp.weixin.qq.com/s/100Vm8FVEuXs0x6rDGTipw)
- [将swagger文档自动变成测试代码](https://mp.weixin.qq.com/s/SY8mVenj0zMe5b47GS9VSQ)
 - [Selenium 4.0 Alpha更新日志](https://mp.weixin.qq.com/s/tU7sm-pcbpRNwDU9D3OVTQ)
- [Selenium 4.0 Alpha更新实践](https://mp.weixin.qq.com/s/yT9wpO5o5aWBUus494TIHw)
- [如何统一接口测试的功能、自动化和性能测试用例](https://mp.weixin.qq.com/s/1xqtXNVw7BdUa03nVcsMTg)

## 非技术文章精选

- [写给所有人的编程思维](https://mp.weixin.qq.com/s/Oj33UCnYfbUgzsBzEm2GPQ)
- [成为自动化测试的7种技能](https://mp.weixin.qq.com/s/e-HAGMO0JLR7VBBWLvk0dQ)
- [Web端自动化测试失败原因汇总](https://mp.weixin.qq.com/s/qzFth-Q9e8MTms1M8L5TyA)
- [测试人员常用借口](https://mp.weixin.qq.com/s/0k_Ciud2sOpRb5PPiVzECw)
- [API测试基础](https://mp.weixin.qq.com/s/bkbUEa9CF21xMYSlhPcULw)
- [API自动化测试指南](https://mp.weixin.qq.com/s/uy_Vn_ZVUEu3YAI1gW2T_A)
- [未来的QA测试工程师](https://mp.weixin.qq.com/s/ngL4sbEjZm7OFAyyWyQ3nQ)
- [JSON基础](https://mp.weixin.qq.com/s/tnQmAFfFbRloYp8J9TYurw)


![](https://mmbiz.qpic.cn/mmbiz_jpg/13eN86FKXzCMW6WN4Wch71qNtGQvxLRSGejZpr37OWa7CDYg5e4ZeanaGWuBgRAX3jicJNIhcyyZPXbKByXcl7w/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)