# CS209A Lecture 1

上了第一节课，感觉这个课是一个大杂烩，CS专业的各个方面课程都有涉及，比如说CS202组成原理，CS305计算机网络，CS306数据挖掘，CS309面向对象，CS304软件工程，CS302操作系统，CS323编译原理等等。

## Outline 
- Course Introduction
- Computer system & programs
- Java overview, JVM and Virtualization
- Software design principles
- Object-Oriented programming concepts


## Part 1.Computer System & Programs


### 1.1 Computer System

- 硬件

物理层面的设备，比如说CPU，键盘，硬盘等等

- 软件    
可以往下细分为2类：
  - 系统软件：与硬件交互，并且管理硬件，比如说操作系统
  - 应用软件：与用户交互，并且完成特定的任务，比如说浏览器，音视频播放器等等


### 1.2 Programs

完成计算及其相关功能的指令序列。

执行序列的周期：Fetch-Decode-Execute
- Fetch：从memory中读取下一个指令
- Decode：解释Fetch中得到的指令
- Execute：将解释好的序列传递给CPU单元以完成操作

![image](https://user-images.githubusercontent.com/64548919/188625907-bab67639-3349-4b5d-a72b-9ba3381cb3ab.png)

由低级语言到高级语言，抽象程度逐渐增强，并且对于自然语言的相近程度也更高。

## Part 2.Java overview, JVM and Virtualization

### 2.1 Java Execution

之前说过，高级语言有更好的抽象，也有更易被人类阅读的特性。但是程序实际上要交给机器来执行。机器需要的原生与底层是与人类需要的易阅读性，易理解性相违背的，因此就需要将人类prefer的高级语言转化为机器prefer的底层语言。

![image](https://user-images.githubusercontent.com/64548919/188627125-fedd26fe-2029-47bf-9261-b08df752bd6c.png)

### 2.2 Virtualization

抽象化的好处：隐藏底层的一些实现具体细节。

#### Example 1 VMWare

#### Example 2 Docker

### 2.3 JVM执行流程

I. **类加载器**

将.jar和.class文件加载到内存

II. **运行时数据区**

- 方法区：Class-level data
- 堆区：Objects/instances
- 栈区：Local variables

III. **执行引擎**

这个部分是依赖于操作系统的，因为要把.class文件转成特定的机器指令，用解释器和JIT完成。

其中还会有内存回收过程，回收不需要的内存对象。

![image](https://user-images.githubusercontent.com/64548919/188628711-cf19b836-89e4-40ea-a704-784fb9c5cc92.png)

### 2.4 JVM, JRE, JDK

![image](https://user-images.githubusercontent.com/64548919/188629051-e41e5efc-e25c-437d-b041-995385863ac9.png)

- JDK: Java Development Kit
- JRE: Java Runtime Environment
- JVM: Java Virtual Machine

JDK > JRE > JVM

## Part 3. Software design principles

### 3.1 Tools

git: 版本管理

checkstyle: 检查代码规范

### 3.2 Conway’s Law

简而言之，就是你的团队是啥情况，设计出的产品就必定是啥情况。

我自己举两个例子说明一下：

**Example 1** 刘老师布置了编译原理的project1，一共交给6个小组完成。假定6个小组都独立完成并上交了作品，那么一定是6个不一样的版本，而不会是5个或者7个。这是因为分组的时候就分成了6组，最终一定是6个产品。

**Example 2** 面向对象课程需要做一个前后端交互的project。如果5个人分为前端组和后端组，那么最终的形式就会是前后端分离的状态，是比较好的设计；如果按功能分组，那么最终的形式就会导致前后端耦合比较高，是比较差的设计。

### 3.3 High Cohesion, Low Coupling

- Cohesion: 单个软件模块内部对于功能封装的完整性。这个数值越高越好，因为这说明这个模块内部是能够自给自足的。

- Coupling: 多个软件模块之间功能的相互依赖性。这个数值越低越好，因为这说明软件模块之间相互依赖性没那么强，如果修改了某一个模块，不至于对其他模块产生过大影响。

### 3.4 Information Hiding

**关键**：隐藏模块内部的具体实现细节，而只保留对外的交互接口。这样可以使得外部用户端无需关心具体实现的细节，从而使得如果模块的具体实现发生了变化，调用其的客户端无需做对应的修改。

举一个例子来说明这个问题。

以下部分的代码就没做好信息隐藏：
```java
class BadDemo{
    private ArrayList<Integer> list = new ArrayList<>();

    public void setList(ArrayList<Integer> list) {
        this.list = list;
    }

    public ArrayList<Integer> getList() {
        return list;
    }
}
```

这是因为暴露的list的具体类型的ArrayList，如果后续发现ArrayList类型不合适了，想换成其他的List实现类（如LinkedList），就需要修改大量代码（比如说范例中就要修改4处），把ArrayList都替换掉，很不方便。

以下是改进后的版本：

```java
class GoodDemo{
    private List<Integer> list = new ArrayList<>();

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public List<Integer> getList() {
        return list;
    }
}
```

这样将list做了抽象处理，如果后续需要修改具体实现，那么只需要变更new处的对应实现即可，很方便。

## Part 4.OOP Concepts

### 4.1 Class, Object and Instance

### 4.2 Encapsulation

将数据和对其的操作绑定在一起，并且通过修饰符阻止外部对数据的直接操作，而必须通过方法的调用接口才能完成操作。

- Rule of thumb: 对象中的数据，从外部访问的权限越低越好（也就是说直接用最狠的权限修饰符private）

提供getter和setter来实现对于数据的操作。

Tips：getter获取的类型如果不是基本数据类型，那么可能有数据安全性问题。

**Example** 22春季Java1A第四次作业第一题BigBinary，就特别强调了数据安全性问题。

### 4.3 Abstraction

隐藏具体实现细节，只保留需要最基本的内容。

Abstraction是从设计上解决问题（解决要有哪些东西），而Encapsulation是从实现上解决问题（解决这些东西应该怎么实现）。


- 抽象类

抽象类可能有一些方法是没有规定具体实现的。

不能实例化，必须要由对应子类完成实例化。

如果有抽象方法，子类必须提供具体实现；如果有具体方法，子类可以重写或直接继承使用。

- 接口

是一个或多个抽象方法的集合体，不允许有具体方法。

不能实例化，必须要有对应的实现类来完成实例化。

### 4.4 Inheritance