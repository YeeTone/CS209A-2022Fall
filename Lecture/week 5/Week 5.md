# CS209A Lecture 5

这节课主要讲java里面的I/O操作。

## Outline
- I/O Overview
- i18n & Character Encoding
- Byte Streams & Character Streams
- Combining Stream Filters
- Reading/Writing Text Input/Output
- I/O from Command Line

## Part 1.Overview

- 包：java.io
- IO可以做如下的分类：
  - 输入流和输出流
  - Byte Stream和Character Stream

其中Character Stream是用来解决国际间的交流问题的。

课件上这个图很不错，上面黄色的是Byte Stream，下面蓝色的是Character Stream。

![image](https://user-images.githubusercontent.com/64548919/195105543-d2a59e6d-e835-48ca-9ffc-e1f2bb8160dd.png)

## Part 2.i18n & Character Encoding

i18n的个人理解：
- Internationalization是说要在地球上任意的机器，任意的人类语言上都应该有相同的行为（Java: Write Once, Run Everywhere）
- Character encoding: 考虑到地球上有很多很多种字符，因此需要规定一种字符的编码方式来实现上面提到的Internationalization。

Encoding的功能是将字符转成其他的形式以更好地传输和存储。
- 1837摩斯电码
- 1872中文电码

### 2.1 ASCII
7个bit表示128种character。

扩展版ASCII可以支持256种character。、

### 2.2 GB2312, GBK, GB18030

GB2312: 使用2个bytes保存一个字符。

扩展顺序：GB2312 --extend-> GBK --extend-> GB18030

GB2312扩展成GBK，GBK扩展成GB18030。

### 2.3 Unicode

**编码原理**

实现了无冲突的字符编码，本质上是int和character的映射。

范围：0 - 0x10FFFF

**存储原理**

完成了映射，接下来需要考虑如何存储。存储结构有UTF-8，UTF-16和UTF-32三种。
- UTF-8: 最小单元是1个byte，但实际上扩充至2，3，4个bytes都有可能。可以和ASCII兼容。
- UTF-16: 最小单元是2个bytes，可以扩充至4个bytes。和ASCII不兼容。
- UTF-32: 直接使用4个bytes。和ASCII不兼容。

UTF-8最巧妙的地方在于设定了空余bit位置表示前缀，从而知道这个character是由几个bytes组成的。

![image](https://user-images.githubusercontent.com/64548919/195109894-3b16ed9e-111d-4e03-a15e-2bf886629b75.png)

**Java里面的char**

是16位的无符号数，遵循Unicode的编码映射关系。int和char转换的时候，如果超范围了就会有truncate。

![image](https://user-images.githubusercontent.com/64548919/195110570-7ef9d6c4-df65-43e7-ad70-343b5c049424.png)

这个例子里面，第三个是epsilon符号的原因是0x10454被截断，变成了0x0454，也就是epsilon符号的对应数字。

Java可以通过某种变换，将int转化为目标的char数组（可能不是1长度的数组），但最终println的时候有处理，还是只会打印一个字符。（处理的机制是UTF-8的前缀机制）

## Part 3.Byte Streams & Character Streams

### 3.1 Byte Streams & Character Streams
Byte Stream：
- 操作的单位是byte
- 因为它不是直接操作可读的字符，因此操作起来很不方便

Character Stream:
- 操作的单位是Unicode字符
- 操作起来比较方便

两者有一些相似点：
- 抽象类
- 起名规则很相似
- Input的类都要实现read()方法
- Output类都要实现write()方法

### 3.2 FileInputStream

它操作的对象是byte，当读到-1的时候就说明读完了：

```java
try(InputStream input = new FileInputStream("src/test.txt")){
    int n;
    while((n = input.read()) != -1){
        System.out.println(n);
    }
}
```

文件编码的方式将影响输入得到的内容，比如说文件中存储`"计算机系统"`五个字：
- UTF-8编码：中文汉字一般用3个bytes，因此得到15个bytes
- GBK编码：要求使用2个bytes，因此得到10个bytes

强制转char会有问题，可能会导致输出的是乱码，比如说计算机系统本来是5个字，强转输出的话就变10个char或者15个char了。

### 3.3 FileReader

FileReader可以以character为单位读取文件。需要指定文件读取的编码，否则就使用默认的编码。

- Java系统的编码：
  - 这个会依赖于操作系统和环境变量
  - 是JVM启动时候的编码方式
- 文件编码：
  - 这个和Java没关系
  - 可以变化，但是应该和Java本身的文件读取编码格式保持一致

如果不存在的话就会乱码，比如说用UTF-8的格式读取GBK文件，那么就会出现illegal的状态，然后就会用默认的character（是个乱码）来替换。

## Part 4.Combining Stream Filters

我个人感觉，这个是装饰器模式的标准实现。

它的功能，是为了丰富原生文件操作流的一些功能。

课件上有3个例子。

## Part 5.Reading/Writing Text Input/Output

Java提供了人类阅读友好的文本I/O方式。
- Input: Scanning，如`java.util.Scanner`
- Output: Formatting，如`java.io.PrintWriter`

Scanner读取文件的示例：

```java
File f = new File("input.txt");
Scanner in = new Scanner(f);

while(in.hasNextDouble()){
    double v = in.nextDouble();
    // process v
}
```

PrintWriter读取文件的示例：

```java
PrintWriter out = new PrintWriter("out.txt");

out.println("Hello, World!");
out.printf("Total: %8.2f\n", total);
```

**注意！** Scanner的构造器输入字符串是错误的，那样会以字符串为输入，而不是文件！

## Part 6.I/O from the Command Line

Java对命令行交互有支持，主要两种方法：

- 标准流，如System.out
- Console终端对象，如System.console()

下面将主要讨论java的标准流。

Java的标准流有3种：
- err: PrintStream
- in: InputStream
- out: PrintStream

**in**

System.in是一个byte stream，没法按character读取。需要做包装，如下：

```java
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

// 这个是Java常用的快读模板。
```

System.in也可以和Scanner配合使用。这个在Java1的时候就练过很多次了，故不再赘述。

**out**

System.out是一个byte stream，但是它可以利用内部的character stream实现character stream的一些功能。

可以使用System.setOut重定向。

容易存在的问题是由于需要和OS交互完成输出，且很大一些输出是过于密集过多的(flooded)，因此使用logging会比较好地解决这一问题。