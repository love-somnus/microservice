-- 固定窗口限流(计数器算法)
local key = KEYS[1]
local rate = tonumber(ARGV[1])
local rateInterval = tonumber(ARGV[2])
local current = redis.call('get', key)
-- 非第一次调用调用，并且超过最大限制数，直接返回当前调用次数
if current and tonumber(current) > rate then
    current = redis.call('incr', key)
    return { rate, tonumber(current) }
end
-- 非第一次调用调用，没超过最大限制数，先自增1
current = redis.call('incr', key)
-- 从第一次调用开始限流，设置对应键值的过期时间
if tonumber(current) == 1 then
    redis.call('expire', key, rateInterval)
end
return { rate, tonumber(current) }