# compiler
大三下学期的编译原理课程设计



### 一、/main/java/下的各个包的说明：
- lexer:手动构造词法分析器
- RegToNfa:正规式到NFA的转化
- NfaToDfa:NFA到DFA的转化
- MinDfa:DFA到MFA的转化，即最小化DFA
- Graph:画图工具包
- syntax:语法分析
- semantic:语义分析和生成中间代码
- ToTargetCode:生成目标代码
- javafx:前端后端总控包


### 二、启动说明
- 每个包下面都有对应的类为Test，可以单独测试每个阶段的成果，javafx包下面的Test是后端跟前端集成后的总控程序。
- 由于之前写的时候没有做细致的结构化开发，存在每个包互相调用的情况，导致耦合性非常高。但时间紧，懒得优化了。
- 本次采用maven来进行管理（然而好像并没有用到什么依赖），所有需要的资料和测试文件都在resource包下。
- 画图的工具为：graphviz，需要用到画图的地方（如NFA、DFA等）需要下载本地环境，网上搜graphviz就可以了（注意配置环境变量）。如果直接运行可能会报错。
- 前端是用javafx写的，写的很简陋，可能我不会设计吧。
- jdk:1.8,内带javafx，不用额外下载。
- 启动错误时需要仔细检查抛出异常，可能是读取文件的地址不对，也有可能是没有安装需要的包。


### 三、完成情况
- 基本完成了大部分的工作。
- 算法情况：LL1预测分析，正规式转nfa,nfa转dfa，dfa最小化,生成中间代码和目标代码。
- 生成的汇编代码不是完整的，只是遵照四元式一一对应。
- 词法，语法，语义报错都有。
