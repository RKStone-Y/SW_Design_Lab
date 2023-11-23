# 功能完成度列表
* 文件操作：
  I.      load: 完成 加载文件进入md编辑器（本程序）   
  II.     save: 完成 :save只是把修改的内容同步到文件中，但并不会退出程序，所以需要quit程序才算完成一次
  III.    quit: 退出程序完成整个编辑操作：只在读取命令行标准输入的过程中涉及，见Invoker/Editor.java/parseCommand(EditTools edit_tools){...};
* 编辑功能:   
  I.      insert:       完成
  II.     append-head:  完成
  III.    append-tail:  完成
  IV.     delete:       完成
  V.      undo:         完成 # observer mode 对需要监听的command加入监听函数进行记录，代码详见src/Receiver/CommandHistory.java
  VI.     redo:         完成
* 显示功能: 树结构使用组合模式生成，再用访问者模式去遍历，具体详见src/Receiver/ContentTree目录下
  I.      list:         完成
  II.     list-tree:    完成
  III.    dir-tree:     完成
* 日志功能: # observer mode 使用观察者模式，成功编辑一条日志之后直接将其加入日志中，代码详见src/Observer/CommandLog.java
  history:              完成
* 统计功能: # observer mode 同日志模式，只不过知关注于工作区域的切换，代码详见src/Observer/CommandLog.java
  stats:                完成            
    
