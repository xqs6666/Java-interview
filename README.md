# 1. Java基础 上 中

## 1. 为什么两个对象有相同的 `hashCode` 值，它们也不一定是相等的？

如果两个对象的`hashCode` 值相等，那这两个对象不一定相等（哈希碰撞）。

如果两个对象的`hashCode` 值相等并且`equals()`方法也返回 `true`，我们才认为这两个对象相等。

如果两个对象的`hashCode` 值不相等，我们就可以直接认为这两个对象不相等。

## 2. 为什么重写 equals() 时必须重写 hashCode() 方法？

如果 `equals` 方法判断两个对象是相等的，那这两个对象的 `hashCode` 值也要相等。

## 3. String、StringBuffer、StringBuilder 的区别？

|   **特性**   |       **String**       |     **StringBuffer**     |    **StringBuilder**     |
| :----------: | :--------------------: | :----------------------: | :----------------------: |
|  **可变性**  |         不可变         |           可变           |           可变           |
| **线程安全** |     安全（不可变）     |     安全（同步方法）     |          不安全          |
|   **性能**   |    低（频繁操作时）    |      中（同步开销）      |       高（无同步）       |
| **内存效率** |   低（生成大量对象）   |      高（直接修改）      |      高（直接修改）      |
| **使用场景** | 常量或极少修改的字符串 | 多线程环境下的字符串操作 | 单线程环境下的字符串操作 |

**可变性**

`String` 是不可变的。

`StringBuilder` 与 `StringBuffer` 都继承自 `AbstractStringBuilder` 类，在 `AbstractStringBuilder` 中也是使用**字符数组保存字符串**，不过没有使用 `final` 和 `private` 关键字修饰，最关键的是这个 `AbstractStringBuilder` 类还提供了很多修改字符串的方法比如 `append` 方法。

**线程安全性**

`String` 中的对象是不可变的，也就可以理解为常量，线程安全。`AbstractStringBuilder` 是 `StringBuilder` 与 `StringBuffer` 的公共父类，定义了一些字符串的基本操作，如 `expandCapacity`、`append`、`insert`、`indexOf` 等公共方法。`StringBuffer` 对方法加了同步锁或者对调用的方法加了同步锁，所以是线程安全的。`StringBuilder` 并没有对方法进行加同步锁，所以是非线程安全的。

**性能**

每次对 `String` 类型进行改变的时候，都会生成一个新的 `String` 对象，然后将指针指向新的 `String` 对象。`StringBuffer` 每次都会对 `StringBuffer` 对象本身进行操作，而不是生成新的对象并改变对象引用。相同情况下使用 `StringBuilder` 相比使用 `StringBuffer` 仅能获得 10%~15% 左右的性能提升，但却要冒多线程不安全的风险。

**对于三者使用的总结：**

- 操作少量的数据: 适用 `String`
- 单线程操作字符串缓冲区下操作大量数据: 适用 `StringBuilder`
- 多线程操作字符串缓冲区下操作大量数据: 适用 `StringBuffer`

## 4. 字符串拼接用“+” 还是 StringBuilder?

字符串对象通过“+”的字符串拼接方式，实际上是通过 `StringBuilder` 调用 `append()` 方法实现的，拼接完成之后调用 `toString()` 得到一个 `String` 对象 。

不过，在**循环内使用“+”**进行字符串的拼接的话，存在比较明显的缺陷：**编译器不会创建单个 `StringBuilder` 以复用，会导致创建过多的 `StringBuilder` 对象**。

```java
String str1 = "he";
String str2 = "llo";
String str3 = "world";
String str4 = str1 + str2 + str3;
//由于 str1、str2 和 str3 是变量，编译器无法在编译时确定它们的值，因此会在运行时进行拼接。
//Java 会使用 StringBuilder 来实现字符串拼接 
new StringBuilder().append(str1).append(str2).append(str3).toString();
```

```java
// 错误示例：每次循环都会创建新的 StringBuilder！
String s = "";
for (int i = 0; i < 10; i++) {
    s += i; // 等价于 s = new StringBuilder().append(s).append(i).toString();
}
//正确示例
StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i).append(", ");
}
String result = sb.toString();
```

## 5. String#equals() 和 Object#equals() 有何区别？

`String` 中的 `equals` 方法是被重写过的，比较的是 String 字符串的值是否相等。 `Object` 的 `equals` 方法是比较的对象的内存地址。

