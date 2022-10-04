# CS209A Lecture 4

这节课主要学习了Java里面的Stream。通过assignment1，我感受到了Stream在Java语言编程中的优雅和强大。Stream给我的一种感觉就是它和数据库的SQL很像，当我要实现一个需求的时候，不应该直接上来暴力写，而是看看现成的封装体系有没有实现的方法。

## Outline
- Stream API
- Optional<T>

## Part 1. Stream API

### 1.1 Overview
- 从Java8开始启用（注意不是I/O的那个Stream，二者没有半毛钱关系）
- 它的功能是用来处理对象的集合
  - 数据流来源于source
  - 在中间的链式操作进行处理（pipeline，管道）
  - 通过终止操作来获得结果

翻译成人话，就是说Stream的数据来源是已经准备好的数据集合；它的API分两类，一类是中间的操作，是链式调用的中间过程，主要做数据处理；另一类是终态的操作，主要将之前链式调用的结果给返回出来。

### 1.2 Create a Stream

创建Stream对象有好几种方法：
- 方法1：从Java的集合分钟获取Stream，它有自带的`stream()`方法。

方法定义：
```java
default Stream<E> stream()
```

如：

```java
List<String> list = new ArrayList<>();
list.add("a");
list.add("b");
Stream<String> stream = list.stream();
```

- 方法2：使用`Stream.generate()`方法，它需要一个`Supplier`来作为输入。它可以用来创建**无限长度**的Stream对象。

Recall: Supplier需要实现一个lambda表达式，不接收参数就能提供出返回结果，如`() -> "abc"`或者类的静态方法`Math::random`

方法定义：
```java
static <T> Stream<T> generate(Supplier<T> s)
```

如：

```java
Stream<String> echos = Stream.generate(() -> "Echo");
Stream<Double> randoms = Stream.generate(Math::random);
```

- 方法3：使用`Stream.of`方法，它可以接受任意数量的同类型参数。

方法定义：
```java
static <T> Stream<T> of(T... values)
```

如：

```java
Integer[] array = new Integer[]{1,2,3};
Stream<Integer> istream = Stream.of(array);

Stream<String> sentence = Stream.of("This", "is", "Java", "2");
```

### 1.3 Primitive Type Stream

**基本数据类型数组转包装类list**

上面介绍了非基本数据类型的创建Stream的方法。对于基本数据类型，也有将其转化为包装数据类型的操作方法，比如说：

```java
int[] ints = new int[]{1,2,3,4,5};
List<Integer> list1 = Arrays.stream(ints).boxed().toList();
List<Integer> list2 = IntStream.of(ints).boxed().toList();
```

**基本数据类型Stream**

Stream库有直接保存基本数据类型而不使用包装类的一些流，如IntStream，LongStream，DoubleStream。用法是比较简单的，如：

```java
IntStream s1 = IntStream.of(1,2,3,5,8);
IntStream s2 = IntStream.range(10);

Stream<String> sentences = Stream.of("This", "is", "Java", "2");
IntStream s3 = sentences.mapToInt(String::length);
```

### 1.4 中间操作

当对于Stream对象做一些筛选或者变形操作的时候，就需要中间操作，包括以下类型：
- filter()
- map()
- sorted()
- distinct()
- peek(), limit(), skip()

有两点需要特别注意：

1. 做了中间操作后，会生成一个新的Stream
2. 惰性计算特性：所有的中间操作都不会执行，除非调用了一个终止操作。

- filter操作

定义：
```java
Stream<T> filter(Predicate<? super T> predicate)
```

筛选出符合要求的Stream元素，示例：

```java
List<Integer> list = Arrays.asList(10,20,31);
list.stream()
    .filter(e -> e % 2 == 0)
    .forEach(e -> System.out.println(e + " "));
```

- map操作

定义：

```java
<R> Stream<R> map(Function<? super T, ? extends R> mapper)
```

Recall: Function中，R为返回类型，T是函数参数类型。

功能是通过规定的映射规则，生成一个目标类型的新Stream对象，示例：

```java
List<String> strs = new ArrayList<>();
strs.add("123");
strs.add("456");
strs.stream() // Stream<String>
    .map(Integer::parseInt) // Stream<Integer>
    .forEach(System.out::println);
```

- distinct操作：

定义：

```java
Stream<T> distinct();
```

功能是移除所有重复类型的元素并只保留1个，从而获得一个元素各不相同的Stream对象，示例：

```java
List<Integer> integers = new ArrayList<>();
integers.add(1);
integers.add(2);
integers.add(1);

List<Integer> result = integers.stream().distinct().collect(Collectors.toList()); 
```

注意`collect(Collectors.toList())`是一个终止操作。

- sorted操作：

定义L
```java
Stream<T> sorted();
Stream<T> sorted(Comparator<? super T> comparator);
```

功能是将Stream中元素以自然排序规则或者自定义排序规则进行重新排序，生成新的Stream对象，示例：

```java
class Point{
    int x,y;
    Point(int x, int y){
        this.x = x;
        this.y = y;
    }
}
aList.stream()
    .sorted((p1, p2) -> Integer.compare(p1.x, p2.x))
    .forEach(System.out::println);
```

Stream的管道操作由0个或者更多的中间操作，以及1个终止操作组成，示例：

```java
List<Integer> integers = Arrays.asList(1,2,3,4,5);
integers.stream()
    .filter(e -> e % 2 == 1)
    .limit(3)
    .map(e -> e*e)
    .forEach(System.out::println);
```