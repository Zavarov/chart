# chart

This project acts as a wrapper for the [JFreeChart](http://www.jfree.org/jfreechart/) library and simplifies the generation of plots.
Currently, pie charts and line charts are supported, where the latter one allows for an arbitrary number of lines.

## Getting Started

Each chart comes with two different classes that can be used as a hookpoint. An superclass and a delegating class.
In the first case, one needs to overwrite the *count* method while in the second case, the method is wrapped around a [function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html), which needs to be passes as a parameter to the constructor.

### Creating a line chart using the abstract class

The given example will plot the total number of events that occured during each timestamp.
```
public class ConcreteLineChart extends AbstractLineChart<String>{
    @Override
    protected long count(Collection<? extends String> data) {
        return data.size();
    }
}
```

### Creating a line chart using the delegator class

The resulting plot will be equivalent to the previous example, but this time it is created via the delegator class.
```
    Function<Collection<? extends String>, Long> mapper;
    DelegatingLineChart<String> chart;
    
    mapper = col -> (long)col.size();
    chart = new DelegatingLineChart<>(mapper);
```

## Installing

In order to install this project, simply execute the maven command:

```
mvn clean install
```

## Dependencies:

This project requires at least **Java 8**.
 * **JFreeChart**
   * Version: **1.5**
   * [Github](https://github.com/jfree/jfreechart)
 * **Guava**
   * Version: **28.1-jre**
   * [Github](https://github.com/google/guava)
 * **JUnit**
   * Version: **4.12**
   * [Github](https://github.com/junit-team/junit4)
 * **Apache Maven JavaDoc Plugin**
   * Version: **3.1.1**
   * [Github](https://github.com/apache/maven-javadoc-plugin/)

## Built With

* [MontiCore](https://github.com/MontiCore/monticore) - The language workbench for the comment and submission grammar in the io module.
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Zavarov**

## License

This project is licensed under the GPLv3 License - see the [LICENSE](LICENSE) file for details