## 6. 字符串常量池的作用了解吗？

**字符串常量池** 是 JVM 为了提升性能和减少内存消耗针对字符串（String 类）专门开辟的一块区域，主要目的是为了避免字符串的重复创建。

|     **创建方式**      |           **是否入池**           |              **示例**              |      **内存分配**      |
| :-------------------: | :------------------------------: | :--------------------------------: | :--------------------: |
| 字面量赋值（`"..."`） |             自动入池             |        `String s = "Java";`        |         常量池         |
|  `new String("...")`  | **不入池**（除非调用`intern()`） |  `String s = new String("Java");`  |         堆内存         |
|    `intern()`方法     |             强制入池             | `s = new String("Java").intern();` | 常量池（若池中不存在） |

```java
String s1 = "Java";       // 常量池中新建"Java"
String s2 = "Java";       // 直接复用常量池中的"Java"
System.out.println(s1 == s2); // true（引用相同）

// 未使用常量池（堆中创建1000个"Hello"对象）
for (int i = 0; i < 1000; i++) {
    String s = new String("Hello"); // ❌ 每次new都在堆中新建对象
}

// 使用常量池（仅1个"Hello"对象被复用）
for (int i = 0; i < 1000; i++) {
    String s = "Hello"; // ✅ 复用常量池中的"Hello"
}
```

## 7. String s1 = new String("abc");这句话创建了几个字符串对象？

先说答案：会创建 1 或 2 个字符串对象。

1. 字符串常量池中不存在 "abc"：会创建 2 个 字符串对象。一个在字符串常量池中，由 `ldc` 指令触发创建。一个在堆中，由 `new String()` 创建，并使用常量池中的 "abc" 进行初始化。
2. 字符串常量池中已存在 "abc"：会创建 1 个 字符串对象。该对象在堆中，由 `new String()` 创建，并使用常量池中的 "abc" 进行初始化。

1、如果字符串常量池中不存在字符串对象 “abc”，那么它首先会在字符串常量池中创建字符串对象 "abc"，然后在堆内存中再创建其中一个字符串对象 "abc"。

```java
String s1 = new String("abc");
```

2、如果字符串常量池中已存在字符串对象“abc”，则只会在堆中创建 1 个字符串对象“abc”。

```java
// 字符串常量池中已存在字符串对象“abc”
String s1 = "abc";
// 下面这段代码只会在堆中创建 1 个字符串对象“abc”
String s2 = new String("abc");
```

## 8. String#intern 方法有什么作用?

`String.intern()` 是一个 `native` (本地) 方法，用来处理字符串常量池中的字符串对象引用。它的工作流程可以概括为以下两种情况：

1. **常量池中已有相同内容的字符串对象**：如果字符串常量池中已经有一个与调用 `intern()` 方法的字符串内容相同的 `String` 对象，`intern()` 方法会**直接返回常量池中该对象的引用**。
2. **常量池中没有相同内容的字符串对象**：如果字符串常量池中还没有一个与调用 `intern()` 方法的字符串内容相同的对象，`intern()` 方法会**将当前字符串对象的引用添加到字符串常量池中，并返回该引用。**

总结：

- `intern()` 方法的主要作用是确保字符串引用在常量池中的唯一性。
- 当调用 `intern()` 时，如果常量池中已经存在相同内容的字符串，则返回常量池中已有对象的引用；否则，将该字符串添加到常量池并返回其引用。

示例代码（JDK 1.8） :

```java
// s1 指向字符串常量池中的 "Java" 对象
String s1 = "Java";
// s2 也指向字符串常量池中的 "Java" 对象，和 s1 是同一个对象
String s2 = s1.intern();
// 在堆中创建一个新的 "Java" 对象，s3 指向它
String s3 = new String("Java");
// s4 指向字符串常量池中的 "Java" 对象，和 s1 是同一个对象
String s4 = s3.intern();
// s1 和 s2 指向的是同一个常量池中的对象
System.out.println(s1 == s2); // true
// s3 指向堆中的对象，s4 指向常量池中的对象，所以不同
System.out.println(s3 == s4); // false
// s1 和 s4 都指向常量池中的同一个对象
System.out.println(s1 == s4); // true
```

## 9. String 类型的变量和常量做“+”运算时发生了什么？

