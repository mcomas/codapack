cdp_check = function(){
  if(!exists('X')) return("No data has been selected")
  
  cond1 = cdp_check_compositional(X, zero_allowed = TRUE)
  if(!is.null(cond1)) return(cond1)
  
  if(min(X + DL) == 0){
    ind = which(apply(X + DL, 1, min) == 0)
    return(sprintf("Observation %s without detection limit set", paste(ind, collapse='+')))
  }
}
cdp_analysis = function(){
  Ximp <- zCompositions::multRepl(X,label=0,dl=DL,frac=V1)
  names(Ximp) = paste0("z.", colnames(X))
  
  # Ooutput
  list(
    'text' = "",
    'dataframe' = list(),
    'graph' = list(),
    'new_data' = data.frame(Ximp)
  )
}