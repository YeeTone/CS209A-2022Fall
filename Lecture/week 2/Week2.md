# CS209A Lecture 2

这节课主要在复习Java1里面的泛型。

## Outline
- Generics
- Abstract Data Type (ADT)
- Collections

## Part 1. Generics

### 1.1 The world without generic

如果没有泛型，那么为了存储元素的通用性，定义可变长数组的时候，内部元素类型必须是Object（Object是所有类型的父类）。

```java
public class ArrayList {
    private Object[] elements;
    // ...
    public Object get(int i){…}
    public void add(Object o){…}
}
```

但这个实现有2个缺陷：

1. 必须显式类型转换，低效且阅读不友好
2. 显式类型转换有可能触发ClassCastException

### 1.2 Solutions

**Solution 1**

定义多个指定类型的ArrayList会存在以下问题：
1. ArrayList的类型太多了
2. 有过多的代码冗余（因为逻辑都是一样的）
3. 对于用户定义的对象，操作极不方便

**Solution 2**

Generic，从JDK 5.0开始引入

类或者接口可以指定为参数传入，编译器可以完成类型的检查

有几个关键概念：
| Example      | Term                  |
| ------------ | --------------------- |
| List<E>      | Generic type          |
| E            | Formal type parameter |
| List<String> | Parameterized type    |
| String       | Actual type parameter |
| List         | Raw type              |

Raw Type丢失了编译器的类型检查的功能，但仍然存在，原因是需要向前兼容，不能让前面的版本编译失败。

### 1.3 Using Generic

#### 1.3.1 使用方法

泛型有三种使用方法：
- Generic classes

Example: `java.util.ArrayList`
- Generic interfaces

Example: `java.lang.Comparable`
- Generic methods

Example:

```java
public static <E> Set<E> union(Set<E> s1, Set<E> s2){
    Set<E> result = new HashSet<>(s1);
    result.addAll(s2);
    return result;
}
```

#### 1.3.2 泛型边界

泛型边界的上下指的是继承树的上下关系，越往上越抽象，顶层是Object。

**上边界：extends**

必须继承对应类或者实现对应接口

Example: `? extends Fruit`

