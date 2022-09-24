## 去重的几种方式

查询的时候不显示重复项

1. `distinct`去重

弊端：字段过多的时候比较慢，因为是把两个记录的所有字段做`&&`，一样的就不要了

大伙都知道

1. `group by`去重

弊端：所有字段都要聚合函数聚合一下，很麻烦

我的评价是狗都不用

1. `row_number() over (partition by  分组列 order by 排序列) 这一组数据的列别名`

row_number()窗函数会根据partition by 分组，然后会统计每个组多少个，我们只需要查询个数为1的记录

```sql
select * from
(
--查询出重复行
select *,row_number() over (partition by UserResult order by UserResult desc)num from Table1
)A
where A.num=1
```

唯一的缺点是我不知道

## 排名问题

https://blog.csdn.net/u011726005/article/details/94592866

1. Rank，并列跳跃排名
2. DENSE_RANK 并列连续排名
3. Row_NUMBER连续排名

![img](https://cdn.mazhiyong.icu/1663051404235-39156f59-eb3c-4900-b281-5af77bc0039b.png)