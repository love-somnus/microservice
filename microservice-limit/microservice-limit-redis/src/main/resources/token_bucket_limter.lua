-- 令牌桶数据使用hash保存

-- KEYS[1]：令牌桶 缓存key

-- 令牌桶最大长度
local max_token_num = tonumber(ARGV[1])
-- 当前时间戳
local now_time = tonumber(ARGV[2])
-- 重置桶内令牌的单位时间（毫秒）
local interval = tonumber(ARGV[3])
-- 单位时间应该放入的令牌数
local rate = tonumber(ARGV[4])
-- 本次申请的令牌数量
local  accquire_num = tonumber(ARGV[5])
-- 上次生成令牌时间
local last_gen_token_time = tonumber(redis.call('HGET', KEYS[1], 'time') or '0')
-- 当前令牌数量
local current_token_num;
-- 如果上次生成令牌时间是0，则需要初始化令牌桶，并设置令牌数量和时间
if last_gen_token_time == 0 then
    current_token_num = max_token_num
    redis.call('HMSET', KEYS[1], 'count', current_token_num, 'time', ARGV[2])
else
    -- 当前令牌数量(令牌桶最大长度)
    current_token_num = tonumber(redis.call('HGET', KEYS[1], 'count') or '0')
    -- 期间应该产生的令牌数 = 距离上一次生成令牌的单位时间(向下取整) * 单位时间应该放入的令牌数
    local duration_token_num = math.floor((now_time - last_gen_token_time) / interval) * rate
    -- 当前令牌数量 = min(当前令牌剩余数量 + 期间应该生成的令牌数量， 令牌最大长度)
    current_token_num = math.min(current_token_num + duration_token_num, max_token_num)
    -- 如果本次生成的令牌数量大于0，则更新时间
    if duration_token_num > 0 then
        redis.call('HSET', KEYS[1], 'time', ARGV[2])
    end
end

-- 当前令牌数量 大于等于 本次申请的令牌数量
if(current_token_num >= accquire_num) then
    -- 更新令牌数量
    local new_current_token_num = current_token_num - accquire_num
    redis.call('HSET', KEYS[1], 'count', new_current_token_num)
end
return { current_token_num, accquire_num }