![image](https://user-images.githubusercontent.com/64548919/190294347-3b05c6fc-54b9-42d0-ab44-a283aa0f0219.png)

**下边界：super**

Example：`? super Fruit`

![image](https://user-images.githubusercontent.com/64548919/190294484-a3f286b7-6259-41d0-9ecf-eb3f76bcbc0d.png)

#### 1.3.3 泛型的继承规则

`List<String>`和`List<Object>`没有半毛钱关系，即使String是Object的子类。

#### 1.3.4 泛型通配符

泛型通配符是`?`，有三种情况：

- 无界：List<?>
- 上界：List<? extends T>
- 下界：List<? super T>

#### 1.3.5 泛型擦除

对于JVM，所有泛型的信息都会被擦除，都将转成强制类型转换。

![image](https://user-images.githubusercontent.com/64548919/190293712-6e1c30b7-5736-4ba6-9e40-8baa01fd2ecd.png)

## Part 2. Abstract Data Type (ADT)

### 2.1 概念

Data type: 数据和操作的集合体

Primitive type: 数据直接映射到机器的表示，操作直接映射到机器的指令

ADT: 数据在内存中的存储，以及内部对于数据的操作，都是对外隐藏的

Example: Stack

### 2.2 ADT的相关操作
- Creator：创建对象的接口。可以是构造器，也可以是设计模式中创建对象的接口。
- Producers：以旧换新的对象生成模式，如String.concat()
- Observers：从不同类型的角度描述ADT的相关信息，如List.size()
- Mutators：对于对象的变化操作，如List.add()

## Part 3. Collections

- Collection: 对象集合体，用于数据存储、数据检索、数据操作等等
- Framework：一系列类和接口的集合体

Core Elements of Collections:
- Interfaces: 对外暴露的操作接口
- Implementation: 对内隐藏的内部实现方法
- Algorithm: 实现方法中，实现相关操作说使用的算法

![image](https://user-images.githubusercontent.com/64548919/190297477-f7810ede-08c2-4c7f-8dac-5db7d58ffaea.png)

### 3.1 Iterable接口

- Iterable接口可以提供foreach的访问方式。

- 注意辨别Iterable接口和Iterator接口！ 

Example: Iterator接口移除所有null元素。

```java
private static void removeNulls(Collection<?> c){
    for (Iterator<?> iterator = c.iterator(); iterator.hasNext();){
        if(iterator.next() == null){
            iterator.remove();
        }
    }
}
```

### 3.2 Set接口

特点：
- 没有添加更多的方法
- 无重复元素
- 需要重写equals和hashCode方法（因为hashCode用于确定HashMap当中的存储位置，而equals是用来判断hash冲突）

Set接口实现数学意义上的子集判定，并集，交集和差集：

假定有：
```java
Set<Type> s1, s2;
// s1, s2的初始化过程和数据添加过程省略
```

- 子集判定实现：

```java
boolean isSubset = s1.containsAll(s2);
```

- 并集实现：

```java
Set<Type> union = new HashSet<>(s1);
union.addAll(s2);
```

- 交集实现：

```java
Set<Type> intersection = new HashSet<>(s1);
intersection.retainAll(s2);
```

- 差集实现：

```java
Set<Type> difference = new HashSet<>(s1);
difference.removeAll(s2);
```

### 3.3 List接口

特点：
- 是对象的一个序列

```java
public interface List<E> extends Collections<E>{
    E get(int index);
    E set(int index, E element);
    void add(int index, E element);
    E remove(int index);
    boolean addAll(int index, Collection<? extends E> c);

    int indexOf(Object o);
    int lastIndexOf(Object o);

    List<E> subList(int from, int to);

    ListIterator<E> listIterator();
    ListIterator<E> listIterator(int index);
}
```

List接口也有一些固定的操作：

假定有两个list：

```java
List<Type> a, b;
```

- 连接两个list:

```java
a.addAll(b);
```

- 移除指定下标序列的元素：

```java
a.subList(from, to).clear();
```

- 将指定下标序列的元素提取出来：

```java
List<Type> partView = a.subList(from, to);
List<Type> part = new ArrayList(partView);
partView.clear();
```

### 3.4 Map接口

### 3.5 实现原理

**List接口**

- ArrayList：数组实现。

![image](https://user-images.githubusercontent.com/64548919/190899595-74a748f6-b998-47a4-b091-28f2ed861213.png)

- LinkedList：双向链表实现。

![image](https://user-images.githubusercontent.com/64548919/190899612-e87b4a71-1fd9-410a-b5d3-452a22bfb32f.png)

| Implementation | add  | remove | set  | get  |
|----------------|------|--------|------|------|
| ArrayList      | O(n) | O(n)   | O(1) | O(1) |
| LinkedList     | O(n) | O(n)   | O(n) | O(n) |


**Map接口**

HashMap的实现原理是数组+链表。

![image](https://user-images.githubusercontent.com/64548919/190899771-aeef6ad9-0444-4e97-b60f-82a2b010a316.png)

首先通过hashCode确定存储位置:    
- 如果位置上没有元素：直接加入为头结点
- 如果位置上有元素（触发哈希碰撞）：判断是否equals相等
    - 相等：replace
    - 不相等：加入链表的结尾

其他实现：
- LinkedHashMap：需要保证插入顺序的时候使用
- TreeMap：需要保证插入时有序的时候使用

共同点：
- 除了TreeSet和TreeMap，禁止null元素出现在keys中或者values中
- 都是Serializable
- 都不是synchronized
- 都有fail-fast iterators（并发条件下，如果有多个线程尝试对集合做修改，那么就会触发异常）

### 3.6 算法
Collections类中，有针对集合体系的通用算法实现。

![image](https://user-images.githubusercontent.com/64548919/190900040-47b8ed7e-9956-4894-995d-87202d9c686f.png)

**排序**

集合排序算法的实现基础：Comparable和Comparator接口。

- Comparable接口：提供排序的默认顺序
- Comparator接口：提供自定义的排序规则（也就是说，有可能不用Comparable的相关实现规则）

**数组和列表的相互转化**

数组变列表：
```java
String[] strings = ...
List<String> list = Arrays.asList(strings);
```

列表变数组：

```java
List<String> list = ...
String[] strings = list.toArray(new String[0]);
```

**批量添加重复元素**

首先构造出有重复元素的不可变列表：
```java
List<String> list = Collections.nCopies(3, "java2");
```

然后批量添加即可：

```java
courses.addAll(list);
```

**构造单例列表以移除单一指定元素**

```java
List<String> myList = ...
// ...
// myList: {"Geeks", "code", "Practice", "Error", "Java",
//          "Class", "Error", "Practice", "Java"}

myList.removeAll(Colllections.singleton("Error"));
```

**构造空列表**

```java
Collections.emptySet();
Collections.emptyList();
Collections.emptyMap();
```
