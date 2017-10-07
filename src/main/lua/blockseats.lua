-- block seats in a session
-- EVALSHA <sha1> 2 session_id exptimestamp seat1 seat2 seat3 ...
-- return [errcode (0 = no error), blockid, seat1, seat2, ...]

local sessionid = KEYS[1]
local exptimestamp = KEYS[2]

local sessionstatekey = sessionid .. ":state"
local seatskey = sessionid .. ":seats"
local countkey = sessionid .. ":count"
local unsoldkey = sessionid .. ":unsold"
local soldkey = sessionid .. ":sold"

local blockidkey = sessionid .. ":nextblockid"
local blockkeyprefix = sessionid .. ":block:"
local blockseatkeyprefix = sessionid .. ":blockseat:"
local seatkeyprefix = sessionid .. ":seat:"

local sessionisopen = function()
	return redis.call('GET', sessionstatekey) == 'open'
end

local isfree = function(seatid)
	local seatkey = seatkeyprefix .. seatid
	return redis.call('TTL', seatkey) < -1
end

local blockseat = function(seatid, blockid, expirets)
	local seatkey = seatkeyprefix .. seatid
	redis.call('SET', seatkey, blockid) 
	-- redis.call('EXPIREAT', seatkey, expirets) 	
	-- TODO : replace with EXPIREAT when called programmatically
	redis.call('EXPIRE', seatkey, 60) 	
end

local saveblock = function(blockid, expirets, avail)
	local blockkey = blockkeyprefix .. blockid
	local blockseatkey = blockseatkeyprefix .. blockid

	redis.call('HMSET', blockkey, 'id', blockid, 'expirets', expirets)

	for i=1,#avail do
		redis.call('HSET', blockseatkey, i, avail[i])		
	end
end

local getnextblockid = function()
	return redis.call('INCR', blockidkey) 
end

local getavailableseats = function(seatids)
	local avail = {}
	local comboseats = {}

	for i=1,#seatids do	
		local seatid = seatids[i]
		if isfree(seatid) then
			local comboid = redis.call('HGET', seatskey, seatid)

			-- uncombined seat
			if comboid == "0" then
				table.insert(avail, seatid)
			else
				if comboseats[comboid] == nil then
					comboseats[comboid] = {}
				end
				table.insert(comboseats[comboid], seatid)
			end
		end
	end

	-- if combinations are fully satisfied, then assign
	for comboid,reqseatids in pairs(comboseats) do
		local cneededseats = redis.call('HGET', countkey, comboid)
		if #reqseatids == tonumber(cneededseats) then
			for i=1,#reqseatids do
				table.insert(avail, reqseatids[i])
			end
		end
	end
	return avail
end

local blockseats = function (avl, blockid, expirets)
	for i=1,#avl do
		blockseat(avl[i], blockid, expirets)
	end
end

local retval = {0}

--if sessionisopen() then
	local availableseats = getavailableseats(ARGV)

	local blockid = getnextblockid()

	blockseats(availableseats, blockid, exptimestamp)

	saveblock(blockid, exptimestamp, availableseats)

	table.insert(retval, blockid)
	for i=1,#availableseats do
		table.insert(retval, availableseats[i])
	end
--else 
	-- session not open error
--	retval[1] = 1
--end


return retval
