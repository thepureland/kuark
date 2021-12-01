kuark工作流：
1. 模型 --部署--> 定义 --启动--> 实例 --自动流转--> 任务 
2. 同一个模型衍生的定义、实例中，流程定义key和流程version始终保持一致
3. 同一个key的模型不允许重复新建，但是对已部署的模型进行更新时，实际上是自动插入一条新版本的模型记录。
4. 流程定义key、流程name、分类、业务主键、流程实例名称、任务key、任务名称、用户id、租户id等所有输入项不可以包含单引号，作为查询条件时也会被忽略。