* 变量拼接 : 编译器无法在编译时确定它们的值，因此会在运行时进行拼接。Java 会使用 StringBuilder 来实现字符串拼接 
* 常量拼接 : 常量字符串 ，jvm 会将其存入字符串常量池。并且，字符串常量拼接得到的字符串常量在编译阶段就已经被存放字符串常量池，这个得益于编译器的优化。

```java
String str1 = "str";
String str2 = "ing";
String str3 = "str" + "ing";//由于 "str" 和 "ing" 都是常量，编译器会在编译时进行常量折叠，将 "str" + "ing" 直接优化为 "string"。"string" 会被放入字符串常量池中
String str4 = str1 + str2;//str1 和 str2 是变量，编译器无法在编译时确定它们的值，因此会在运行时通过 StringBuilder 进行字符串拼接。拼接后的结果 "string" 是一个新的字符串对象，存储在堆内存中，而不是字符串常量池。str4 引用指向堆内存中的 "string" 对象。
String str5 = "string";//字符串字面量 "string" 会被放入字符串常量池中。str5 引用指向常量池中的 "string"。
System.out.println(str3 == str4);//false
System.out.println(str3 == str5);//true
System.out.println(str4 == str5);//false
```

不过，字符串使用 `final` 关键字声明之后，可以让编译器当做常量来处理。

示例代码：

```java
final String str1 = "str";
final String str2 = "ing";
// 下面两个表达式其实是等价的
String c = "str" + "ing";// 常量池中的对象
String d = str1 + str2; // 常量池中的对象
System.out.println(c == d);// true
```

被 `final` 关键字修饰之后的 `String` 会被编译器当做常量来处理，编译器在程序编译期就可以确定它的值，其效果就相当于访问常量。

如果 ，编译器在运行时才能知道其确切值的话，就无法对其优化。

示例代码（`str2` 在运行时才能确定其值）：

```java
final String str1 = "str";
final String str2 = getStr();
String c = "str" + "ing";// 常量池中的对象
String d = str1 + str2; // 在堆上创建的新的对象
System.out.println(c == d);// false
public static String getStr() {
      return "ing";
}
```

# 2. Java基础 下

## 1. Exception 和 Error 有什么区别？

![Java 异常类层次结构图](https://oss.javaguide.cn/github/javaguide/java/basis/types-of-exceptions-in-java.png)

在 Java 中，所有的异常都有一个共同的祖先 `java.lang` 包中的 `Throwable` 类。`Throwable` 类有两个重要的子类:

- **`Exception`** :程序本身可以处理的异常，可以通过 `catch` 来进行捕获。`Exception` 又可以分为 Checked Exception (受检查异常，必须处理) 和 Unchecked Exception (不受检查异常，可以不处理)。
- **`Error`**：`Error` 属于程序无法处理的错误 ，我们没办法通过 `catch` 来进行捕获不建议通过`catch`捕获 。例如 Java 虚拟机运行错误（`Virtual MachineError`）、虚拟机内存不够错误(`OutOfMemoryError`)、类定义错误（`NoClassDefFoundError`）等 。这些异常发生时，Java 虚拟机（JVM）一般会选择线程终止。

## 2. Checked Exception 和 Unchecked Exception 有什么区别？

|      特性      |          Checked Exception（受检异常）          |            Unchecked Exception（非受检异常）             |
| :------------: | :---------------------------------------------: | :------------------------------------------------------: |
|  **继承关系**  | 继承自 `Exception`（不包括 `RuntimeException`） |                继承自 `RuntimeException`                 |
| **编译时检查** |     必须显式处理（`try-catch` 或 `throws`）     |                        不强制处理                        |
|  **可恢复性**  |              通常表示可恢复的异常               |                  通常表示不可恢复的异常                  |
|  **常见例子**  |          `IOException`, `SQLException`          | `NullPointerException`, `ArrayIndexOutOfBoundsException` |
|  **使用场景**  |      外部资源操作（文件、网络、数据库等）       |            程序逻辑错误（空指针、数组越界等）            |

## 3. Throwable 类常用方法有哪些

- `String getMessage()`: 返回异常发生时的详细信息
- `String toString()`: 返回异常发生时的简要描述
- `String getLocalizedMessage()`: 返回异常对象的本地化信息。使用 `Throwable` 的子类覆盖这个方法，可以生成本地化信息。如果子类没有覆盖该方法，则该方法返回的信息与 `getMessage()`返回的结果相同
- `void printStackTrace()`: 在控制台上打印 `Throwable` 对象封装的异常信息
