return {
  evaluate = function(ctx, params)
    if ctx.phase ~= "POWER" then return {} end
    return {{type = "AttackPowerModifier", amount = tonumber(params.amount)}}
  end
}
