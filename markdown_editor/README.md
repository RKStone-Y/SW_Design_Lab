# Lab Analytics
## lab1
1. Function implement
   1. 文件操作：
     I.      load: 完成 加载文件进入md编辑器（本程序）   
     II.     save: 完成 :save只是把修改的内容同步到文件中，但并不会退出程序，所以需要quit程序才算完成一次
     III.    quit: 退出程序完成整个编辑操作：只在读取命令行标准输入的过程中涉及，见Invoker/Editor.java/parseCommand(EditTools edit_tools){...};
   2. 编辑功能:   
     I.      insert:       完成
     II.     append-head:  完成
     III.    append-tail:  完成
     IV.     delete:       完成
     V.      undo:         完成 # observer mode 对需要监听的command加入监听函数进行记录，代码详见src/Receiver/CommandHistory.java
     VI.     redo:         完成
   3. 显示功能: 树结构使用组合模式生成，再用访问者模式去遍历，具体详见src/Receiver/ContentTree目录下
     I.      list:         完成
     II.     list-tree:    完成
     III.    dir-tree:     完成
   4. 日志功能: # observer mode 使用观察者模式，成功编辑一条日志之后直接将其加入日志中，代码详见src/Observer/CommandLog.java
     history:              完成
   5. 统计功能: # observer mode 同日志模式，只不过知关注于工作区域的切换，代码详见src/Observer/CommandLog.java
     stats:                完成            
2. Adaption to lab2:
   1. fileholder & Edit_tool 变成一个？或者其中函数进行调整
   2. 工作区状态恢复: memento->Snapshot
   3. Undo/Redo problem: No need to change in lab2
   4. Tree traverse 是否要用visitor（可以不修改）
   5. 统计和日志是否需要分开？考虑到多个workspace的结构
   6. save 持久化的时机

## lab2: extension from lab1
### 扩展功能:
1. 支持多个workspace(commands below)
   1. 多工作区的实现/:
      1. load [filePath]
      2. save 
      3. list
      4. open [fileName] : 切换workspace
      5. close
      6. exit
   2. 工作区恢复：(Memento)
      1. 恢复上次退出该工作区时的所有状态（不包括exit和close）
      2. _*加分项(5%)*_: 恢复工作区时，需要恢复其原有的undo/redo的状态 
2. 显示当前工作目录的内容：
   * ls
3. Command id:
   1. insert
   2. append-head
   3. append-tail
   4. delete
   5. undo
   6. redo
   7. load
   8. save
   9. open
   10. close
   11. history
   12. stats
   13. list
   14. list-tree
   15. dir-tree
   16. list-workspace
   17. ls
   
### 自动化测试
```
1. 根据讨论时分层的建议，对各层单独测试。
2. 写日志时写入了系统时间。需要在统计模块的单元测试中验证时否写入了时间。
```