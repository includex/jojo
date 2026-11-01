return {
  evaluate = function(ctx, params)
    if ctx.phase ~= "DAMAGE" then return {} end
    return {{type = "DamageModifier", amount = -math.min(tonumber(params.amount), ctx.baseDamage)}}
  end
}
