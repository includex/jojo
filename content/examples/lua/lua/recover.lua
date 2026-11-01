return {
  evaluate = function(ctx, params)
    local owner = ctx.owner
    local amount = math.min(tonumber(params.amount), owner.maxHitPoints - owner.hitPoints)
    return {{type = "HpDelta", unitId = owner.id, amount = amount}}
  end
}
