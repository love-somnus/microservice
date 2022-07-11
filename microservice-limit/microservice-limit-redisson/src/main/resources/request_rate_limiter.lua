local rate = redis.call('hget', KEYS[1], 'rate');
local interval = redis.call('hget', KEYS[1], 'interval');
local type = redis.call('hget', KEYS[1], 'type');
assert(rate ~= false and interval ~= false and type ~= false, 'RateLimiter is not initialized')

local valueName = KEYS[2];
if type == '1' then
  valueName = KEYS[3];
end;

local currentValue = redis.call('get', valueName);
if currentValue ~= false then
  if tonumber(currentValue) < tonumber(ARGV[1]) then
    return redis.call('pttl', valueName);
  else
    redis.call('decrby', valueName, ARGV[1]);
    return nil;
  end;
else
  assert(tonumber(rate) >= tonumber(ARGV[1]), 'Requested permits amount could not exceed defined rate');
  redis.call('set', valueName, rate, 'px', interval);
  redis.call('decrby', valueName, ARGV[1]);
  return nil;
end;