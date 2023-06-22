cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  
  cond1 = cdp_check_compositional(X, zero_allowed = TRUE, na_allowed = TRUE)
  if(!is.null(cond1)) return(cond1)
  
  if(min(X + DL, na.rm = TRUE) == 0){
    ind = which(apply(X + DL, 1, min, na.rm=TRUE) == 0)
    return(sprintf("Observation %s without detection limit set", paste(ind, collapse='+')))
  }
}
cdp_analysis = function(){
  save.image('Rscripts/cdp_lrnormal_zero_repl_missing.RData')
  Ximp <- zCompositions::lrEMplus(X,dl=DL,rob=V3,ini.cov=V1,frac=V2)
  names(Ximp) = paste0("z.", colnames(X))
  
  # Ooutput
  list(
    'text' = "",
    'dataframe' = list(),
    'graph' = list(),
    'new_data' = data.frame(Ximp)
  )
  
}