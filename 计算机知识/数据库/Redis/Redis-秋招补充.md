## Redis的优缺点：

优点：

1. 内存操作，读写速度快
2. 支持多种数据类型，如String，Hash，Set，Zset，List
3. 支持持久化，RDB和AOF
4. 支持事务
5. 支持主从复制：主节点自动会把数据同步到从节点，可以读写分离
6. Redis命令单线程的，不用考虑并发的问题。网络读写是多线程的

缺点：

1. 对结构化查询支持比较差
2. 数据库容量受到物理内存的限制，不适合海量数据的读写
3. Redis较难支持在线扩容

**对结构化查询支持比较差：**

像MySQL这种，我们可以很容易对数据进行逻辑操作，可以进行复杂的查询，而Redis更多的还是只是存取键值对

**较难支持在线扩容**

集群的在线扩容主要就是两步

1. 增加节点
2. 重新分片

Redis的集群有16384个槽，扩容之后这些槽要重新分配，

重新分配到新节点的槽就要执行迁移命令，一个一个的挪过去

把每个槽的key都挪过去

## Redis实现接口限流

Redis + Lua实现接口限流

令牌桶算法：

```lua
-- 令牌桶在redis中的key值
local tokens_key = KEYS[1]
-- 该令牌桶上一次刷新的时间对应的key的值
local timestamp_key = KEYS[2]
-- 令牌单位时间填充速率
local rate = tonumber(ARGV[1])
-- 令牌桶容量
local capacity = tonumber(ARGV[2])
-- 当前时间
local now = tonumber(ARGV[3])
-- 请求需要的令牌数
local requested = tonumber(ARGV[4])
-- 令牌桶容量/令牌填充速率=令牌桶填满所需的时间
local fill_time = capacity/rate
-- 令牌过期时间 填充时间*2
local ttl = math.floor(fill_time*2)
-- 获取上一次令牌桶剩余的令牌数
local last_tokens = tonumber(redis.call("get", tokens_key))
-- 如果没有获取到，可能是令牌桶是新的，之前不存在该令牌桶，或者该令牌桶已经好久没有使用
-- 过期了，这里需要对令牌桶进行初始化，初始情况，令牌桶是满的
if last_tokens == nil then
  last_tokens = capacity
end
-- 获取上一次刷新的时间，如果没有，或者已经过期，那么初始化为0
local last_refreshed = tonumber(redis.call("get", timestamp_key))
if last_refreshed == nil then
  last_refreshed = 0
end
-- 计算上一次刷新时间和本次刷新时间的时间差
local delta = math.max(0, now-last_refreshed)
-- delta*rate = 这个时间差可以填充的令牌数，
-- 令牌桶中先存在的令牌数 = 填充令牌数+令牌桶中原有的令牌数
-- 以为令牌桶有容量，所以如果计算的值大于令牌桶容量，那么以令牌容容量为准
local filled_tokens = math.min(capacity, last_tokens+(delta*rate))
-- 判断令牌桶中的令牌数是都满本次请求需要的令牌数，如果不满足，说明被限流了
local allowed = filled_tokens >= requested

-- 这里声明了两个变量，一个是新的令牌数，一个是是否被限流，0代表限流，1代表没有线路
local new_tokens = filled_tokens
local allowed_num = 0
-- 如果没有被限流，即，filled_tokens >= requested，
-- 新的令牌数=刚刚计算好的令牌桶中存在的令牌数减掉本次需要使用的令牌数
-- 并设置限流结果为未限流
if allowed then
  new_tokens = filled_tokens - requested
  allowed_num = 1
end
-- 存储本次操作后，令牌桶中的令牌数以及本次刷新时间
if ttl > 0 then
  redis.call("setex", tokens_key, ttl, new_tokens)
  redis.call("setex", timestamp_key, ttl, now)
end
-- 返回是否被限流标志以及令牌桶剩余令牌数
return { allowed_num, new_tokens }
```

- 这个KEYS[1],KEYS[2]ARGV[1],ARGV[2]... 表示调用该lua脚本时，传入的变量列表。
- KEYS[i] 表示调用lua脚本传过来的变量KEYS[i]作为一个key，从redis中获取具体的值
- ARGV[i] 表示调用lua脚本时传过来的变量ARGV[i]

## Redis的IO模型

Redis引入了多线程去处理网络读写，解决**网络的瓶颈**但是由于文件事件处理还是单线程的，所以不会有太多影响

待完善