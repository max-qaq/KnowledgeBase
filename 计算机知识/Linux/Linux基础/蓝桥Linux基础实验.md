## 学会使用通配符

### 一次性创建多个文件

![image-20220920224821883](https://cdn.mazhiyong.icu/image-20220920224821883.png)

使用{a..b}可以创建从a到b这些序号的文件

## Linux目录的FHS标准

![img](https://cdn.mazhiyong.icu/4-1.png)

使用`pwd`查看当前目录绝对路径

![image-20220920225323230](https://cdn.mazhiyong.icu/image-20220920225323230.png)

> 使用cd切换目录的时候，两次Tab可以自动帮你补全选中的路径，防止输入错了

## Linux文件的操作

1. 新建空白文件

   `touch`

2. 新建目录

   `mikdir`一次创建一个目录

   `mikdir -p`可以创建带子目录的目录

3. 复制文件

   `cp`一次复制一个文件

   `cp -r`可以把目录整个复制了

   ![image-20220920230145967](https://cdn.mazhiyong.icu/image-20220920230145967.png)

4. 重命名/移动文件

   `mv`

5. 查看文件

   比较常用的是`head`或者`tail`

   `tail`看日志比较方便，例如`tail -n 100 log.log`,tail也能实时更新，`tail -f -n 100 log.log`实时更新后面的



> 我现在才反应过来为啥要记这些。。过几天就忘了 用的时候或者忘了再看吧

## 环境变量

`etc/bashrc` shell变量

`etc/profile`环境变量

`echo`查看变量

![image-20220921221006351](https://cdn.mazhiyong.icu/image-20220921221006351.png)

## 搜索文件

`whereis 文件名` 但是搜索的不是很全

`locate`又快又全

`locate`命令是通过查询数据库来实现的`/var/lib/mlocate/mlocate.db`

这个数据库不是实时更新的，每天晚上会更新一次

所以我们用之前要先`updatedb`一次

![image-20220921221700236](https://cdn.mazhiyong.icu/image-20220921221700236.png)

`locate`会查询目录及其子目录下的所有文件

`find`命令查询的信息是比较全面的，同时也可以指定参数查询某些时间以前的内容

## 更改文件的信息

`chmod`更改文件的权限，权限有三种，RWX，R是4，W是写，X是可执行

​	`chmod 777 file`,第一个7是所有者，第二个7是用户组，第三个7是其他用户

​	`chmod +x filename`是给文件加上执行权限，如果不指定范围的话默认是`a`所有人，

​		`chmod u+x filename`是给文件所有者添加执行权限，一共有四个,`u`文件所属者，`g`文件所属者的组用户，`o`其他用户,`a`全体

`chown`更改文件所有者

## 文件操作和磁盘管理

`df -h`查看磁盘容量

`du -h -d 深度 ~`查看目录容量，可以指定深度

`mkfs`格式化磁盘

`mount`把磁盘挂载到目录

`fdisk`磁盘分区

`dd`复制文件，是使用输入输出流来控制的，同时也可以控制格式

​	`dd if=/dev/stdin of=test bs=10 count=1 conv=ucase`

> 找到这个目录中占用最大的前 10 个文件
>
> 参数看手册，把输出过滤一下就好了
>
> ```shell
> du -ham -d 1 ~ | sort -rh | head 10
> ```

## 任务计划crontab

```text
# Example of job definition:
# .---------------- minute (0 - 59)
# |  .------------- hour (0 - 23)
# |  |  .---------- day of month (1 - 31)
# |  |  |  .------- month (1 - 12) OR jan,feb,mar,apr ...
# |  |  |  |  .---- day of week (0 - 6) (Sunday=0 or 7) OR sun,mon,tue,wed,thu,fri,sat
# |  |  |  |  |
# *  *  *  *  * user-name command to be executed
```

`crontab -r`删除定时任务

`crontab -e`添加任务

```text
为 shiyanlou 用户添加计划任务
每天凌晨 3 点的时候定时备份 alternatives.log 到 /home/shiyanlou/tmp/ 目录
命名格式为 年-月-日，比如今天是 2017 年 4 月 1 日，那么文件名为 2017-04-01

sudo cron -f &
crontab -e # 添加
0 3 * * * sudo rm /home/shiyanlou/tmp/*
0 3 * * * sudo cp /var/log/alternatives.log /home/shiyanlou/tmp/$(date +%Y-%m-%d)
```

## 命令执行顺序控制和管道

```shell
which cowsay>/dev/null && echo "exist" || echo "not exist"
```

用上面的命令来举例，`&&`是与，如果前面返回1就不执行后面的，否则执行后面的

`||`是或，前面是1就执行后面的，否则执行前面的，就是和`&&`相反的

## 管道

所谓管道就是前面的结果交给后面去处理，很强大

`cut`可以通过分隔符分开每一行的内容，然后拼接到一起

​	`cut /etc/passwd -d ':' -f 1,6` -d是分隔符，-f是显示的内容

> **cut 命令** 用来显示行中的指定部分，删除文件中指定字段。cut 经常用来显示文件的内容，类似于 type 命令。

`wc`可以计数

`sort`排序，有`-r`倒排，`-t`指定分隔符，`-k`根据哪个字段排序，`-n`默认是字典序，加上`-n`是数字排序

> cat /etc/passwd | sort -t':' -k 3 -n

`uniq`去重

`tr`可以转换结果的格式，或者删除结果里面的特定字符

```shell
# 删除 "hello shiyanlou" 中所有的'o'，'l'，'h'
$ echo 'hello shiyanlou' | tr -d 'olh'
# 将"hello" 中的ll，去重为一个l
$ echo 'hello' | tr -s 'l'
# 将输入文本，全部转换为大写或小写输出
$ echo 'input some text here' | tr '[:lower:]' '[:upper:]'
# 上面的'[:lower:]' '[:upper:]'你也可以简单的写作'[a-z]' '[A-Z]'，当然反过来将大写变小写也是可以的
```

`col`可以转换空格和TAB

`join`把两个结果相同的行连接到一起

`paste`是直接合并，不管一不一样

## 输出重定向

`>`会覆盖文件，`>>`是追加在文件后面

`cat`会打开一个标准输入流，我们可以把cat的内容输入到文件里面,使用`heredoc`的模式

```shell
mkdir Documents
cat > Documents/test.c <<EOF //指定EOF是文件结尾，也可以使用别的符号
#include <stdio.h>

int main()
{
    printf("hello world\n");
    return 0;
}
EOF
```

![image-20220924164824219](https://cdn.mazhiyong.icu/image-20220924164824219.png)

> 在Linux里面，0代表标准输入，1是标准输出，2是标准错误

```shell
# 将标准错误重定向到标准输出，再将标准输出重定向到文件，注意要将重定向到文件写到前面
cat Documents/test.c hello.c >somefile  2>&1
```

`tee`命令可以把输出重定向到文件，同时在终端输出

![image-20220924165226263](https://cdn.mazhiyong.icu/image-20220924165226263.png)

永久重定向，方法是开启子shell

```shell
# 先开启一个子 Shell
zsh
# 使用exec替换当前进程的重定向，将标准输出重定向到一个文件
exec 1>somefile
# 后面你执行的命令的输出都将被重定向到文件中，直到你退出当前子shell，或取消exec的重定向（后面将告诉你怎么做）
ls
exit
cat somefile
```

`xargs`用于把标准输入转换成命令行输入，在管道操作里面有些命令不支持标准输入，所以要通过`xrags`转换成命令行输入

```shell
find /sbin -perm +700 |ls -l       #这个命令是错误的
find /sbin -perm +700 |xargs ls -l   #这样才是正确的
```

