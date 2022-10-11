# CS209A Lecture 4

这节课主要学习了Java里面的Stream。通过assignment1，我感受到了Stream在Java语言编程中的优雅和强大。Stream给我的一种感觉就是它和数据库的SQL很像，当我要实现一个需求的时候，不应该直接上来暴力写，而是看看现成的封装体系有没有实现的方法。

## Outline
- Stream API
- Optional\<T\>

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

定义：
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

### 1.5 终止操作

终止操作是stream操作的终点，并且也是stream管道调用的最后操作。

它返回的是一种非Stream类型的结果，比如说：
- 基本数据类型：count()
- 引用数据类型：collect()
- void类型：forEach()

它有一种特殊的特性，eager execution。终止操作总会更早地执行。

包括但不限于以下类别：
```
anyMatch()
allMatch()
noneMatch()
collect()
count()
findAny()
findFirst()
forEach()
min()
max()
reduce()
toArray()
```

下面将逐一做介绍：

- anyMatch操作：

定义：
```java
boolean anyMatch(Predicate<? super T> predicate)
```

回忆一下Lecture 3中对于Predicate的介绍：

```
boolean test(T t)
```

它的功能是检查Stream中是否有符合约束条件的值或对象，示例：

```java
boolean x = sList.stream().anyMatch(v -> {
    return v.startsWith("Java");
});
boolean y = sList.stream().anyMatch(s -> Character.isUpperCase(s.charAt(1)));
```

- findFirst操作

定义：
```java
Optional<T> findFirst();
```

它的功能是获取Stream当中的第一个对象。如果Stream里面没有对象了，那么它就会返回一个Empty Optional对象（注意不是null！），示例：

```java
List<String> stringList = new ArrayList<String>();
stringList.add("one");
stringList.add("two");
stringList.add("three");
Stream<String> stream = stringList.stream();
Optional<String> result = stream.findFirst();
System.out.println(result.orElse("unknown"));
```

- collect操作

当Stream做完数据筛选与处理后，很多时候需要将结果收集到一个数据结构中。

放入数组中：

```java
String[] result = stream.toArray(String[]::new);
```

放入动态容器中：

```java
List<String> result = stream.collect(Collectors.toList());
Set<String> result = stream.collect(Collectors.toSet());
TreeSet<String> result = stream.collect(Collectors.toCollection(TreeSet::new));
```

下面是一些我从来没见过的高级用法：

```java
Stream<String> stream =Stream.of("a", "bb", "cc", "ddd");
Map<String, Integer> map = stream.collect(Collectors.toMap(Function.identity(), String::length));
{bb=2, cc=2, a=1, ddd=3}

Stream<String> stream =Stream.of("a", "bb", "cc", "ddd");
String joined = stream.collect(Collectors.joining("$"));
a$bb$cc$ddd

Stream<String> stream = Stream.of("a", "bb", "cc", "ddd", "a", "bb");
Map<String, Long> group = stream.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
{cc=1, bb=2, a=2, ddd=1}

Stream<String> stream = Stream.of("a", "bb", "cc", "ddd", "a", "bb", "eee");
Map<Integer, Set<String>> group = stream.collect(Collectors.groupingBy(String::length, Collectors.toSet()));
{1=[a], 2=[bb, cc], 3=[eee, ddd]}

Stream<String> stream = Stream.of("1a", "1bb", "1c", "2a", "2a", "2bb");
Map<Character, Set<String>> group = stream.collect(Collectors.groupingBy(s->s.charAt(0), 
Collectors.mapping(s->s.substring(1), Collectors.toSet())));
{1=[bb, a, c], 2=[bb, a]}
```

- reduce（归约）操作

它的功能是将一个Stream中的所有元素以某种规则聚合成一个引用数据类型或基本数据类型，有以下几类：

```java
min()
max()
average()
sum()
reduce() <- 最普适的版本
```

reduce方法的定义：

```java
Optional<T> reduce(BinaryOperator<T> accumulator);
```

而BinaryOperator的定义是：

```
BinaryOperator<T> T apply(T t1, T t2);
```

其中这里面有讲究。t1是已经处理操作过的结果累计值，而t2是传入的下一个参数值，下面课件中的图很清晰地表示了这一点：

![image](https://user-images.githubusercontent.com/64548919/194545989-a2aaca76-b0e5-4be3-aaa7-d77953fe8f21.png)

期间有好几个要注意的问题：

1. 获得的Optional里面如果没有值（比如说之前的Stream对象是空的），那么如果使用get方法就会报异常：

```java
Exception in thread "main" java.util.NoSuchElementException: No value present
	at java.base/java.util.Optional.get(Optional.java:143)
```

解决方法是下列两种中的一种：

```java
int sum = arrayList.stream().reduce((a,b)->a+b).orElse(0);
int sum = arrayList.stream().reduce(0, (a,b)->a+b);
```

2. 下列代码的输出是`JavaScript`

```java
List<String> words = Arrays.asList("Java", "Python", "C", "C++", "JavaScript");
String x = words.stream()
                .reduce((word1, word2) -> word1.length() > word2.length() ? word1 : word2)
                .orElse("");
System.out.println(x);
```

这个题目的思考方式和上面3840000的思考方式是一致的。

```
"Java".length() < "Python".length() -> "Python"
"Python".length() > "C".length() -> "Python"
"Python".length() > "C++".length() -> "Python"
"Python".length() < "JavaScript".length() -> "JavaScript"
```

### 1.6 惰性执行

- 中间操作：延迟进行，只**记录**要进行这个操作
- 终止操作：即刻执行，**记录过的**操作一个接一个执行

惰性执行的好处在于可以利用底层实现做一些潜移默化的优化（又和SQL非常类似）。

![image](https://user-images.githubusercontent.com/64548919/194548982-9ba61199-4e58-4eb5-8731-0b4cc3950d66.png)

比如说上图的操作，如果使用默认的即刻求值方法暴力实现，那么filter需要12次检查操作以选出符合的元素，map需要2次映射操作，findFirst需要1次取元素操作，总共15次，才能获得目标值"Kim"。期间还带来了很多中间变量的存储开销。因此暴力实现对于节约时空，节约代码长度都非常不利。

Stream可以对上面的代码做优化，优化求值的先后顺序以减少操作：filter中就选出第一个符合的用3次操作，然后再各自做1次map和findFirst即可拿到目标"Kim"，一共5次操作。

## Part 2: Optional\<T\>

Optional存在的意义是解决Java中令人头痛的空指针问题，是设计模式中空对象模式的标准实现。它使用一个对象来代替`null`，这样就无需if的频繁检查。

创建Optional对象的三种方法：

```java
Optional.empty()
Optional.of()
Optional.ofNullable()
```

课件上的相关操作：

```java
Optional<String> ops = ...
String result = ops.orElse("");
String result = ops.orElseGet(() -> System.getProperty("..."));
String result = ops.orElseThrow(IllegalStateException::new);
```

flatMap和Map的区别：

它们的功能都可以做映射，但是处理的对象不一样。如果调用的映射方法已经返回了Optional类型，那么就需要用flatMap做一下扁平化展开，避免出现`Optional<Optional<T>>`的套娃形式。