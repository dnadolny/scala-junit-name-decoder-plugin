Scala JUnit Name Decoder (Jenkins Plugin)
=========================================

This is a Jenkins plugin to display Scala JUnit test names as they would appear in the source code.

This is useful when writing descriptive test names, eg:
```
@Test def `customer paying $5.00 is charged $0.65 in tax` {
  ...
}
```
That name is very easy to read in code, but the Scala compiler encodes it to ```customer$u0020paying$u0020$5$u002E00$u0020is$u0020charged$u0020$0$u002E65$u0020in$u0020tax```, which is hard to read. This plugin will decode Scala identifiers (package name, class name, test name) before displaying them on the JUnit result page.


Before
------
![Test results without plugin](https://github.com/dnadolny/scala-junit-name-decoder-plugin/raw/master/docs/images/without_plugin.png "Test results without plugin")

After
-----
![Test results with plugin](https://github.com/dnadolny/scala-junit-name-decoder-plugin/raw/master/docs/images/with_plugin.png "Test results with plugin")