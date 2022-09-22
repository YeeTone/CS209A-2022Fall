# CS209A Lecture 3

这节课主要在学习一种除了面向过程和面向对象的新型编程方法——函数式编程。

## Outline
- Functional Programming
- Lambda Expression

## Part 1.Functional Programming

将代码抽象为函数做处理，行为和数据做了分离，从而不会发生变化。

### 1.1 特点

它具有以下特点：
- 操作的基本单元是function
- function没有副作用，不会对数据有变化
- function的行为是不变的
- 支持递归操作

Function也可以像变量一样处理：
- 可以作为参数传递
- 可以像变量一样赋值
- 可以作为返回结果

更高级的Function会支持：
- 接收function为参数
- 返回另一个function为结果

### 1.2 Functional Programming没有任何的副作用 **（对于相同的输入，一定产生相同的输出）**

Example:

```python
global_list = []
def append_to_list(x):
    global_list.append(x)
    print(global_list)
```

这个就不是一个pure function，因为调用多次会影响`global_list`，从而影响输出的结果。

### 1.3 数据不变性

这个就是说，当数据定义完成后，就不会再修改数据里面的内容。

Example:

```C
int plus10(int x){
    x = x + 10;
    return x;
}
```
这个就不符合数据不变性，因为x被修改了。

```C
int plus10(int x){
    return x + 10;
}
```
这个就符合数据不变性。

### 1.4 不允许有循环

在functional programming中，`for`和`while`循环是不允许出现的，必须使用`recursion`代替。

Example:

```python
def factorial(n):
    fact = 1
    while n >= 1:
        fact = fact * n
        n = n - 1
    return fact
```

这个代码虽然可以实现阶乘，但是存在的问题是违背了数据不变性，fact和n都有在执行过程中变化

改进后：
```python
def factorial(n):
    if n <= 0:
        return 1
    return n * factorial(n - 1)
```

这个代码就解决了数据变化的问题。

### 1.5 Functional Programming的好处

- Debug，测试，并行执行都比较方便
  - 确定：相同输入必定得到相同输出
  - 无副作用
  - 数据不变性
- 简化逻辑，降低复杂度

## Part 2. Lambda表达式

从Java8首次引入。

- 匿名函数，无需名称或者标识符
- 可以不属于任何一个class
- 可以作为参数传递
- 可以在任何位置调用

### 2.1 Syntax
(param1, param2) -> {lambda expression}

lambda expression：
- 可以有单句，也可以有多句，用`;`分割
- 可以有`return`语句
- 可以有局部变量和控制语句（如if,for等等）

函数式接口可以有多个静态方法或者default方法（从Java8开始），但只能有一个抽象方法，从而可以用lambda表达式做简化。

Example:

```java
public class Practice3Answer {
    public static void main(String[] args) {
        MyFunctional m = (a,b,c)-> a > b && b > c;
        System.out.println(m.isOK(3,2,1));
    }
}

interface MyFunctional{
    boolean isOK(int a,int b, int c);
}
```

### 2.2 Type inference

lambda表达式做类型推断，很大一部分是需要泛型信息的支持。


以下代码会编译错误：
```java
List strs = new ArrayList();
strs.add("abc");
strs.add("bcd");
Collections.sort(strs, (s1, s2) -> Integer.compare(s1.length(), s2.length()));
```

编译错误的原因是List是raw type，里面的类型信息是Object，没法推断是String，也就不能用String的length方法。


### 2.3 Use cases

Lambda表达式的使用举例：
```java
(param) -> System.out.println("1 parameter: "+ param);

(param) -> {
    System.out.println("1 parameter: "+ param);
    return 0;
}

(param1, param2) -> {return param1 > param2; }
(param1, param2) -> param1 > param2;
```

以上例子，很大程度上都需要泛型的支持！

### 2.4 Method references

Lambda表达式有可能是调用一个已经存在的方法，可以直接通过方法名来调用。

Example:

```java
public interface Printer {
    public void print(String s);
}

Printer ref = s -> System.out.println(s);

Printer ref = System.out::println;
```

可以reference的方法：
- Static method

```java
str -> Integer.parseInt(str)

Integer::parseInt
```
- Instance method(Bound)

这个用法是我第一次见。

```java
public class StringConverter{
    public int convertToInt(String v1){
        return Integer.valueOf(v1);
    }
}

public interface Deserializer{
    public int deserialize(String v1);
}

StringConverter sc = new StringConverter();
Deserializer des = sc::convertToInt;

```
- Instance method(Unbound)

这个用法也是我第一次见。

```java
public interface Transformer{
    String transform(String s);
}

Transformer tf = s -> s.toLowerCase();

Transformer tf = String::toLowerCase;
```

```java
public interface Finder{
    int find(String s1, String s2);
}

Finder f = (s1, s2) -> s1.indexOf(s2);

Finder f = String::indexOf;
```
- Constructor

我对这个用法见得也不多，要好好熟悉下。

```java
Supplier<String> s = String::new;

Supplier<String> s = () -> new String();
```

### 2.5 Java Functional Interfaces

java自带一些函数式的接口：
- Consumer<T>
- Supplier<T>
- Predicate<T>
- ……

![image](https://user-images.githubusercontent.com/64548919/191656570-1423530e-3a50-4751-b913-b416f9168afd.png)